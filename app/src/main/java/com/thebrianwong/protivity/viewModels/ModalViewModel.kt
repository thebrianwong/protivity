package com.thebrianwong.protivity.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ModalViewModel : ViewModel() {
    private val _hours = mutableStateOf<Long?>(null)
    private val _minutes = mutableStateOf<Long?>(null)
    private val _seconds = mutableStateOf<Long?>(null)

    val hours = _hours
    val minutes = _minutes
    val seconds = _seconds

    private fun resetInputValues() {
        _hours.value = null
        _minutes.value = null
        _seconds.value = null
    }

    fun handleOpenModal() {
        resetInputValues()
    }

    fun handleUserInput(newValue: String, valueType: String, maxValue: Long) {
        val newValueLong = newValue.toLongOrNull()
        if ((newValueLong == null) || (newValueLong <= maxValue)) {
            when (valueType) {
                "hours" -> _hours.value = newValueLong
                "minutes" -> _minutes.value = newValueLong
                "seconds" -> _seconds.value = newValueLong
            }
        }
    }

    fun calculateInputTime(): Long {
        val inputHours = _hours.value ?: 0
        val inputMinutes = _minutes.value ?: 0
        val inputSeconds = _seconds.value ?: 0
        val hoursToSeconds = inputHours * 3600
        val minutesToSeconds = inputMinutes * 60
        val inputTimeMilliseconds = (hoursToSeconds + minutesToSeconds + inputSeconds) * 1000
        return inputTimeMilliseconds
    }
}
