package com.example.forumapp.profilePage.data.model

data class User(
    var userId: String = "",
    val username: String? = null,
    val image: String? = null,
    val posts: List<String> = emptyList()
) {
    constructor() : this("", "", "", emptyList())

}
