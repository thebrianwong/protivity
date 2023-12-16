package com.thebrianwong.protivity.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
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

    fun calculateInputTime(): Long {
        val inputHours = hours ?: 0
        val inputMinutes = minutes ?: 0
        val inputSeconds = seconds ?: 0
        val hoursToSeconds = inputHours * 3600
        val minutesToSeconds = inputMinutes * 60
        val inputTimeMilliseconds = (hoursToSeconds + minutesToSeconds + inputSeconds) * 1000
        return inputTimeMilliseconds
    }

    AlertDialog(
        onDismissRequest = { handleDismiss() },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true),
        confirmButton = {
            Button(onClick = {
                val userInputTime = calculateInputTime()
                handleConfirm(userInputTime)
                handleDismiss()
            }) {
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
        modifier = Modifier.padding(horizontal = 16.dp).shadow(elevation = 8.dp, shape = RoundedCornerShape(32.dp)),
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
                        finalInput = false,
                        handleValueChange = { handleUserInput(it, "hours", 99) },

                        )
                    TimeModalInput(
                        value = minutes,
                        label = "MM",
                        modifier = Modifier.weight(1f),
                        finalInput = false,
                        handleValueChange = { handleUserInput(it, "minutes", 59) },

                        )
                    TimeModalInput(
                        value = seconds,
                        label = "SS",
                        modifier = Modifier.weight(1f),
                        finalInput = true,
                        handleValueChange = { handleUserInput(it, "seconds", 59) },

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
