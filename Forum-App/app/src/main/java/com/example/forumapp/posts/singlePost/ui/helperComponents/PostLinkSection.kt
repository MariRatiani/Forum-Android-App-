package com.example.forumapp.posts.singlePost.ui.helperComponents

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.forumapp.posts.data.model.Post


@Composable
fun PostLinkSection(post: Post, context: Context) {
    fun openLink(link: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        context.startActivity(intent)
    }

    post.link?.let { link ->
        val annotatedString = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)) {
                append(link)
            }
            addStringAnnotation(
                tag = "URL",
                annotation = link,
                start = 0,
                end = link.length
            )
        }
        ClickableText(
            text = annotatedString,
            style = MaterialTheme.typography.bodySmall,
            onClick = { offset ->
                annotatedString.getStringAnnotations("URL", offset, offset)
                    .firstOrNull()?.let { annotation ->
                        openLink(annotation.item)
                    }
            },
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}