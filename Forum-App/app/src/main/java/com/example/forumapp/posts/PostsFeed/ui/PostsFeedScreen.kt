package com.example.forumapp.posts.PostsFeed.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.forumapp.categories.CategoryViewModel
import com.example.forumapp.categories.CategoryViewModelFactory
import com.example.forumapp.posts.PostsFeed.viewModel.PostsFeedViewModel
import com.example.forumapp.posts.PostsFeed.viewModel.PostsFeedViewModelFactory
import com.example.forumapp.posts.data.repository.CategoryRepository
import com.example.forumapp.posts.data.repository.PostRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PostsFeedScreen(
    onUserClicked: (String) -> Unit,
    postsViewModel: PostsFeedViewModel = viewModel(
        factory = PostsFeedViewModelFactory(PostRepository())
    ),
    categoryViewModel: CategoryViewModel = viewModel(
        factory = CategoryViewModelFactory(CategoryRepository())
    )
) {
    val posts by postsViewModel.posts.collectAsState()
    val categories by categoryViewModel.categories.collectAsState()

    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var isRefreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            scope.launch {
                Log.d("refresh", "in onRefresh")
                postsViewModel.fetchAllPosts()
                isRefreshing = false
            }
        }
    )

    ModalNavigationDrawer(
        drawerContent = {
            DrawerContent(
                categories = categories,
                selectedItemIndex = 0,
                onSubCategoryClicked = { subcategory ->
                    postsViewModel.getPostsBySubCategory(subcategory)
                },
                onCloseDrawer = { scope.launch { drawerState.close() } }
            )
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "FORUM", fontSize = 24.sp)
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu")
                        }
                    }
                )
            },
            content = {
                Box(modifier = Modifier.fillMaxSize()
                    .pullRefresh(pullRefreshState)) {
                    PostsFeed(posts, onUserClicked)
                    Spacer(modifier = Modifier.height(16.dp))
                    PullRefreshIndicator(
                        refreshing = isRefreshing,
                        state = pullRefreshState,
                        modifier = Modifier.align(Alignment.TopCenter)
                    )
                }
            }
        )
    }
}
