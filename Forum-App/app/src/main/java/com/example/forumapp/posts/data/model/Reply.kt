package com.example.forumapp.posts.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference

data class Reply(
    val userId: String? = null,
    val authorName: String = "",
    val text: String = "",
    val createdAt: Timestamp? = null
)
