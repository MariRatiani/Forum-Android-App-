package com.example.forumapp.profilePage.UI

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.forumapp.R
import com.example.forumapp.posts.data.repository.ImageRepository
import com.example.forumapp.posts.postCreation.ui.components.ErrorWindow
import com.example.forumapp.posts.postCreation.ui.components.SuccessWindow
import com.example.forumapp.profilePage.data.repository.UserRepository
import com.example.forumapp.profilePage.viewModels.ProfileViewModel
import com.example.forumapp.profilePage.viewModels.ProfileViewModelFactory

@Composable
fun ProfileScreen(userId: String, onSignOutSuccess: () -> Unit) {
    val profileViewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModelFactory(UserRepository(), ImageRepository())
    )
    val user by profileViewModel.user.observeAsState()
    profileViewModel.getLoggedInUser()

    var editableUserName by remember { mutableStateOf("") }
    var isEditing by remember { mutableStateOf(false) }

    val selectedImageUri = remember { mutableStateOf<Uri?>(null) }
    val userProfileImage = remember { mutableStateOf<Uri?>(null) }
    val showSuccess = remember { mutableStateOf(false) }
    val showError = remember { mutableStateOf(false) }
    var successText by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf("") }

    val multipleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            selectedImageUri.value = it
        }
    )

    LaunchedEffect(user) {
        editableUserName = user?.username ?: ""
    }

    fun onSuccessImage() {
        showSuccess.value = true
        showError.value = false
        successText = "Image Uploaded successfully!"
    }

    fun onSuccessImageRemove() {
        showSuccess.value = true
        showError.value = false
        successText = "Image Removed successfully!"
//        userProfileImage.value = null
    }

    fun showErrorWindow(errorMessage: String) {
        errorText = errorMessage
        showError.value = true
        showSuccess.value = false
    }

    fun removeImageFromRepo() {
        profileViewModel.removeImage(userProfileImage.value)
        onSuccessImageRemove()
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        SuccessWindow(showSuccess = showSuccess, successText = successText)
        ErrorWindow(showError = showError, errorText = errorText)

        user?.let { userProfile ->
            userProfileImage.value = userProfile.image?.let {
                Uri.parse(it)
            }

            SingleImageScreen(selectedImageUri = userProfileImage) { removeImageFromRepo() }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isEditing) {
                    TextField(
                        value = editableUserName,
                        onValueChange = { editableUserName = it },
                        label = { Text("User Name") }
                    )
                    IconButton(onClick = {
                        isEditing = false
                        profileViewModel.updateUserName(editableUserName)
                    }) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = "Save")
                    }
                } else {
                    Text(text = editableUserName)
                    IconButton(onClick = { isEditing = true }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Sign out")
                IconButton(onClick = {
                    profileViewModel.signOut()
                    onSignOutSuccess()
                }) {
                    Icon(imageVector = Icons.Default.ExitToApp, contentDescription = "Sign out")
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Select an image")
                IconButton(
                    onClick = {
                        multipleLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.image_24px),
                        contentDescription = "Add Image icon"
                    )
                }
            }

            Button(onClick = {
                profileViewModel.updateImage(
                    selectedImageUri.value,
                    { onSuccessImage() },
                    onError = { errorMessage ->
                        showErrorWindow(errorMessage)
                    })
            }) {
                if (userProfileImage.value != null) {
                    Text(text = "Change Image")
                } else {
                    Text(text = "Upload Image")
                }
            }

            SingleImageScreen(selectedImageUri = selectedImageUri)
        } ?: run {
            Text(text = "Loading...")
        }
    }
}
