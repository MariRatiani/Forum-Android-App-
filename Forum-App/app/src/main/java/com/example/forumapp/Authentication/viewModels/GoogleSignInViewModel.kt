package com.example.forumapp.Authentication.viewModels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forumapp.Authentication.model.SignInResult
import com.example.forumapp.Authentication.model.SignInState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class GoogleSignInViewModel : ViewModel() {
    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    fun onSignInResult(result: SignInResult, context: Context) {
        viewModelScope.launch {
            if (result.data != null) {
                _state.update {
                    it.copy(
                        isSignInSuccessful = true,
                        signInError = null
                    )
                }
                checkUserInDatabase(result.data.userId)
            }
                _state.update {
                it.copy(
                    isSignInSuccessful = result.data != null,
                    signInError = result.errorMessage
                )
            }
        }
    }

    private suspend fun checkUserInDatabase(userId: String) {
        val userRef = db.collection("users").document(userId)
        val document = userRef.get().await()
        if (!document.exists()) {
            // User does not exist in the database, create a new user
            val newUser = hashMapOf(
                "userId" to userId,
                "username" to (auth.currentUser?.displayName ?: ""),
                "image" to auth.currentUser?.photoUrl.toString(),
                "posts" to emptyList<String>()
            )
            userRef.set(newUser).await()
        }
    }

    fun resetState() {
        _state.update { SignInState() }
    }
}
