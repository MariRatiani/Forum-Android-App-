package com.example.forumapp.profilePage.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.forumapp.profilePage.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository: IUserRepository {
    private val db = FirebaseFirestore.getInstance()
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val userCache = MutableLiveData<User?>()

    override fun getLoggedInUser(): MutableLiveData<User?> {
        val currentUser = auth.currentUser

        if (currentUser != null && userCache.value == null) {
            val userId = currentUser.uid
            db.collection("users").document(userId).get().addOnSuccessListener { document ->
                Log.d("tag1", "repo getLoggedInUser: ${document}")
                val user = document.toObject(User::class.java)
                user?.userId = userId
                userCache.postValue(user)
                Log.d("tag1", "repo getLoggedInUser: $user")

            }.addOnFailureListener { exception ->
                Log.d("errorM", "Error during getLoggedInUser $userId error: $exception")
            }
        }
        return userCache
    }

    override suspend fun getUser(userId: String): User? {
        return if (userCache.value?.userId == userId)
            userCache.value
        else getUserFromDb(userId)
    }


    override suspend fun setUserName(newName: String) {
        try {
            val userId = auth.currentUser?.uid
            if (userId != null) {
                db.collection("users").document(userId).update("username", newName).await()

                val updatedUser = userCache.value?.copy(username = newName)
                userCache.value = updatedUser
            }
        } catch (exception: Exception) {
            Log.d("errorM", "Error during setUserName, error: $exception")
        }
    }

    override suspend fun setUserPhoto(newPhotoUrl: String) {
        try {
            val userId = auth.currentUser?.uid
            if (userId != null) {
                db.collection("users").document(userId).update("image", newPhotoUrl).await()

                val updatedUser = userCache.value?.copy(image = newPhotoUrl)
                userCache.value = updatedUser
            }
        } catch (exception: Exception) {
            Log.d("errorM", "Error during setUserPhoto, error: $exception")
        }
    }

    override suspend fun updatePassword(newPassword: String) {
        val currentUser = auth.currentUser
        currentUser?.updatePassword(newPassword)?.await()
    }

    override suspend fun signOut() {
        auth.signOut()
        userCache.value = null
    }

    override suspend fun deleteUserAccount() {
        try {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                // First delete the user from Firestore
                db.collection("users").document(currentUser.uid).delete().await()
                // Then delete the user from Firebase Authentication
                currentUser.delete().await()
                userCache.value = null
            } else {
                Log.d("errorM", "Error: No current user or userId mismatch.")
            }
        } catch (exception: Exception) {
            Log.d("errorM", "Error during deleteUserAccount, error: $exception")
        }
    }

    override suspend fun updateImage(
        url: String,
        onSuccess: (() -> Unit)?,
        onError: (String) -> Unit
    ) {
        try {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                val userRef = db.collection("users").document(currentUser.uid)
                userRef.update("image", url).await()
                Log.d("updateImage", "Image URL updated successfully.")
            } else {
                Log.d("errorM", "Error: No current user or userId mismatch.")
            }
        } catch (exception: Exception) {
            Log.d("errorM", "Error during updateImage, error: $exception")
        }
    }

    override suspend fun deleteImage(userId: String) {
        try {
            val userRef = db.collection("users").document(userId)
            userRef.update("image", FieldValue.delete()).await()
            val updatedUser = userCache.value?.copy(image = null)
            userCache.value = updatedUser
            Log.d("deleteImage", "Image field deleted successfully.")
        } catch (exception: Exception) {
            Log.d("errorM", "Error during deleteImage, error: $exception")
        }
    }

    //this works on correct UserId
    private suspend fun getUserFromDb(userId: String): User? {
        return try {
            val document = db.collection("users").document(userId).get().await()
            val data = document.data
            Log.d("tag1", "Firestore document data: $data")

            if (data != null) {
                val user = document.toObject(User::class.java)?.copy(userId = document.id)
                Log.d("tag1", "Mapped user object: $user")
                user?.userId = userId
                user?.let {
                    userCache.value = it
                }

                user
            } else {
                Log.d("tag1", "No data found in Firestore document")
                null
            }
        } catch (exception: Exception) {
            Log.d("tag1", "Error during getUserFromDb $userId error: $exception")
            null
        }
    }

    override suspend fun searchUsersByUsername(query: String): List<User> {
        return try {
            val result = db.collection("users")
                .whereGreaterThanOrEqualTo("username", query)
                .whereLessThanOrEqualTo("username", query + "\uf8ff")
                .get()
                .await()

            result.documents.map { document ->
                document.toObject(User::class.java)!!.copy(userId = document.id)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

//    private suspend fun getUserFromDb(userId: String): User? {
//        return try {
//
//            val document = db.collection("users").document(userId).get().await()
//            val returnv = document.toObject(User::class.java).also { userCache.value = it }
//            Log.d("tag1", "in getUserFromDb: return: ${returnv}, userId: $userId")
//            returnv
//        } catch (exception: Exception) {
//            Log.d("tag1", "Error during getUserFromDb $userId error: $exception")
//
//            null
//        }
//    }
}