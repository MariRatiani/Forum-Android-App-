package com.example.forumapp.profilePage.viewModels

import android.media.Image
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.forumapp.posts.data.repository.ImageRepository
import com.example.forumapp.profilePage.data.model.User
import com.example.forumapp.profilePage.data.repository.IUserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: IUserRepository,
    private val imageRepository: ImageRepository
) : ViewModel(){
    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user
    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    init {
        auth.currentUser?.uid?.let { getUser(it) } ?: Log.d("tag1", "No user is currently logged in.")
    }

    fun getUser(userId: String) {
        viewModelScope.launch {
            val fetchedUser = userRepository.getUser(userId)
            Log.d("tag1", "fetched user: ${fetchedUser?.userId}")

            _user.postValue(fetchedUser)
        }
    }

    fun getUserName(userId: String) {
        viewModelScope.launch {
            val user = userRepository.getUser(userId)
            _user.postValue(user)
        }
    }

    fun updateUserName(newName: String) {
        viewModelScope.launch {
            userRepository.setUserName(newName)
            auth.uid?.let { getUser(it) }  // Refresh user data
        }
    }

    fun getLoggedInUser(){
        viewModelScope.launch {
            val loggedInUser = userRepository.getLoggedInUser()
            Log.d("tag1", "in getLoggedInUser ${loggedInUser.value?.userId}")
            _user.postValue(loggedInUser.value)
        }
    }
    fun updateUserPhoto(newPhotoUrl: String) {
        viewModelScope.launch {
            userRepository.setUserPhoto(newPhotoUrl)
            auth.uid?.let { getUser(it) }  // Refresh user data
        }
    }

    fun updatePassword(newPassword: String) {
        viewModelScope.launch {
            userRepository.updatePassword(newPassword)
            auth.uid?.let { getUser(it) }  // Refresh user data
        }
    }

    fun deleteUserAccount() {
        viewModelScope.launch {
            userRepository.deleteUserAccount()
        }
    }

    fun signOut() {
        viewModelScope.launch {
            userRepository.signOut()
        }
    }

    fun removeImage(imageUri: Uri?) {
        viewModelScope.launch {
            user.value?.let {
                userRepository.deleteImage(it.userId)
                imageRepository.deleteImageFromFirebase(imageUri)
                // Update _user LiveData to reflect image removal
                _user.postValue(_user.value?.copy(image = null))
            }
        }
    }

    fun updateImage(
        imageUri: Uri?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val url = uploadImage(imageUri)
            if (url != null) {
                userRepository.updateImage(url, null, onError)
                _user.postValue(_user.value?.copy(image = imageUri.toString()))
                onSuccess()
            } else {
                onError("Failed to upload image")
            }
        }
    }

    suspend fun uploadImage(imageUri: Uri?): String? {
        return imageUri?.let {
            coroutineScope {
                async { imageRepository.uploadImageToFirebase(it) }.await()
            }
        }
    }
}

class ProfileViewModelFactory(
    private val userRepository: IUserRepository,
    private val imageRepository: ImageRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(userRepository, imageRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}