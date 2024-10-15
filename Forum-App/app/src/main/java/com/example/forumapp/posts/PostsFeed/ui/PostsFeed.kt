package com.example.forumapp.posts.PostsFeed.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.forumapp.posts.data.model.Post
import com.example.forumapp.posts.singlePost.ui.SinglePostComponent
import java.lang.reflect.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp

@Composable
fun PostsFeed(posts: List<Post>, onUserClicked: (String) -> Unit,) {
    LazyColumn {
        items(posts) { post ->
            SinglePostComponent(post = post, onUserClicked)
        }
    }
}
