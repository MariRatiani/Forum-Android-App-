package com.example.forumapp.navigation

sealed class Screen(val route: String, val title: String) {
    object PostsFeed : Screen("posts_feed", "Feed")
    object SignIn : Screen("sign_in", "Sign In")
    object Profile : Screen("profilePage/{userId}", "Profile")
    object AddPost : Screen("postCreationPage", "Add Post")
    object Search : Screen("SearchScreen", "Search")
    object OtherUserProfile : Screen("otherUserProfilePage/{userId}", "OtherProfile")
}
