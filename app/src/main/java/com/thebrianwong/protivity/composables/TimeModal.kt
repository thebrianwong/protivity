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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.thebrianwong.protivity.R
import com.thebrianwong.protivity.viewModels.ModalViewModel

@Composable
fun TimeModal(
    modalViewModel: ModalViewModel,
    newTimer: Boolean,
    handleConfirm: (Long) -> Unit,
    handleDismiss: () -> Unit
) {
    var currentlyFocused by rememberSaveable {
        mutableStateOf("HH")
    }

    AlertDialog(
        onDismissRequest = { handleDismiss() },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true),
        confirmButton = {
            Button(onClick = {
                val userInputTime = modalViewModel.calculateInputTime()
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
                contentDescription = "Clock Timer"
            )
        },
        title = { Text(text = if (newTimer) "New Timer" else "Edit Timer") },
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(32.dp)),
        text = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TimeModalInput(
                        value = modalViewModel.hours.value,
                        label = "HH",
                        modifier = Modifier.weight(1f),
                        finalInput = false,
                        hasFocus = currentlyFocused == "HH",
                        handleValueChange = {value, index -> modalViewModel.handleUserInput(value,index, "hours", 99) },
                        handleFocusChange = { currentlyFocused = it },
                        highlightInputValue = { modalViewModel.highlightInputValue("hours")}
                    )
                    TimeModalInput(
                        value = modalViewModel.minutes.value,
                        label = "MM",
                        modifier = Modifier.weight(1f),
                        finalInput = false,
                        hasFocus = currentlyFocused == "MM",
                        handleValueChange = {value, index ->  modalViewModel.handleUserInput(value,index, "minutes", 59) },
                        handleFocusChange = { currentlyFocused = it },
                        highlightInputValue = { modalViewModel.highlightInputValue("minutes")}
                    )
                    TimeModalInput(
                        value = modalViewModel.seconds.value,
                        label = "SS",
                        modifier = Modifier.weight(1f),
                        finalInput = true,
                        hasFocus = currentlyFocused == "SS",
                        handleValueChange = {value, index ->  modalViewModel.handleUserInput(value,index, "seconds", 59) },
                        handleFocusChange = { currentlyFocused = it },
                        highlightInputValue = { modalViewModel.highlightInputValue("seconds")}
                    )
                }
            }
        }
    )
}
