package com.faruk_tutkun.bolkar.ui.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment


data class menuItem(var text:String = "", var onClick:()->Unit = {})
@Composable
fun BolkarDropdownMenu(items:List<menuItem>, expanded: Boolean ) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopEnd)
    ) {
         DropdownMenu(
            expanded = expanded,
            onDismissRequest = {}
        ) {
            for(item in items)
            {
                DropdownMenuItem(
                    text = { Text(item.text) },
                    onClick = item.onClick
                )
            }
        }
    }
}

