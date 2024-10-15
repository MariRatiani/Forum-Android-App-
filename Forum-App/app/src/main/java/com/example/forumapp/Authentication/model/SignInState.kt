package com.example.forumapp.Authentication.model

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)
