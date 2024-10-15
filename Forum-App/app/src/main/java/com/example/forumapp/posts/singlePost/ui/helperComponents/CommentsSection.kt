package com.example.forumapp.posts.singlePost.ui.helperComponents

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.forumapp.navigation.NavControllerHolder
import com.example.forumapp.navigation.Screen
import com.example.forumapp.posts.data.model.Reply

@Composable
fun CommentsSection(replies: List<Reply>, onAddComment: (String) -> Unit) {
    var newComment by remember { mutableStateOf("") }
    Column {
        Text(
            text = if (replies.isNullOrEmpty()) "No Replies" else "Replies",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        replies.forEach { reply ->
            Log.d("reply user id", "reply user id: ${reply.userId}")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable {
                        NavControllerHolder.navController?.navigate(Screen.OtherUserProfile.route.replace("{userId}", reply.userId?: ""))
                    }
            ) {
                Image(
                    painter = rememberImagePainter(data = "https://149455152.v2.pressablecdn.com/wp-content/uploads/2021/09/I-Am-Batman-1-1.jpg"), // Replace with actual user photo URL
                    contentDescription = "User Photo",
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(30.dp)
                        .padding(end = 8.dp)
                )
                Column {
                    Text(
                        text = reply.authorName,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Blue
                    )
                    Text(
                        text = reply.text,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = newComment,
            onValueChange = { newComment = it },
            label = { Text("reply") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Button(
            onClick = {
                onAddComment(newComment)
                newComment = ""
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Submit")
        }
    }
}
