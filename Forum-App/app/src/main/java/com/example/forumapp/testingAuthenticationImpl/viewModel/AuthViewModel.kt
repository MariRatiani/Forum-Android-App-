package com.example.forumapp.testingAuthenticationImpl.viewModel


import android.content.Intent
import android.content.IntentSender
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forumapp.testingAuthenticationImpl.authProviders.EmailAuthProvider
import com.example.forumapp.testingAuthenticationImpl.authProviders.IGoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
//import com.example.forumapp.testingImpl.models.UserData
//import com.example.forumapp.testingImpl.models.AuthResult

class AuthViewModel(
    private val emailAuthProvider: EmailAuthProvider,
    private val googleAuthProvider: IGoogleAuthProvider
) : ViewModel() {

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    private val _signInIntentSender = MutableStateFlow<IntentSender?>(null)
    val signInIntentSender = _signInIntentSender.asStateFlow()

    fun signUpWithEmail(email: String, password: String) {
        viewModelScope.launch {
            val result = emailAuthProvider.signUp(email, password)
            Log.d("inSignUp", "signing up ${result.user}")
            _state.update {
                it.copy(
                    isSignInSuccessful = result.user != null,
                    signInError = result.errorMessage
                )
//                Log.d("inSignUp", "isSignInSuccessful : ${state,isSign=}")

            }
        }
    }

    fun signInWithEmail(email: String, password: String) {
        viewModelScope.launch {
            val result = emailAuthProvider.signIn(email, password)
            _state.update {
                it.copy(
                    isSignInSuccessful = result.user != null,
                    signInError = result.errorMessage
                )
            }
        }
    }

    fun requestGoogleSignIn() {
        viewModelScope.launch {
            val intentSender = googleAuthProvider.getSignInIntentSender()
            _signInIntentSender.value = intentSender
        }
    }

    fun handleGoogleSignInResult(intent: Intent) {
        viewModelScope.launch {
            val result = googleAuthProvider.signInWithIntent(intent)
            _state.update {
                it.copy(
                    isSignInSuccessful = result.user != null,
                    signInError = result.errorMessage
                )
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            emailAuthProvider.signOut()
            googleAuthProvider.signOut()
            _state.update {
                SignInState()
            }
        }
    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            emailAuthProvider.resetPassword(email)
        }
    }

    fun resetState() {
        _state.update { SignInState() }
    }
}

