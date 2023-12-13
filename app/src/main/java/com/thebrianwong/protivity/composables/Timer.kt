package com.thebrianwong.protivity.composables

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.thebrianwong.protivity.classes.ProtivityCountDownTimer

@Composable
fun Timer(startingDuration: Long) {
    var remainingTime by remember { mutableLongStateOf(startingDuration) }
    val hours = (remainingTime / 3600 / 1000).toInt()
    val minutes = ((remainingTime / 1000 - (hours * 3600)) / 60).toInt()
    val seconds = (remainingTime / 1000 - (hours * 3600) - (minutes * 60)).toInt()
    var isNewCounter by remember { mutableStateOf(true)}
    var isCounting by remember { mutableStateOf(false) }

    fun updateRemainingTime(newTime: Long) {
        remainingTime = newTime
    }

    var timer by remember {
        mutableStateOf(ProtivityCountDownTimer(
            0, { }, {}
        ))
    }

    fun formatTime(timeUnit: Int) = if (timeUnit < 10) "0$timeUnit" else timeUnit

    fun resetTimer() {
        timer =
            ProtivityCountDownTimer(remainingTime, { updateRemainingTime(it) }, { resetTimer() })
        remainingTime = startingDuration
        isCounting = false
        isNewCounter = true
    }

    fun handleButtonClick() {
        if (isCounting) {
            timer.cancel()
            isCounting = false
        } else {
            timer = ProtivityCountDownTimer(
                remainingTime,
                { updateRemainingTime(it) },
                { resetTimer() })
            timer.start()
            isCounting = true
            isNewCounter = false
        }
    }

    fun increaseTimer(timeIncrement: Long) {
        remainingTime += timeIncrement * 1000
        timer.cancel()
        timer =
            ProtivityCountDownTimer(remainingTime, { updateRemainingTime(it) }, { resetTimer() })
        if (isCounting) {
            timer.start()
        }
    }

    DisposableEffect(key1 = startingDuration) {
        timer =
            ProtivityCountDownTimer(startingDuration, { updateRemainingTime(it) }, { resetTimer() })
        remainingTime = startingDuration
        if (isCounting) {
            timer.start()
        }
        isNewCounter = true
        onDispose {
            timer.cancel()
            isCounting = false
        }
    }

    Text(text = "Remaining time: ${formatTime(hours)}:${formatTime(minutes)}:${formatTime(seconds)}")
    Button(onClick = { handleButtonClick() }) {
        Text(text = if (isCounting) "Pause" else if (isNewCounter) "Start" else "Resume")
    }
    TimeIncrementButtons(handleClick = { increaseTimer(it) })
}
