package com.example.forumapp.posts.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.forumapp.profilePage.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class OtherUserRepository {

    private val db = FirebaseFirestore.getInstance()
    val auth: FirebaseAuth = FirebaseAuth.getInstance()


    suspend fun getUserById(userId: String): User? {
        return getUserFromDb(userId)
    }

    private suspend fun getUserFromDb(userId: String): User? {
        return try {
            val document = db.collection("users").document(userId).get().await()
            val data = document.data

            if (data != null) {
                val user = document.toObject(User::class.java)?.copy(userId = document.id)
                user?.userId = userId
                user
            } else {
                null
            }
        } catch (exception: Exception) {
            Log.d("error", "Error during getUserFromDb from OtherUserRepo $userId error: $exception")
            null
        }
    }
}