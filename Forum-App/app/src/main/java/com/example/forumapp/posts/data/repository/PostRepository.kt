package com.example.forumapp.posts.data.repository

import android.util.Log
import com.example.forumapp.posts.data.model.Post
import com.example.forumapp.posts.data.model.Reply
import com.example.forumapp.posts.data.model.SubCategory
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class PostRepository {
    private val db = FirebaseFirestore.getInstance()

    suspend fun getAllExistingPosts(): List<Post> {
        return try {
            val result = db.collection("posts")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get().await()
            val posts = result.documents.map { document ->
                val post = document.toObject(Post::class.java)!!.copy(postId = document.id)
                val replies = getReplies(document.id)
                post.replies = replies
                post
            }
            return posts
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getPostsBySubCategory(subCategory : SubCategory): List<Post> {
        return try {
            val result = db.collection("posts")
                .whereEqualTo("subcategory", subCategory.name)
                .get().await()
            val posts = result.documents.map { document ->
                val post = document.toObject(Post::class.java)!!.copy(postId = document.id)
                val replies = getReplies(document.id)
                post.replies = replies
                post
            }
            return posts
        } catch (e: Exception) {
            Log.e("PostRepository", "Error getting posts by subcategory", e)
            emptyList()
        }
    }

    suspend fun addPost(post: Post): String? {
        if (post.userId == null) {
            // TODO: Handle this
            Log.d("current", "post.userId is null")
            return null
        }
        return try {
            val documentRef = db.collection("posts").add(post).await()
            val postId = documentRef.id
            // Optionally, update the post with the postId if needed
            db.collection("posts").document(postId).update("postId", postId).await()
            postId
        } catch (e: Exception) {
            // TODO: Handle error
            null
        }
    }

    suspend fun getReplies(postId: String): List<Reply> {
        return try {
            val result = db.collection("posts").document(postId).collection("replies")
                .orderBy("createdAt")
                .get()
                .await()

            result.documents.map { document -> document.toObject(Reply::class.java)!! }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun addReply(postId: String, reply: Reply) {
        try {
            Log.d("comment_debug", "what is being added (postRepo): ${reply.authorName}")

            db.collection("posts")
                .document(postId)
                .collection("replies")
                .add(reply)
                .await()

        } catch (e: Exception) {
            Log.d("comment_debug", "exception during adding the reply: $e")
        }
    }

    suspend fun searchPostsByTitleOrText(query: String): List<Post> {
        return try {
            val titleResult = db.collection("posts")
                .whereGreaterThanOrEqualTo("title", query)
                .whereLessThanOrEqualTo("title", query + "\uf8ff")
                .get().await()

            val titlePosts = titleResult.documents.map { document ->
                val post = document.toObject(Post::class.java)!!.copy(postId = document.id)
                val replies = getReplies(document.id)
                post.replies = replies
                post
            }

            // Search by text
            val textResult = db.collection("posts")
                .whereGreaterThanOrEqualTo("text", query)
                .whereLessThanOrEqualTo("text", query + "\uf8ff")
                .get().await()

            val textPosts = textResult.documents.map { document ->
                val post = document.toObject(Post::class.java)!!.copy(postId = document.id)
                val replies = getReplies(document.id)
                post.replies = replies
                post
            }

            (titlePosts + textPosts).distinctBy { it.postId }
        } catch (e: Exception) {
            emptyList()
        }
    }

}
