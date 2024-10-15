package com.example.forumapp.Authentication.viewModels

import android.content.Context
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forumapp.Authentication.data.SignUpRepository
import com.example.forumapp.Authentication.model.SecondSignInResult
import com.example.forumapp.Authentication.model.SignInResult
import com.example.forumapp.Authentication.model.SignInState
import com.example.forumapp.Authentication.model.UserData
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.example.forumapp.profilePage.data.model.User

// delete this context later !!!
class AuthViewModel(
    private val myContext: Context,
    private val signUpRepository: SignUpRepository
    ) : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _state = MutableStateFlow(SignInState())

    fun signUp(email: String, password: String, callback: (SecondSignInResult) -> Unit) {

        if(mailIsNotValid(email)) {
            callback( SecondSignInResult(null,  "mail is not valid"))
            return
        }

        viewModelScope.launch {
            try {
                val authResult = auth.createUserWithEmailAndPassword(email, password).await()
                val user = authResult.user
                user?.uid?.let {
                    val ret = signUpRepository.addUser(User(userId= user.uid))
                }
                callback(SecondSignInResult(user?.uid, null))
                Toast.makeText(myContext, "signed up, userId ${user?.uid}", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                callback(SecondSignInResult(null, e.message))
                Log.i("error", e.toString())
                Toast.makeText(myContext, "s up failed ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun signIn(email: String, password: String, callback: (SecondSignInResult) -> Unit) {

        if(mailIsNotValid(email)) {
            callback( SecondSignInResult(null,  "mail is not valid"))
            return
        }

        viewModelScope.launch {
            try {
                val authResult = auth.signInWithEmailAndPassword(email, password).await()
                val user = authResult.user
                callback(SecondSignInResult(user?.uid, null))
                Toast.makeText(myContext, "signed in, userId ${user?.uid}", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                callback(SecondSignInResult(null, e.message))
                Log.i("error", e.toString())
                Toast.makeText(myContext, "sign in failed ${e.message}", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun mailIsNotValid(email: String): Boolean {
        if (email.isNullOrBlank() || !isValidEmail(email)) {
            Toast.makeText(myContext, "Please enter a valid email address", Toast.LENGTH_LONG).show()
            return true
        }
        return false
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun resetPassword(email: String) {
        if(mailIsNotValid(email)) return
        Toast.makeText(myContext, "trying to reset", Toast.LENGTH_LONG).show()

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    Toast.makeText(myContext, "Password reset email sent", Toast.LENGTH_LONG).show()
                }else {
                    Toast.makeText(myContext, "Failed to send reset email: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }

    }

}
