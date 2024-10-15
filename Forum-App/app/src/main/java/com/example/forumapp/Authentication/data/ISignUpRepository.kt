package com.example.forumapp.Authentication.data

import com.example.forumapp.Authentication.model.SignInResult
import com.example.forumapp.Authentication.model.UserData
import com.example.forumapp.profilePage.data.model.User

interface ISignUpRepository {

    // returns added user Id,
    // if could not added, returns -1
    suspend fun addUser(user: User): String?
}