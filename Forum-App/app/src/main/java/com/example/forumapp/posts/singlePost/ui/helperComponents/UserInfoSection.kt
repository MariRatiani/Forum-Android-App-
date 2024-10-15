package com.example.forumapp.posts.singlePost.ui.helperComponents

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.forumapp.navigation.NavControllerHolder
import com.example.forumapp.posts.data.model.Post
import com.example.forumapp.posts.data.repository.OtherUserRepository
import com.example.forumapp.posts.singlePost.viewModel.PostOwnerViewModel
import com.example.forumapp.posts.singlePost.viewModel.PostOwnerViewModelFactory

@Composable
fun UserInfoSection(
    post: Post, onUserClicked: (String) -> Unit
) {
    val postOwnerViewModel: PostOwnerViewModel = viewModel(
        key = "PostOwnerViewModel_${post.postId}",
        factory = PostOwnerViewModelFactory(OtherUserRepository())
    )

    val user by postOwnerViewModel.user.collectAsState()

    LaunchedEffect(post.userId) {
        post.userId?.let { postOwnerViewModel.fetchUserById(it) }
    }

    InfoSection(userImage = user?.image, userId = user?.userId ?: "", userName = user?.username, onUserClicked = onUserClicked)
}

@Composable
fun UserInfoSection(
    userImage: String? = "",
    userId: String,
    userName: String? = "",
    onUserClicked: (String) -> Unit
) {
    InfoSection(userImage = userImage, userId = userId, userName = userName,
        onUserClicked = onUserClicked)
}

@Composable
fun InfoSection(
    modifier: Modifier = Modifier,
    userId: String,
    userImage: String? = "",
    userName: String? = "",
    onUserClicked: (String) -> Unit
) {
    val context = LocalContext.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .pointerInput(Unit) {
            detectTapGestures(onPress = {
                // Navigate to UserInfoScreen here
//                context.startActivity(/* intent to UserInfoScreen */)
            })
        }
            .clickable{
                onUserClicked(userId)
//                userId?.let {
//                    NavControllerHolder.navController?.navigate("OtherUserProfileScreen/$userId")
//                }
            }
            .fillMaxWidth()
    ) {
        Image(
            painter = rememberImagePainter(data = userImage ?: ""),
            contentDescription = "User Photo",
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .padding(end = 8.dp)
        )
        Text(
            text = userName ?: "No Name",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Blue,
            fontFamily = FontFamily.SansSerif,
            modifier = Modifier.padding(start = 12.dp, bottom = 4.dp)
        )
    }
}
