package com.example.forumapp.posts.data.repository

import com.example.forumapp.posts.data.model.Category
import com.example.forumapp.posts.data.model.SubCategory
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CategoryRepository {
    private val db = FirebaseFirestore.getInstance()

    suspend fun getCategories(): List<Category> {
        return try {
            val result = db.collection("categories").get().await()
            result.documents.map { document ->
                val category = document.toObject(Category::class.java)!!
                val subcategories = getSubcategories(document.id)
                category.subcategories = subcategories
                category
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun addCategory(category: Category) {
        try {
            db.collection("categories").add(category).await()
        } catch (e: Exception) {
            // TODO: Handle error
        }
    }

    suspend fun getSubcategories(categoryId: String): List<SubCategory> {
        return try {
            val result = db.collection("categories").document(categoryId).collection("subcategories").get().await()
            result.documents.map { document -> document.toObject(SubCategory::class.java)!! }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun addSubcategory(categoryId: String, subcategory: SubCategory) {
        try {
            db.collection("categories").document(categoryId).collection("subcategories").add(subcategory).await()
        } catch (e: Exception) {
            // TODO: Handle error
        }
    }
}
