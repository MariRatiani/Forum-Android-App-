package com.example.forumapp.profilePage.postOwnerProfilePage.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.forumapp.posts.data.repository.ImageRepository
import com.example.forumapp.profilePage.data.model.User
import com.example.forumapp.profilePage.data.repository.IUserRepository
import kotlinx.coroutines.launch

class PostOwnerProfileViewModel(
    private val userRepository: IUserRepository,
) : ViewModel() {
    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user

    init {

    }

    fun getUser(userId: String) {
        viewModelScope.launch {
            val fetchedUser = userRepository.getUser(userId)
//            Log.d("tag1", "fetched user: ${fetchedUser?.userId}")
            _user.postValue(fetchedUser)
        }
    }

//    fun getUserName(userId: String) {
//        viewModelScope.launch {
//            val user = userRepository.getUser(userId)
//            _user.postValue(user)
//        }
//    }
}


class PostOwnerProfileViewModelFactory(
    private val userRepository: IUserRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostOwnerProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PostOwnerProfileViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}