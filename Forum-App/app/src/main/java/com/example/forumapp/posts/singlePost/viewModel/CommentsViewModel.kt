package com.example.forumapp.posts.singlePost.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forumapp.posts.data.model.Reply
import com.example.forumapp.posts.data.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModelProvider

class CommentsViewModel(
    private val postRepository: PostRepository,
) : ViewModel() {
    private val _comments = MutableStateFlow<List<Reply>>(emptyList())
    val comments: StateFlow<List<Reply>> get() = _comments.asStateFlow()

    fun fetchComments(postId: String) {
        viewModelScope.launch {
            _comments.value = postRepository.getReplies(postId)
        }
    }

    fun addComment(postId: String?, reply: Reply) {
        if (postId == null) {
            Log.d("comment_debug", "cant add reply, post id is null")
            return
        }
        viewModelScope.launch {
            postRepository.addReply(postId, reply)
            fetchComments(postId) // Refresh comments after adding a new one
        }
    }
}

class CommentsViewModelFactory(
    private val postRepository: PostRepository,
    private val postId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CommentsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CommentsViewModel(postRepository).apply { fetchComments(postId) } as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}