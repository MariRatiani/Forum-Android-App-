package com.example.forumapp.posts.PostsFeed.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forumapp.posts.data.model.Post
import com.example.forumapp.posts.data.model.SubCategory
import com.example.forumapp.posts.data.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PostsFeedViewModel(private val repository: PostRepository) : ViewModel() {
    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> get() = _posts.asStateFlow()

    init {
        fetchAllPosts()
    }

    fun fetchAllPosts() {
        viewModelScope.launch {
            _posts.value = repository.getAllExistingPosts()
        }
    }

    fun getPostsBySubCategory(subCategory: SubCategory) {
        viewModelScope.launch {
            _posts.value = repository.getPostsBySubCategory(subCategory)
        }
    }

}