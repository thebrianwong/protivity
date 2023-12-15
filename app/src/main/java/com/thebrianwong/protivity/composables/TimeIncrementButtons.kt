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
        val timeIncrements = listOf<Long>(1, 5, 15)
        timeIncrements.forEach { increment ->
            Button(modifier = Modifier.weight(1f), onClick = { handleClick(increment) }) {
                Text(text = "+${increment}s")
            }
        }
    }
}
