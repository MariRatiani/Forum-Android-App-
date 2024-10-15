package com.example.forumapp.posts.postCreation.ui.components
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun ImageComponent(imageUri: Uri?, onRemove: (() -> Unit)?) {
    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .size(140.dp)
            .padding(4.dp)
    ) {
        AsyncImage(
            model = imageUri,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
        )
        IconButton(
            onClick = {
                showDialog = true
            },
            modifier = Modifier
                .size(30.dp)
                .align(Alignment.TopEnd)
                .padding(2.dp)
                .clip(CircleShape)
                .background(Color.White)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Remove Image",
                tint = Color.Red
            )
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = {
                    Text(text = "Remove Image")
                },
                text = {
                    Text(text = "Are you sure you want to remove this image?")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showDialog = false
                            onRemove?.invoke()
                        }
                    ) {
                        Text(text = "Confirm")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            showDialog = false
                        }
                    ) {
                        Text(text = "Cancel")
                    }
                }
            )
        }
    }
}
