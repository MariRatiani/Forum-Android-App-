package com.example.forumapp.posts.postCreation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.forumapp.posts.PostsFeed.viewModel.PostsFeedViewModel
import com.example.forumapp.posts.data.repository.CategoryRepository
import com.example.forumapp.posts.data.repository.ImageRepository
import com.example.forumapp.posts.data.repository.PostRepository

class ViewModelFactory(
    private val postRepository: PostRepository,
    private val imageRepository: ImageRepository? = null,
    private val categoryRepository: CategoryRepository? = null
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostsFeedViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PostsFeedViewModel(postRepository) as T
        } else if (modelClass.isAssignableFrom(AddPostViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            if (categoryRepository != null && imageRepository != null) {
                return AddPostViewModel(postRepository, imageRepository, categoryRepository) as T
            }
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
