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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.thebrianwong.protivity.R

@Composable
fun TimeModal(handleConfirm: (Long) -> Unit, handleDismiss: () -> Unit) {

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
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "HH")
                        TextField(value = "00", onValueChange = {})
                    }
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "MM")
                        TextField(value = "00", onValueChange = {})
                    }
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "SS")
                        TextField(value = "00", onValueChange = {})
                    }
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
