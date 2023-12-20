package com.thebrianwong.protivity.viewModels

import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.thebrianwong.protivity.classes.ProtivityCountDownTimer

class TimerViewModel : ViewModel() {
    private val _timer = mutableStateOf<ProtivityCountDownTimer?>(null)
    private val _startingTime = mutableLongStateOf(0L)
    private val _remainingTime = mutableLongStateOf(0L)
    private val _maxTime = mutableLongStateOf(_startingTime.longValue)
    private val _isNewCounter = mutableStateOf(true)
    private val _isCounting = mutableStateOf(false)

    val timer = _timer
    val startingTime = _startingTime
    val remainingTime = _remainingTime
    val maxTime = _maxTime
    val isNewCounter = _isNewCounter
    val isCounting = _isCounting

    fun getHours(): Int {
        return (_remainingTime.longValue / 3600 / 1000).toInt()
    }

    fun getMinutes(): Int {
        return ((_remainingTime.longValue / 1000 - (getHours() * 3600)) / 60).toInt()
    }

    fun getSeconds(): Int {
        return (_remainingTime.longValue / 1000 - (getHours() * 3600) - (getMinutes() * 60)).toInt()
    }

    private fun handleTick(newTime: Long) {
        _remainingTime.longValue = newTime
    }

    fun resetTimer() {
        _timer.value =
            ProtivityCountDownTimer(_remainingTime.longValue, { handleTick(it) }, { resetTimer() })
        _remainingTime.longValue = _startingTime.longValue
        _maxTime.longValue = _startingTime.longValue
        _isNewCounter.value = true
        _isCounting.value = false
    }

    fun createTimer(time: Long) {
        _timer.value = ProtivityCountDownTimer(time, { handleTick(it) }, { resetTimer() })
        _startingTime.longValue = time
        _remainingTime.longValue = time
        _maxTime.longValue = time
    }

    fun startOrPauseTimer() {
        if (_isCounting.value) {
            _timer.value?.cancel()
            _isCounting.value = false
        } else {
            _timer.value = ProtivityCountDownTimer(
                _remainingTime.longValue,
                { handleTick(it) },
                { resetTimer() })
            _timer.value?.start()
            _isCounting.value = true
            _isNewCounter.value = false
        }
    }

    fun increaseTimer(timeIncrement: Long) {
        _remainingTime.longValue += timeIncrement * 1000
        if (_remainingTime.longValue > _maxTime.longValue) {
            _maxTime.longValue = _remainingTime.longValue
        }
        _timer.value?.cancel()
        _timer.value = ProtivityCountDownTimer(
            _remainingTime.longValue,
            { handleTick(it) },
            { resetTimer() })
        if (_isCounting.value) {
            _timer.value?.start()
        }
    }

    fun onCompose(time: Long) {
        createTimer(time)
        _startingTime.longValue = time
        _remainingTime.longValue = time
        if (_isCounting.value) {
            _timer.value?.start()
        }
        _isNewCounter.value = true
    }

    fun handleDispose() {
        _timer.value?.cancel()
        _isCounting.value = false
    }
}
