package com.example.forumapp.Authentication.UI

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.forumapp.Authentication.viewModels.AuthViewModel

@Composable
fun MailAuthComponent(
    viewModel: AuthViewModel,
    onSignInSuccess: (userId: String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Button(onClick = { viewModel.signUp(email, password) { result ->
            if (result.errorMessage == null) {
                if (result.userId != null) {

                    onSignInSuccess(result.userId)
                }
            } else {
                Log.d("errorM", result.errorMessage)
            }
        }}) {
            Text("Sign Up")
        }
        Button(onClick = { viewModel.signIn(email, password) {
            secondSignInResult ->
            if (secondSignInResult.errorMessage == null) {
                if (secondSignInResult.userId != null) {
                    onSignInSuccess(secondSignInResult.userId)
                }
            } else {
                Log.d("errorM", secondSignInResult.errorMessage)
            }
        } }) {
            Text("Sign In")
        }
        Button(onClick = { viewModel.resetPassword(email) }) {
            Text("Reset Password")
        }
    }
}
