package com.example.forumapp.posts.postCreation.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun FocusedPostTextField(
    textState: MutableState<String>,
    textStyle: TextStyle,
    placeholder: String,
    focusRequester: FocusRequester
) {
    PostTextField(
        modifier = Modifier.focusRequester(focusRequester), textState, textStyle, placeholder)
}

@Composable
fun PostTextField(
    modifier: Modifier = Modifier,
    textState: MutableState<String>,
    textStyle: TextStyle,
    placeholder: String,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        if (textState.value.isEmpty()) {
            Text(
                text = placeholder,
                style = textStyle,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        BasicTextField(
            value = textState.value,
            onValueChange = { textState.value = it },
            textStyle = textStyle,
            modifier = Modifier.fillMaxWidth()
        )
    }
}