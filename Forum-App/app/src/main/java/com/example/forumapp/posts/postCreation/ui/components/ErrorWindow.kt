package com.example.forumapp.posts.postCreation.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun ErrorWindow(
    modifier: Modifier = Modifier,
    showError: MutableState<Boolean>,
    errorText: String
) {
    AnimatedVisibility(
        visible = showError.value,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(Color.Red)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = errorText,
                color = Color.Black,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )
        }
        LaunchedEffect(key1 = showError.value) {
            delay(3000)
            showError.value = false
        }
    }
}

