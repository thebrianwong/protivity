package com.thebrianwong.protivity.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.thebrianwong.protivity.R

@Composable
fun TimeModal(handleConfirm: (Long) -> Unit, handleDismiss: () -> Unit) {
    var hours by remember { mutableStateOf<Long?>(null) }
    var minutes by remember { mutableStateOf<Long?>(null) }
    var seconds by remember { mutableStateOf<Long?>(null) }

    fun handleUserInput(newValue: String, valueType: String, maxValue: Long) {
        val newValueLong = newValue.toLongOrNull()
        if ((newValueLong == null) || (newValueLong <= maxValue)) {
            when (valueType) {
                "hours" -> hours = newValueLong
                "minutes" -> minutes = newValueLong
                "seconds" -> seconds = newValueLong
            }
        }
    }

    AlertDialog(
        onDismissRequest = { handleDismiss() },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true),
        confirmButton = {
            Button(onClick = { handleConfirm(0) }) {
                Text(text = "Create")
            }
        },
        dismissButton = {
            Button(onClick = { handleDismiss() }) {
                Text(text = "Cancel")
            }
        },
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.baseline_timer_24),
                contentDescription = "New Timer"
            )
        },
        title = { Text(text = "New Timer") },
        text = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TimeModalInput(
                        value = hours,
                        label = "HH",
                        modifier = Modifier.weight(1f),
                        handleValueChange = { handleUserInput(it, "hours", 99) }
                    )
                    TimeModalInput(
                        value = minutes,
                        label = "MM",
                        modifier = Modifier.weight(1f),
                        handleValueChange = { handleUserInput(it, "minutes", 59) }
                    )
                    TimeModalInput(
                        value = seconds,
                        label = "SS",
                        modifier = Modifier.weight(1f),
                        handleValueChange = { handleUserInput(it, "seconds", 59) }
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewTimeModal() {
    TimeModal(handleConfirm = {}, handleDismiss = {})
}
