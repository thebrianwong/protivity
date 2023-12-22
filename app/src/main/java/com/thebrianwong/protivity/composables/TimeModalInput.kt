package com.thebrianwong.protivity.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun TimeModalInput(
    value: Long?,
    label: String,
    modifier: Modifier,
    finalInput: Boolean,
    hasFocus: Boolean,
    handleValueChange: (String) -> Unit,
    handleFocusChange: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    var initialComposition by remember { mutableStateOf(true) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        fun handleEmptyInput() {
            if (value?.toString() == null) {
                handleValueChange("0")
            }
        }

        TextField(
            modifier = Modifier
                .width(64.dp)
                .focusRequester(focusRequester)
                .onGloballyPositioned {
                    if (hasFocus) {
                        focusRequester.requestFocus()
                    }
                }
                .onFocusChanged { state ->
                    if (!initialComposition) {
                        // tap out of input
                        if (!state.isFocused) {
                            handleEmptyInput()
                        // tap into input
                        } else {
                            handleFocusChange(label)
                        }
                    } else {
                        initialComposition = false
                    }
                },
            value = value?.toString() ?: "",
            placeholder = { Text(text = "0") },
            onValueChange = { handleValueChange(it) },
            singleLine = true,
            label = { Text(text = label) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = if (finalInput) ImeAction.Done else ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Right)
                    handleEmptyInput()
                },
                onDone = {
                    focusManager.clearFocus()
                    handleEmptyInput()
                }
            )
        )
    }
}
