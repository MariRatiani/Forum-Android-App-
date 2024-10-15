package com.example.forumapp.posts.singlePost.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.SubcomposeAsyncImage
import com.example.forumapp.R
import com.example.forumapp.posts.data.model.Post
import com.example.forumapp.posts.data.model.Reply
import com.example.forumapp.posts.data.repository.OtherUserRepository
import com.example.forumapp.posts.data.repository.PostRepository
import com.example.forumapp.posts.postCreation.ui.theme.ForumAppTheme
import com.example.forumapp.posts.singlePost.ui.helperComponents.CommentsSection
import com.example.forumapp.posts.singlePost.ui.helperComponents.PostLinkSection
import com.example.forumapp.posts.singlePost.ui.helperComponents.PostTopicAndCommentButton
import com.example.forumapp.posts.singlePost.ui.helperComponents.UserInfoSection
import com.example.forumapp.posts.singlePost.viewModel.CommentsViewModel
import com.example.forumapp.posts.singlePost.viewModel.CommentsViewModelFactory
import com.example.forumapp.posts.singlePost.viewModel.PostOwnerViewModel
import com.example.forumapp.posts.singlePost.viewModel.PostOwnerViewModelFactory
import com.example.forumapp.posts.singlePost.viewModel.UserViewModel
import com.example.forumapp.posts.singlePost.viewModel.UserViewModelFactory
import com.example.forumapp.profilePage.data.repository.UserRepository
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth


@Composable
fun SinglePostComponent(
    post: Post, onUserClicked: (String) -> Unit
) {
    val context = LocalContext.current
    var showComments by remember { mutableStateOf(false) }
    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    val commentsViewModel: CommentsViewModel = viewModel(
        key = post.postId,
        factory = CommentsViewModelFactory(PostRepository(), post.postId ?: "-1")
    )

    val userViewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(UserRepository())
    )
    val user by userViewModel.user.collectAsState()
    val replies by commentsViewModel.comments.collectAsState()

    LaunchedEffect(post) {
        post.userId?.let {
            commentsViewModel.fetchComments(it)
            Log.d("comment_debug", "commentsViewModel.fetchComments(it) done + ${replies.size}")
        }
        userViewModel.fetchLoggedInUser()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(color = colorResource(id = R.color.background_color))
            .padding(16.dp) // Inner padding for the content
    ) {
        Column {
            UserInfoSection(post, onUserClicked)

            Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(bottom = 8.dp))

            // Post Name
            Text(
                fontFamily = FontFamily.SansSerif,
                text = post.title ?: "No Title",
                style = MaterialTheme.typography.titleLarge,
                color = Color.DarkGray,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Post text
            Text(
                text = post.text ?: "",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            post.images?.firstOrNull()?.let { imageUrl ->
                SubcomposeAsyncImage(
                    model = post.images[0],
                    contentDescription = "Post Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .padding(bottom = 8.dp)
                )
            }

            PostLinkSection(post, context)

            PostTopicAndCommentButton(post) {
                showComments = !showComments
                if (showComments) {
                    post.postId?.let { commentsViewModel.fetchComments(it) }
                }
            }

            if (showComments) {
                CommentsSection(replies) { newComment ->
                    if (auth.currentUser == null) {
                        Toast.makeText(context, "can't comment without sign in", Toast.LENGTH_LONG)
                            .show()
                    } else {
                        commentsViewModel.addComment(
                            post.postId,
                            Reply(
                                userId = FirebaseAuth.getInstance().currentUser?.uid,
                                user?.username ?: "NO NAME",
                                newComment,
                                createdAt = Timestamp.now()
                            )
                        )
                    }
                }
            }
        }
    }
}

//
//@Preview(showBackground = true)
//@Composable
//fun PostComponentPreview() {
//    ForumAppTheme {
//        val photoRef = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT2ZC4cuL4d49q6nmXhU4_B7G9mk2bXcpofKA&s"
//
//        val testPost = Post(
//            "",
//            "Mariko Magaria",
//            "martla martla",
//            images = listOf(photoRef),
//            link = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQdfi9j1XHxKY0dZkSfzlhlzGjlwJt_nXAoBg&s",
//            category = "topic 1",
//            subcategory = "subTopic 1",
//        )
//
//        SinglePostComponent(post = testPost, onUserClicked = onU)
//    }
//}
//
//

