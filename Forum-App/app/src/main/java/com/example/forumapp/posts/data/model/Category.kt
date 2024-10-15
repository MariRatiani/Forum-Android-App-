package com.example.forumapp.posts.data.model

data class Category(
    val name: String = "",
    var subcategories: List<SubCategory> = emptyList()
)
