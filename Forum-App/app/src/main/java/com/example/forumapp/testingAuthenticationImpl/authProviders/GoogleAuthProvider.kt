package com.example.forumapp.testingAuthenticationImpl.authProviders

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.example.forumapp.R
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import com.example.forumapp.testingAuthenticationImpl.models.UserData
import com.example.forumapp.testingAuthenticationImpl.models.AuthResult

class GoogleAuthProvider(
    private val context: Context,
    private val client: SignInClient
): IGoogleAuthProvider {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override suspend fun getSignInIntentSender(): IntentSender? {
        val result = client.beginSignIn(buildSignInRequest()).await()
        return result.pendingIntent.intentSender
    }

    override suspend fun signInWithIntent(intent: Intent): AuthResult {
        val credential = client.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
        return try {
            val user = auth.signInWithCredential(googleCredential).await().user
            AuthResult(
                user = user?.let { UserData(userId = it.uid, userName = it.displayName ?: "") },
                errorMessage = null
            )
        } catch (e: Exception) {
            AuthResult(
                user = null,
                errorMessage = e.message
            )
        }
    }

    override suspend fun signOut() {
        client.signOut().await()
        auth.signOut()
    }

    override fun getCurrentUser(): UserData? {
        val user = auth.currentUser
        return user?.let { UserData(userId = it.uid, userName = it.displayName ?: "") }
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.Builder()
                    .setSupported(true)
                    .setServerClientId(context.getString(R.string.web_client_id))
                    .build()
            )
            .build()
    }
}
