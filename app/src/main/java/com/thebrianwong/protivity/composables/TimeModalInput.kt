package com.thebrianwong.protivity.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun TimeModalInput(
    value: Long?,
    label: String,
    modifier: Modifier,
    handleValueChange: (String) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = value?.toString() ?: "", 
            onValueChange = { handleValueChange(it) },
            label = { Text(text = label)},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )
    }
}
