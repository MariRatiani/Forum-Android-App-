package com.example.forumapp.posts.postCreation.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.forumapp.posts.data.model.Category
import com.example.forumapp.posts.data.model.SubCategory

@Composable
fun SelectableList(
    categories: List<Category>,
    selectedCategory: MutableState<Category?>,
    selectedSubCategory: MutableState<SubCategory?>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { category ->
            item {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedCategory.value = category
                            selectedSubCategory.value = null
                        }
                        .background(
                            if (selectedCategory.value == category) MaterialTheme.colorScheme.primary.copy(
                                alpha = 0.1f
                            )
                            else Color.Transparent
                        )
                        .padding(8.dp)
                )
            }
            item {
                SubCategoriesList(
                    subcategories = category.subcategories,
                    currentCategory = category,
                    selectedCategory = selectedCategory,
                    selectedSubCategory =  selectedSubCategory
                )
            }
        }
    }
}

@Composable
fun SubCategoriesList(
    subcategories: List<SubCategory>,
    currentCategory: Category,
    selectedCategory: MutableState<Category?>,
    selectedSubCategory: MutableState<SubCategory?>
) {
    AnimatedVisibility(
        visible = selectedCategory.value == currentCategory,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
    ) {
        Column {
            subcategories.forEach { subcategory ->
                Text(
                    text = subcategory.name,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedSubCategory.value = subcategory
                        }
                        .background(
                            if (selectedSubCategory.value == subcategory) MaterialTheme.colorScheme.primary.copy(
                                alpha = 0.1f
                            )
                            else Color.Transparent
                        )
                        .padding(start = 16.dp, top = 4.dp, bottom = 4.dp)
                )
            }
        }
    }
}
