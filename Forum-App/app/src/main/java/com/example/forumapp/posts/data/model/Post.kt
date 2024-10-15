package com.example.forumapp.posts.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference

data class Post(
    val postId: String? = null,
    val userId: String? = null,
    val title: String = "",
    val text: String = "",
    val images: List<String> = emptyList(),
    val link: String = "",
    val category: String = "",
    val subcategory: String = "",
    val createdAt: Timestamp? = null,
    var replies: List<Reply> = emptyList()
)
