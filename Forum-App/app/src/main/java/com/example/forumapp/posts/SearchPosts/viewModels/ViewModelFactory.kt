package com.example.forumapp.posts.SearchPosts.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.forumapp.posts.data.repository.PostRepository
import com.example.forumapp.profilePage.data.repository.IUserRepository

class ViewModelFactory(
    private val postRepository: PostRepository,
    private val userRepository: IUserRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchPostsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchPostsViewModel(postRepository, userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
