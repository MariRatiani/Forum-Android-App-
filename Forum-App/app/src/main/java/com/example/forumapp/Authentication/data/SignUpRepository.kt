package com.example.forumapp.Authentication.data

import android.util.Log
import com.example.forumapp.profilePage.data.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class SignUpRepository: ISignUpRepository {
    private val db = FirebaseFirestore.getInstance()

    override suspend fun addUser(user: User): String? {
        return try {
            db.collection("users").document(user.userId).set(user).await()
            user.userId
        }catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}