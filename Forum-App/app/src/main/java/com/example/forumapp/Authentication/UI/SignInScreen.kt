package com.example.forumapp.Authentication.UI

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.forumapp.Authentication.data.GoogleAuthUIClient
import com.example.forumapp.Authentication.data.SignUpRepository
import com.example.forumapp.Authentication.viewModels.GoogleSignInViewModel
import com.example.forumapp.Authentication.viewModels.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun SignInScreen(
    googleAuthUIClient: GoogleAuthUIClient,
    lifecycleScope: CoroutineScope,
    onSignInSuccess: (userId: String) -> Unit,
    context: Context
) {
    val googleViewModel = viewModel<GoogleSignInViewModel>()

    val signUpRepository = remember {
        SignUpRepository()
    }
    val googleSignInState by googleViewModel.state.collectAsStateWithLifecycle()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                lifecycleScope.launch {
                    val signInResult = googleAuthUIClient.signInWithIntent(
                        intent = result.data ?: return@launch
                    )
                    googleViewModel.onSignInResult(signInResult, context)
                }
            }
        }
    )

    LaunchedEffect(key1 = googleSignInState.isSignInSuccessful) {
        if (googleSignInState.isSignInSuccessful) {
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                onSignInSuccess(user.uid)
            }

            Toast.makeText(
                context,
                "Sign in Successful",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        MailAuthComponent(AuthViewModel(context, signUpRepository), onSignInSuccess)
        GoogleSignInComponent(
            state = googleSignInState,
            onSignInClicked = {
                lifecycleScope.launch {
                    val signInIntentSender = googleAuthUIClient.signIn()
                    launcher.launch(
                        IntentSenderRequest.Builder(
                            signInIntentSender ?: return@launch
                        ).build()
                    )
                }
            }
        )
    }
}

