package com.thebrianwong.protivity.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel

class ModalViewModel : ViewModel() {
    private val _hours = mutableStateOf(TextFieldValue(""))
    private val _minutes = mutableStateOf(TextFieldValue(""))
    private val _seconds = mutableStateOf(TextFieldValue(""))

    val hours = _hours
    val minutes = _minutes
    val seconds = _seconds

    private fun resetInputValues() {
        _hours.value = _hours.value.copy(
            text = "",
            selection = TextRange(0, 0)
        )
        _minutes.value = _minutes.value.copy(
            text = "",
            selection = TextRange(0, 0)
        )
        _seconds.value = _seconds.value.copy(
            text = "",
            selection = TextRange(0, 0)
        )
    }

    fun handleOpenModal() {
        resetInputValues()
    }

    fun handleUserInput(newValue: String, endIndex: Int, valueType: String, maxValue: Long) {
        val newValueLong = newValue.toLongOrNull()
        if (newValue == "" || (newValueLong != null && newValueLong <= maxValue)) {
            when (valueType) {
                "hours" ->
                    _hours.value = _hours.value.copy(
                        text = newValue,
                        selection = TextRange(endIndex, endIndex)
                    )

                "minutes" ->
                    _minutes.value = _minutes.value.copy(
                        text = newValue,
                        selection = TextRange(endIndex, endIndex)
                    )

                "seconds" ->
                    _seconds.value = _seconds.value.copy(
                        text = newValue,
                        selection = TextRange(endIndex, endIndex)
                    )
            }
        }
    }

    fun highlightInputValue(valueType: String) {
        when (valueType) {
            "hours" ->
                _hours.value = _hours.value.copy(
                    selection = TextRange(0, _hours.value.text.length)
                )

            "minutes" ->
                _minutes.value = _minutes.value.copy(
                    selection = TextRange(0, _minutes.value.text.length)
                )

            "seconds" ->
                _seconds.value = _seconds.value.copy(
                    selection = TextRange(0, _seconds.value.text.length)
                )
        }
    }

    fun calculateInputTime(): Long {
        val inputHours = _hours.value.text.toLongOrNull() ?: 0
        val inputMinutes = _minutes.value.text.toLongOrNull() ?: 0
        val inputSeconds = _seconds.value.text.toLongOrNull() ?: 0
        val hoursToSeconds = inputHours * 3600
        val minutesToSeconds = inputMinutes * 60
        val inputTimeMilliseconds = (hoursToSeconds + minutesToSeconds + inputSeconds) * 1000
        return inputTimeMilliseconds
    }
}
