package com.example.forumapp.posts.postCreation.ui.components

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import com.example.forumapp.posts.data.model.Category
import com.example.forumapp.posts.data.model.SubCategory

@Composable
fun AddPostContent(
    modifier: Modifier = Modifier,
    padding: PaddingValues,
    titleState: MutableState<String>,
    textState: MutableState<String>,
    linkState: MutableState<String>,
    shoulShowLinkInput: MutableState<Boolean>,
    shouldShowCategories: MutableState<Boolean>,
    showSuccess: MutableState<Boolean>,
    showError: MutableState<Boolean>,
    errorText: MutableState<String>,
    categories: List<Category>,
    selectedCategory: MutableState<Category?>,
    selectedSubcategory: MutableState<SubCategory?>,
    selectedImageUris: MutableState<List<Uri?>>,
    focusRequester: FocusRequester,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SuccessWindow(showSuccess = showSuccess, successText = "Post added successfully!")
        ErrorWindow(showError = showError, errorText = errorText.value)

        FocusedPostTextField(
            titleState,
            MaterialTheme.typography.headlineSmall,
            "Title",
            focusRequester
        )
        PostTextField(
            textState = textState,
            textStyle = MaterialTheme.typography.bodyLarge,
            placeholder = "Post Text"
        )

        if (shoulShowLinkInput.value) {
            PostTextField(
                textState = linkState,
                textStyle = MaterialTheme.typography.bodyLarge,
                placeholder = "URL"
            )
        }

        if (shouldShowCategories.value) {
            SelectableList(
                categories = categories,
                selectedCategory = selectedCategory,
                selectedSubCategory = selectedSubcategory
            )
        }
        Images(selectedImageUris)
        Spacer(modifier = Modifier.weight(1f))
    }
}