package com.example.forumapp.posts.singlePost.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.forumapp.posts.data.repository.OtherUserRepository
import com.example.forumapp.profilePage.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import com.example.forumapp.posts.data.model.Post
import kotlinx.coroutines.launch

class PostOwnerViewModel(private val otherUserRepository: OtherUserRepository): ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> get() = _user

    fun getOwnerOfThePost(post: Post) {
        viewModelScope.launch {
            val fetchedUser = post.userId?.let { otherUserRepository.getUserById(it)}
            _user.value = fetchedUser
        }
    }
    fun fetchUserById(userId: String) {
        viewModelScope.launch {
            val fetchedUser = otherUserRepository.getUserById(userId)
            _user.value = fetchedUser
        }
    }

}

class PostOwnerViewModelFactory(private val otherUserRepository: OtherUserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostOwnerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PostOwnerViewModel(otherUserRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
