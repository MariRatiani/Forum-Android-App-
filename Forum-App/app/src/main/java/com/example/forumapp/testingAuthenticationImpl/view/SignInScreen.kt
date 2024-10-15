package com.example.forumapp.testingAuthenticationImpl.view


import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.forumapp.testingAuthenticationImpl.viewModel.AuthViewModel

@Composable
fun SignInScreen(
    authViewModel: AuthViewModel,
    onSignInSuccess: () -> Unit,
    context: Context
) {
    val state by authViewModel.state.collectAsStateWithLifecycle()
    val signInIntentSender by authViewModel.signInIntentSender.collectAsStateWithLifecycle()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let {
                    authViewModel.handleGoogleSignInResult(it)
                }
            }
        }
    )

    LaunchedEffect(key1 = state.isSignInSuccessful) {
        if (state.isSignInSuccessful) {
            onSignInSuccess()
            Toast.makeText(context, "Sign in Successful", Toast.LENGTH_LONG).show()
        }else {
            Toast.makeText(context, state.signInError?.toString(), Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(key1 = signInIntentSender) {
        signInIntentSender?.let {
            launcher.launch(IntentSenderRequest.Builder(it).build())
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
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
            Button(onClick = { authViewModel.signUpWithEmail(email, password) }) {
                Text("Sign Up")
            }
            Button(onClick = { authViewModel.signInWithEmail(email, password) }) {
                Text("Sign In")
            }
            Button(onClick = { authViewModel.resetPassword(email) }) {
                Text("Reset Password")
            }

            Button(onClick = { authViewModel.requestGoogleSignIn() }) {
                Text("Sign in with Google")
            }
        }
    }
}
