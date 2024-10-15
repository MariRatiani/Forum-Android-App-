package com.example.forumapp.posts.PostsFeed.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.forumapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedHeader(
    onCategoryMenuClick: () -> Unit
) {
    TopAppBar(
        title = {
        Text(text = stringResource(id = R.string.app_name))
        },
//        backgroundColor = MaterialTheme.colorScheme.primary,
//        contentColor = MaterialTheme.colorScheme.onPrimary,
        navigationIcon = {
            IconButton(onClick = onCategoryMenuClick) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "category menu button")
            }
        }
        )
}