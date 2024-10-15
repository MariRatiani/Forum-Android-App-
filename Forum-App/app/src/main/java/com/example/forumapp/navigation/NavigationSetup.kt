package com.example.forumapp.navigation

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.forumapp.Authentication.UI.SignInScreen
import com.example.forumapp.Authentication.data.GoogleAuthUIClient
import com.example.forumapp.posts.PostsFeed.ui.PostsFeedScreen
//import com.example.forumapp.posts.PostsFeed.ui.PostsFeedScreen
import com.example.forumapp.posts.postCreation.ui.screens.AddPostScreen
import com.example.forumapp.profilePage.UI.ProfileScreen
import com.example.forumapp.posts.SearchPosts.ui.SearchScreen
import com.example.forumapp.profilePage.postOwnerProfilePage.OtherUserProfileScreen
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope

data class BottomNavigationItem(
    val title: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationSetup(
    googleAuthUIClient: GoogleAuthUIClient,
    lifecycleScope: CoroutineScope,
    context: Context
) {
    val navController = rememberNavController()
    NavControllerHolder.navController = navController

    val items = listOf(
        //homePage
        BottomNavigationItem(
            title = Screen.PostsFeed.title,
            route = Screen.PostsFeed.route,
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home
        ),
        //Search Page
        BottomNavigationItem(
            title = Screen.Search.title,
            route = Screen.Search.route,
            selectedIcon = Icons.Filled.Search,
            unselectedIcon = Icons.Outlined.Search
        ),
        //Add post Page
        BottomNavigationItem(
            title = Screen.AddPost.title,
            route = Screen.AddPost.route,
            selectedIcon = Icons.Filled.Create,
            unselectedIcon = Icons.Outlined.Create
        ),

        // Profile Page
        BottomNavigationItem(
            title = Screen.Profile.title,
            route = Screen.Profile.route,
            selectedIcon = Icons.Filled.AccountCircle,
            unselectedIcon = Icons.Outlined.AccountCircle
        )
    )

    var selectedItemIndex by rememberSaveable { mutableStateOf(0) }
    var isUserSignedIn by remember { mutableStateOf(false) }

    // Observe FirebaseAuth state
    DisposableEffect(Unit) {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            isUserSignedIn = auth.currentUser != null
        }
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener)
        onDispose {
            FirebaseAuth.getInstance().removeAuthStateListener(authStateListener)
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItemIndex == index,
                        onClick = {
                            selectedItemIndex = index
                            navController.navigate(item.route) {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                            }
                        },
                        icon = {
                            BadgedBox(badge = {}) {
                                Icon(
                                    imageVector = if (selectedItemIndex == index) {
                                        item.selectedIcon
                                    } else item.unselectedIcon,
                                    contentDescription = item.title
                                )
                            }
                        },
                        label = {
                            Text(text = item.title)
                        }
                    )
                }
            }
        }
    ) { innerPadding ->

        val onUserClicked: (String) -> Unit = {userId ->
            navController.navigate(
                Screen.OtherUserProfile.route.replace(
                    "{userId}",
                    userId
                )
            )
        }

        val onSignOutSuccess = {
            navController.navigate(Screen.SignIn.route) {
                popUpTo(Screen.SignIn.route) { inclusive = true }
            }
        }

        NavHost(
            navController = navController,
            startDestination = Screen.PostsFeed.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.OtherUserProfile.route) { backStackEntry ->
                val auth: FirebaseAuth = FirebaseAuth.getInstance()
                val userId = backStackEntry.arguments?.getString("userId")
                if (userId == auth.currentUser?.uid) {
                    ProfileScreen(userId = userId ?: "") {
                        onSignOutSuccess()
                    }
                } else OtherUserProfileScreen(userId = userId ?: "")
            }

            composable(Screen.PostsFeed.route) {
                PostsFeedScreen(
                    onUserClicked = onUserClicked
                )
            }
            composable(Screen.SignIn.route) {
                SignInScreen(
                    googleAuthUIClient,
                    lifecycleScope,
                    onSignInSuccess = { userId ->
                        Log.d("tag1", "sign in success navigating to profile")
                        navController.navigate(Screen.Profile.route.replace("{userId}", userId))
                    },
                    context = context
                )
            }
            composable(Screen.Profile.route) { backStackEntry ->
                Log.d("tag1", "isSignedIn in nav: $isUserSignedIn")
                if (isUserSignedIn) {
                    val userId = backStackEntry.arguments?.getString("userId")
                    ProfileScreen(
                        userId ?: "-1",
                        onSignOutSuccess = onSignOutSuccess
                    )
                } else {
                    Log.d("tag1", "isSignedIn in nav: $isUserSignedIn navigating to SignIn route")
                    navController.navigate(Screen.SignIn.route) {
                        popUpTo(Screen.SignIn.route) { inclusive = true }
                    }
                }
            }
            composable(Screen.AddPost.route) {
                AddPostScreen()
            }
            composable(Screen.Search.route) {
                SearchScreen(onUserClicked = onUserClicked)
            }
        }
    }
}
