package com.example.forumapp.posts.SearchPosts.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.forumapp.posts.SearchPosts.viewModels.SearchPostsViewModel
import com.example.forumapp.posts.SearchPosts.viewModels.ViewModelFactory
import com.example.forumapp.posts.data.repository.PostRepository
import com.example.forumapp.posts.singlePost.ui.SinglePostComponent
import com.example.forumapp.posts.singlePost.ui.helperComponents.UserInfoSection
import com.example.forumapp.profilePage.data.repository.UserRepository
import kotlinx.coroutines.delay

@Composable
fun SearchScreen(
    onUserClicked: (String) -> Unit,
    viewModel: SearchPostsViewModel = viewModel(factory = ViewModelFactory(PostRepository(), UserRepository()))
) {
    var searchQuery by remember { mutableStateOf("") }
    val searchPostResults by viewModel.searchPostResults.collectAsState()
    val searchUserResults by viewModel.searchUserResults.collectAsState()
    var selectedTab by remember { mutableStateOf("Posts") }

    val scope = rememberCoroutineScope()

    LaunchedEffect(searchQuery, selectedTab) {
        if (searchQuery.isNotEmpty()) {
            delay(500)
            if (selectedTab == "Posts") {
                viewModel.searchPosts(searchQuery)
            } else {
                viewModel.searchUsers(searchQuery)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        TextField(
            value = searchQuery,
            onValueChange = { newQuery ->
                searchQuery = newQuery
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Search $selectedTab") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(
                onClick = {
                    selectedTab = "Posts"
                    searchQuery = ""
                },
                enabled = selectedTab != "Posts"
            ) {
                Text("Posts")
            }
            Button(
                onClick = {
                    selectedTab = "Users"
                    searchQuery = ""
                },
                enabled = selectedTab != "Users"
            ) {
                Text("Users")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            if (selectedTab == "Posts") {
                items(searchPostResults) { post ->
                    SinglePostComponent(post = post, onUserClicked = onUserClicked)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            } else {
                items(searchUserResults) { user ->
                    UserInfoSection(userName = user.username, userId = user.userId ?: "NO ID",  userImage = user.image,
                        onUserClicked = onUserClicked)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}
