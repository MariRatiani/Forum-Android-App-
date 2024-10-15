package com.example.forumapp.profilePage.UI

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.forumapp.posts.postCreation.ui.components.ImageComponent

@Composable
fun SingleImageScreen(selectedImageUri: MutableState<Uri?>, onRemove: (() -> Unit)? = null) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        selectedImageUri.value?.let { uri ->
            ImageComponent(
                imageUri = uri,
                onRemove = {
                    if (onRemove == null) {
                        selectedImageUri.value = null
                        Toast.makeText(context, "Image removed", Toast.LENGTH_SHORT).show()
                    } else {
                        onRemove()
                    }
                }
            )
        }
    }
}
