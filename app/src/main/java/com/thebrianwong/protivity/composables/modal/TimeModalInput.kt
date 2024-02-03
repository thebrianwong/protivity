package com.thebrianwong.protivity.composables.modal

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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun TimeModalInput(
    value: TextFieldValue,
    label: String,
    modifier: Modifier,
    finalInput: Boolean,
    hasFocus: Boolean,
    handleValueChange: (String, Int) -> Unit,
    handleFocusChange: (String) -> Unit,
    highlightInputValue: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    var initialComposition by remember { mutableStateOf(true) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = label)
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
                        // tap into input
                        if (state.isFocused) {
                            handleFocusChange(label)
                            highlightInputValue()
                        }
                    } else {
                        initialComposition = false
                    }
                },
            value = value,
            placeholder = { Text(text = "00") },
            onValueChange = {
                handleValueChange(it.text, it.text.length)
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = if (finalInput) ImeAction.Done else ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Right)
                },
                onDone = {
                    focusManager.clearFocus()
                    handleFocusChange("")
                }
            )
        )
    }
}
