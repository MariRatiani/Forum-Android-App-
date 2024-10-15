package com.example.forumapp.posts.PostsFeed.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.forumapp.posts.data.model.Category

@Composable
fun CategorySection() {
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 64.dp),
        contentAlignment = Alignment.Center) {
        Text(text = "header", fontSize = 60.sp)
    }

}


@Composable
fun CategoryList(
    categories: List<Category>,
    modifier: Modifier = Modifier,
    itemTextStyle: TextStyle = TextStyle(fontSize = 18.sp),
    onItemClicked: (Category) -> Unit
) {
    LazyColumn(modifier) {
         items(categories) { category ->
             Row(modifier = Modifier
                 .fillMaxWidth()
                 .clickable { onItemClicked(category) }
                 .padding(16.dp)) {
             }
             // In case you wanna add the icon to categories
//             Icon(imageVector = category.icon, contentDescription = null)
            Text(text = category.name,
                style = itemTextStyle,
//                modifier = Modifier.weight(1f)
                )
         }
    }
}