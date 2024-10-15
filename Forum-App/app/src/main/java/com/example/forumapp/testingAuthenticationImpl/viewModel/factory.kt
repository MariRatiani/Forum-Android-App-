package com.example.forumapp.testingAuthenticationImpl.viewModel



import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.forumapp.testingAuthenticationImpl.authProviders.EmailAuthProvider


import com.example.forumapp.testingAuthenticationImpl.authProviders.GoogleAuthProvider

class AuthViewModelFactory(
    private val emailAuthProvider: EmailAuthProvider,
    private val googleProvider: GoogleAuthProvider
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(emailAuthProvider, googleProvider) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
