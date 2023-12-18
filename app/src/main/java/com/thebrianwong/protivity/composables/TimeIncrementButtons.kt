package com.thebrianwong.protivity.composables

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@Composable
fun TimeIncrementButtons(handleClick: (Long) -> Unit) {
    val timeIncrements = listOf<Long>(1, 5, 15)
    val configuration = LocalConfiguration.current

    if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            timeIncrements.forEach { increment ->
                Button(
                    modifier = Modifier.weight(
                        weight = 1f
                    ), onClick = { handleClick(increment) }) {
                    Text(text = "+${increment}s")
                }
            }
        }
    } else {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            timeIncrements.forEach { increment ->
                Button(
                    onClick = { handleClick(increment) }) {
                    Text(text = "+${increment}s")
                }
            }
        }
    }
}
