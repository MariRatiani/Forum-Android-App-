package com.example.forumapp.Authentication.data

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.example.forumapp.Authentication.model.SignInResult
import com.example.forumapp.Authentication.model.UserData
import com.example.forumapp.R
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

class GoogleAuthUIClient (
    private val context: Context,
    private val client: SignInClient
){
    private val auth = Firebase.auth

    suspend fun signIn(): IntentSender? {
        val result = try {
            client.beginSignIn(
                buildSignInRequest()
            ).await() // waits till begin  sign in is finished
        }catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) {throw e }
            null
        }
        return result?.pendingIntent?.intentSender
    }

    suspend fun signInWithIntent(intent: Intent) : SignInResult {
        val credential = client.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
        return try {
            val user = auth.signInWithCredential(googleCredential).await().user
            SignInResult(
                 data = user?.run {
                     UserData(
                         userId = uid,
//                         userName = displayName.toString()
                     )
                },
                errorMessage = null
            )
        }catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) {throw e }
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
    }

    suspend fun signOut() {
        try {
            client.signOut().await()
            auth.signOut()

        }catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) {throw e }

        }
    }


    private fun buildSignInRequest() :BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.Builder()
                    .setSupported(true) //makes sure that signing in with google is permited
                    .setFilterByAuthorizedAccounts(false) // if its true, once you sign in with one account, later only option will be that account
                    .setServerClientId(context.getString(R.string.web_client_id))
                    .build()
            ).setAutoSelectEnabled(true) // if you only have one account, you will sign in automatically
            .build()
    }

}
