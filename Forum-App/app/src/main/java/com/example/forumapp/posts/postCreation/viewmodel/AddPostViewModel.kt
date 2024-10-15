package com.example.forumapp.posts.postCreation.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forumapp.posts.data.model.Category
import com.example.forumapp.posts.data.model.Post
import com.example.forumapp.posts.data.model.SubCategory
import com.example.forumapp.posts.data.repository.CategoryRepository
import com.example.forumapp.posts.data.repository.ImageRepository
import com.example.forumapp.posts.data.repository.PostRepository
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AddPostViewModel(
    private val postRepository: PostRepository,
    private val imageRepository: ImageRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> get() = _categories
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    init {
        fetchCategories()
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            _categories.value = categoryRepository.getCategories()
        }
    }

    fun addPost(post: Post) {
        viewModelScope.launch {
            postRepository.addPost(post)
        }
    }

    suspend fun isValidPost(
        title: String?,
        text: String?,
        url: String?,
        category: Category?,
        subCategory: SubCategory?,
        imageUris: List<Uri?>
    ): Pair<Post?, String?> {
        val currentUser = auth.currentUser

        if (currentUser == null) {
            return Pair(null, "User is not authenticated, Please Log In")
        }
        if (title.isNullOrEmpty()) {
            return Pair(null, "Title cannot be empty")
        }

        if (text.isNullOrEmpty()) {
            return Pair(null, "text category cannot be empty")
        }

        if (subCategory == null) {
            return Pair(null, "SubTopic cannot be empty, Please Select One!")
        }

        val imageUrls = coroutineScope {
            imageUris.mapNotNull { uri ->
                uri?.let {
                    async { imageRepository.uploadImageToFirebase(it) }
                }
            }.awaitAll().filterNotNull()
        }

        val post = category?.let {
            Post(
                userId = currentUser.uid,
                postId = "TO DO",
                title = title,
                text = text,
                images = imageUrls,
                link = url.orEmpty(),
                category = category.name,
                subcategory = subCategory.name,
                createdAt = Timestamp.now()
            )
        }

        return Pair(post, null)
    }


    fun validateAndAddPost(
        title: String?,
        text: String?,
        url: String?,
        category: Category?,
        subCategory: SubCategory?,
        imageUris: List<Uri?>,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val (post, errorMessage) = isValidPost(title, text, url, category, subCategory, imageUris)

            if (post != null) {
                addPost(post)
                Log.d("current", "Post added successfully")

                onSuccess()
            } else {
                Log.d("current", "Error: $errorMessage")

                onError(errorMessage ?: "Unknown error")
            }
        }
    }

}
