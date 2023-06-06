package com.faruk_tutkun.bolkar.ui.theme


import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun toolbar_v1(
    func: () -> Unit = {},
    func2: () -> Unit = {},
    hasBackArrow: Boolean = true,
    hasMenu: Boolean = true,
    items: List<menuItem> = emptyList()
) {
    var expanded by remember { mutableStateOf(false) }
    Column {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.onPrimary,
                    text = "BOLKAR",
                )
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
            navigationIcon = if (hasBackArrow) (
                    {
                        IconButton(onClick = func) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Go Back",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    })
            else ({}),
            actions =
            if (hasMenu) ({
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Menu",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    for (item in items) {
                        DropdownMenuItem(
                            text = { Text(item.text) },
                            onClick = item.onClick
                        )
                    }
                }
            })
            else ({})
        )
    }
}

