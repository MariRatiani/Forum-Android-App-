package com.example.forumapp.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.forumapp.posts.data.model.Category
import com.example.forumapp.posts.data.repository.CategoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val categoryRepository: CategoryRepository
): ViewModel() {
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> get() = _categories

    init {
        getCategories()
    }

    fun getCategories() {
        viewModelScope.launch {
            _categories.value = categoryRepository.getCategories()
        }
    }

    fun addCategory(category: Category){
        viewModelScope.launch {
            categoryRepository.addCategory(category)
            getCategories()
        }
    }

}

class CategoryViewModelFactory(
    private val categoryRepository: CategoryRepository? = null
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return categoryRepository?.let { CategoryViewModel(it) } as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
