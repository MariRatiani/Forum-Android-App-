package com.example.forumapp.Authentication.UI

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import com.example.forumapp.Authentication.model.SignInState

@Composable
fun GoogleSignInComponent(
    state: SignInState,
    onSignInClicked: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = state.signInError) {
        state.signInError?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG)
                .show()
        }
    }

    Box(
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = onSignInClicked) {
            Text("Sign in with Google")
        }
    }
}
