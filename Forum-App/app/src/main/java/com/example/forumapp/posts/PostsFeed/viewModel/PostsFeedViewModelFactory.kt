package com.example.forumapp.posts.PostsFeed.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.forumapp.posts.data.repository.PostRepository

class PostsFeedViewModelFactory(
    private val postRepository: PostRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostsFeedViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PostsFeedViewModel(postRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
