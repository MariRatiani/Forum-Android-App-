package com.example.forumapp.profilePage.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.forumapp.profilePage.data.model.User

interface IUserRepository {
    fun getLoggedInUser(): MutableLiveData<User?>
    suspend fun getUser(userId: String): User?
    suspend fun setUserName(newName: String)
    suspend fun setUserPhoto(newPhotoUrl: String)
    suspend fun updatePassword(newPassword: String)
    suspend fun signOut()
    suspend fun deleteUserAccount()
    suspend fun updateImage(url: String,
                            onSuccess: (() -> Unit)?,
                            onError: ((String) -> Unit))
    suspend fun deleteImage(userId: String)
    suspend fun searchUsersByUsername(query: String): List<User>
}
