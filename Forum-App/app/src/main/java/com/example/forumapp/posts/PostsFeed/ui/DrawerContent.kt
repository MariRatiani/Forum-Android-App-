package com.example.forumapp.posts.PostsFeed.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.forumapp.posts.data.model.Category
import com.example.forumapp.posts.data.model.SubCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerContent(
    categories: List<Category>,
    selectedItemIndex: Int,
    onSubCategoryClicked: (SubCategory) -> Unit,
    onCloseDrawer: () -> Unit
) {
    var expandedIndex by remember { mutableStateOf(-1) }

    ModalDrawerSheet {
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(categories) { category ->
                Column {
                    val thisIsExplanded = (expandedIndex == categories.indexOf(category))
                    NavigationDrawerItem(
                        label = { Text(text = category.name) },
                        selected = categories.indexOf(category) == selectedItemIndex,
                        onClick = {
                            if (expandedIndex == categories.indexOf(category)) {
                                expandedIndex = -1
                            } else {
                                expandedIndex = categories.indexOf(category)
                            }
                        },

                        icon = { if(thisIsExplanded) Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = null)
                        else Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)},

                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                    if (expandedIndex == categories.indexOf(category)) {
                        category.subcategories.forEach { subcategory ->
                            NavigationDrawerItem(
                                label = { Text(text = subcategory.name) },
                                selected = false,
                                onClick = {
                                    onSubCategoryClicked(subcategory)
                                    onCloseDrawer()
                                },
                                modifier = Modifier.padding(
                                    start = 32.dp,
//                                    end = NavigationDrawerItemDefaults.ItemPadding.end,
//                                    top = NavigationDrawerItemDefaults.ItemPadding.top,
//                                    bottom = NavigationDrawerItemDefaults.ItemPadding.bottom
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
