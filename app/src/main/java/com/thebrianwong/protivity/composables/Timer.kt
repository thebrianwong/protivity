package com.thebrianwong.protivity.composables

import android.os.CountDownTimer
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
    var isCounting by remember { mutableStateOf(false) }

    fun updateRemainingTime(newTime: Long) {
        remainingTime = newTime
    }

    var timer by remember {
        mutableStateOf(ProtivityCountDownTimer(
            startingDuration, { updateRemainingTime(it) }
        ))
    }

    fun formatTime(timeUnit: Int) = if (timeUnit < 10) "0$timeUnit" else timeUnit

    fun handleButtonClick() {
        if (isCounting) {
            timer.cancel()
            isCounting = false
        } else {
            timer = ProtivityCountDownTimer(remainingTime, { updateRemainingTime(it) })
            timer.start()
            isCounting = true
        }
    }

    DisposableEffect(key1 = startingDuration) {
        timer.start()
        isCounting = true
        onDispose {
            timer.cancel()
            isCounting = false
        }
    }

    Text(text = "Remaining time: ${formatTime(hours)}:${formatTime(minutes)}:${formatTime(seconds)}")
    Button(onClick = { handleButtonClick() }) {
        Text(text = if (isCounting) "Pause" else "Resume")
    }
}
