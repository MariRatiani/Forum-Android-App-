package com.example.forumapp.posts.SearchPosts.viewModels

import androidx.lifecycle.ViewModel

import androidx.lifecycle.viewModelScope
import com.example.forumapp.posts.data.model.Post
import com.example.forumapp.posts.data.repository.PostRepository
import com.example.forumapp.profilePage.data.model.User
import com.example.forumapp.profilePage.data.repository.IUserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchPostsViewModel(
    private val postRepository: PostRepository,
    private val userRepository: IUserRepository,
) : ViewModel() {

    private val _searchPostResults = MutableStateFlow<List<Post>>(emptyList())
    val searchPostResults: StateFlow<List<Post>> = _searchPostResults
    private val _searchUserResults = MutableStateFlow<List<User>>(emptyList())
    val searchUserResults: StateFlow<List<User>> = _searchUserResults

    fun searchPosts(query: String) {
        viewModelScope.launch {
            val results = postRepository.searchPostsByTitleOrText(query)
            _searchPostResults.value = results
        }
    }

    fun searchUsers(query: String) {
        viewModelScope.launch {
            val results = userRepository.searchUsersByUsername(query)
            _searchUserResults.value = results
        }
    }
}
