package com.thebrianwong.protivity.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TimeIncrementButtons(handleClick: (Long) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Button(modifier = Modifier.weight(1f), onClick = { handleClick(5) }) {
            Text(text = "+5s")
        }
        Button(modifier = Modifier.weight(1f), onClick = { handleClick(10) }) {
            Text(text = "+10s")
        }
        Button(modifier = Modifier.weight(1f), onClick = { handleClick(15) }) {
            Text(text = "+15s")
        }
    }
}
