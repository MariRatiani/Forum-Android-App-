package com.example.forumapp.testingAuthenticationImpl.authProviders

import com.example.forumapp.testingAuthenticationImpl.models.UserData
import com.example.forumapp.testingAuthenticationImpl.models.AuthResult


interface IEmailAuthProvider {
    suspend fun signUp(email: String, password: String): AuthResult
    suspend fun signIn(email: String, password: String): AuthResult
    suspend fun signOut()
    fun getCurrentUser(): UserData?
    suspend fun resetPassword(email: String)
}
