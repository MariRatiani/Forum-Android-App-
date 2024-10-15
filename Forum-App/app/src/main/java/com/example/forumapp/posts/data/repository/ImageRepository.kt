package com.example.forumapp.posts.data.repository

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await

class ImageRepository {
    private val storage = Firebase.storage("gs://forum-app-db0ef.appspot.com")
    private val storageRef = storage.reference
    private val imagesRef = storageRef.child("images")

    suspend fun uploadImageToFirebase(imageUri: Uri): String? {
        val fileName = imageUri.lastPathSegment ?: return null
        val fileRef = imagesRef.child(fileName)
        return try {
            fileRef.putFile(imageUri).await()
            fileRef.downloadUrl.await().toString()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun deleteImageFromFirebase(imageUri: Uri?): Boolean {
        val fileName = imageUri?.lastPathSegment ?: return false
        val fileRef = imagesRef.child(fileName)
        return try {
            fileRef.delete().await()
            true
        } catch (e: Exception) {
            false
        }
    }
}
