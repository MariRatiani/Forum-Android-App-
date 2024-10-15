package com.example.forumapp.profilePage.postOwnerProfilePage

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.forumapp.profilePage.UI.SingleImageScreen
import com.example.forumapp.profilePage.data.repository.UserRepository
import com.example.forumapp.profilePage.postOwnerProfilePage.viewModel.PostOwnerProfileViewModel
import com.example.forumapp.profilePage.postOwnerProfilePage.viewModel.PostOwnerProfileViewModelFactory

@Composable
fun OtherUserProfileScreen(
    userId: String,
    viewModel: PostOwnerProfileViewModel = viewModel(
        factory = PostOwnerProfileViewModelFactory(UserRepository())
    )
) {
    val user by viewModel.user.observeAsState()
    viewModel.getUser(userId)
    val userProfileImage = remember { mutableStateOf<Uri?>(null) }

    user?.let { userProfile ->
        userProfileImage.value = userProfile.image?.let {
            Uri.parse(it)
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {


        user?.image.let {
            SingleImageScreen(userProfileImage)
        }
        Text(user?.username ?: "no user")
    }
}