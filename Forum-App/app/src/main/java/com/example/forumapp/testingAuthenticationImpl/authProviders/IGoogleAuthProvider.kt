package com.example.forumapp.testingAuthenticationImpl.authProviders

import android.content.Intent
import android.content.IntentSender
import com.example.forumapp.testingAuthenticationImpl.models.AuthResult
import com.example.forumapp.testingAuthenticationImpl.models.UserData

interface IGoogleAuthProvider {
    suspend fun getSignInIntentSender(): IntentSender?
    suspend fun signInWithIntent(intent: Intent): AuthResult
    suspend fun signOut()
    fun getCurrentUser(): UserData?

}