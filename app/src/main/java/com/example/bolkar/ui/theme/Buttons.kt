package com.faruk_tutkun.bolkar.ui.theme



import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun Button_v1(
    text: String = "click me",
    func: () -> Unit = {},
    x: Int = 0,
    y: Int = 0,
    layoutHeight: Int = 150,
    width: Int = 100,
    height: Int = 60,
    padding: Int = 20,
    fontSize: Int = 15,
) {
    Column(
        Modifier
            .height(layoutHeight.dp)
            .offset(x = x.dp, y = y.dp)
            .absolutePadding(padding.dp, padding.dp, padding.dp, padding.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = func,
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier
                .height(height.dp)
                .width(width.dp)
        ) {
            Text(
                maxLines = 2,
                fontSize = fontSize.sp,
                color = MaterialTheme.colorScheme.onPrimary,
                text = text,
                modifier = Modifier
                    .defaultMinSize(minHeight = 20.dp)
                    .wrapContentSize()
                    .padding(horizontal = 6.dp, vertical = 2.dp),
                style = MaterialTheme.typography.displayMedium
            )
        }
    }
}

@Composable
fun Button_v2(
    text: String = "click me",
    func: () -> Unit = {},
    width: Int = 100,
    height: Int = 60,
    fontSize: Int = 15,
    padding:  Int  = 12,
) {
    Button(
            onClick = func,
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier
                .height((height +2*padding).dp)
                .width((width +2*padding).dp).absolutePadding(padding.dp, padding.dp, padding.dp, padding.dp)
        ) {
            Text(
                maxLines = 2,
                fontSize = fontSize.sp,
                color = MaterialTheme.colorScheme.onPrimary,
                text = text,
                modifier = Modifier
                    .defaultMinSize(minHeight = 20.dp)
                    .wrapContentSize()
                    .padding(horizontal = 6.dp, vertical = 2.dp),
                style = MaterialTheme.typography.displayMedium
            )
        }
}
@Composable
fun Button_v3(
    text: String = "click me",
    func: () -> Unit = {},
    width: Int = 100,
    height: Int = 60,
    fontSize: Int = 15,
    padding:  Int  = 12,
) {
    Button(
        onClick = func,
        shape = RectangleShape,
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier
            .height((height +2*padding).dp)
            .width((width +2*padding).dp).absolutePadding(padding.dp, padding.dp, padding.dp, padding.dp)
    ) {
        Text(
            maxLines = 2,
            fontSize = fontSize.sp,
            color = MaterialTheme.colorScheme.onPrimary,
            text = text,
            modifier = Modifier
                .defaultMinSize(minHeight = 20.dp)
                .wrapContentSize()
                .padding(horizontal = 6.dp, vertical = 2.dp),
            style = MaterialTheme.typography.displayMedium
        )
    }
}

@Composable
fun RadioGroup(
    mItems: List<String>,
    selected: String,
    setSelected: (selected: String) -> Unit,
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            mItems.forEach { item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selected == item,
                        onClick = {
                            setSelected(item)
                        },
                        enabled = true
                    )
                    Text(text = item, modifier = Modifier.padding(start = 8.dp))
                }
            }
        }
    }
}