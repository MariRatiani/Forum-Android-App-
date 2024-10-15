package com.example.forumapp.posts.postCreation.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.forumapp.R
import com.example.forumapp.posts.data.model.Category
import com.example.forumapp.posts.data.model.SubCategory
import com.example.forumapp.posts.data.repository.CategoryRepository
import com.example.forumapp.posts.data.repository.ImageRepository
import com.example.forumapp.posts.data.repository.PostRepository
import com.example.forumapp.posts.postCreation.ui.components.AddPostContent
import com.example.forumapp.posts.postCreation.viewmodel.AddPostViewModel
import com.example.forumapp.posts.postCreation.viewmodel.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPostScreen(
    viewModel: AddPostViewModel = viewModel(factory = ViewModelFactory(PostRepository(),
        ImageRepository(),
        CategoryRepository()))
) {
    val textState = remember { mutableStateOf("") }
    val titleState = remember { mutableStateOf("") }
    val linkState = remember { mutableStateOf("") }
    val shouldShowCategories = remember { mutableStateOf(false) }
    val shoulShowLinkInput = remember { mutableStateOf(false) }
    val showSuccess = remember { mutableStateOf(false) }
    val showError = remember { mutableStateOf(false) }
    val errorText = remember { mutableStateOf("") }
    val categories by viewModel.categories.collectAsState(initial = emptyList())

    var selectedCategory = remember { mutableStateOf<Category?>(null) }
    var selectedSubcategory = remember { mutableStateOf<SubCategory?>(null) }

    val focusRequester = remember { FocusRequester() }

    var selectedImageUris = remember { mutableStateOf<List<Uri?>>(emptyList()) }
    val multipleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = {
            selectedImageUris.value = it
        }
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    // MARK: Helper functions
    fun showSuccessWindow() {
        showSuccess.value = true
        showError.value = false
    }

    fun showErrorWindow(errorMessage: String) {
        errorText.value = errorMessage
        showError.value = true
        showSuccess.value = false
    }

    fun resetState() {
        textState.value = ""
        titleState.value = ""
        linkState.value = ""
        selectedCategory.value = null
        selectedSubcategory.value = null
        selectedImageUris.value = emptyList()
    }

    fun onSuccessValidateAndAdd() {
        resetState()
        showSuccessWindow()
    }

    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            TopAppBar(
                title = { Text("") },
                actions = {
                    Button(
                        onClick = {
                            viewModel.validateAndAddPost(
                                titleState.value,
                                textState.value,
                                linkState.value,
                                selectedCategory.value,
                                selectedSubcategory.value,
                                selectedImageUris.value,
                                { onSuccessValidateAndAdd() },
                                onError = { errorMessage ->
                                    showErrorWindow(errorMessage)
                                }
                            )
                        }
                    ) {
                        Text(text = "Post")
                    }
                }
            )
        },
        content = { padding ->
            AddPostContent(
                padding = padding,
                titleState = titleState,
                textState = textState,
                linkState = linkState,
                selectedCategory = selectedCategory,
                selectedSubcategory = selectedSubcategory,
                selectedImageUris = selectedImageUris,
                focusRequester = focusRequester,
                categories = categories,
                shouldShowCategories = shouldShowCategories,
                shoulShowLinkInput = shoulShowLinkInput,
                showSuccess = showSuccess,
                showError = showError,
                errorText = errorText
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.height(56.dp), // Set desired height
                content = {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        IconButton(
                            onClick = {
                                shoulShowLinkInput.value = !shoulShowLinkInput.value
                                linkState.value = ""
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.add_link_24px),
                                contentDescription = "Add link icon"
                            )
                        }

                        IconButton(
                            onClick = { multipleLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )},
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.image_24px),
                                contentDescription = "Add Image icon"
                            )
                        }

                        IconButton(
                            onClick = { shouldShowCategories.value = !shouldShowCategories.value },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.category_24px),
                                contentDescription = "Add category icon"
                            )
                        }
                    }
                }
            )
        }
    )
}


