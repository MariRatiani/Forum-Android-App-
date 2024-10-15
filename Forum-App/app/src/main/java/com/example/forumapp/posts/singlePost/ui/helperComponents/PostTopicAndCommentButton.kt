package com.example.forumapp.posts.singlePost.ui.helperComponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.forumapp.R
import com.example.forumapp.posts.data.model.Post


@Composable
fun PostTopicAndCommentButton(post: Post, onCommentIconClick: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "${post.category} / ${post.subcategory}",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
        IconButton(onClick = {
            onCommentIconClick()
        }) {
            Icon(painter = painterResource(id = R.drawable.comment_2), contentDescription = "Comment")
        }
    }
}