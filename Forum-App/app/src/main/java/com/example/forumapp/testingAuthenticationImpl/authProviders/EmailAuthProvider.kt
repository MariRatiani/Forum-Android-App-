package com.example.forumapp.testingAuthenticationImpl.authProviders

import com.google.firebase.auth.FirebaseAuth
import com.example.forumapp.testingAuthenticationImpl.models.UserData
import com.example.forumapp.testingAuthenticationImpl.models.AuthResult
import kotlinx.coroutines.tasks.await

class EmailAuthProvider : IEmailAuthProvider {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override suspend fun signUp(email: String, password: String): AuthResult {
        return try {
            val user = auth.createUserWithEmailAndPassword(email, password).await().user
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

    override suspend fun signIn(email: String, password: String): AuthResult {
        return try {
            val user = auth.signInWithEmailAndPassword(email, password).await().user
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
        auth.signOut()
    }

    override suspend fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }


    override fun getCurrentUser(): UserData? {
        val user = auth.currentUser
        return user?.let { UserData(userId = it.uid, userName = it.displayName ?: "") }
    }
}