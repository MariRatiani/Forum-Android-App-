package com.example.forumapp.Authentication.model


data class SecondSignInResult(
    val userId: String?,
    val errorMessage: String?
)

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)

data class UserData (
    val userId: String,
//    val userName: String
)

