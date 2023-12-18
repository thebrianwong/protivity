package com.thebrianwong.protivity.composables

import android.content.res.Configuration
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thebrianwong.protivity.classes.ProtivityCountDownTimer

@Composable
fun Timer(startingDuration: Long) {
    var remainingTime by remember { mutableLongStateOf(startingDuration) }
    val hours = (remainingTime / 3600 / 1000).toInt()
    val minutes = ((remainingTime / 1000 - (hours * 3600)) / 60).toInt()
    val seconds = (remainingTime / 1000 - (hours * 3600) - (minutes * 60)).toInt()
    var isNewCounter by remember { mutableStateOf(true) }
    var isCounting by remember { mutableStateOf(false) }
    var indicatorMax by remember { mutableLongStateOf(startingDuration) }
    val indicatorProgress = animateFloatAsState(
        targetValue = (indicatorMax - remainingTime) / indicatorMax.toFloat(),
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = "Timer Progress",
        visibilityThreshold = 0.1f
    )
    var timer by remember {
        mutableStateOf(ProtivityCountDownTimer(
            0, { }, {}
        ))
    }
    val configuration = LocalConfiguration.current

    fun updateRemainingTime(newTime: Long) {
        remainingTime = newTime
    }

    fun formatTime(timeUnit: Int) = if (timeUnit < 10) "0$timeUnit" else timeUnit

    fun resetTimer() {
        timer =
            ProtivityCountDownTimer(remainingTime, { updateRemainingTime(it) }, { resetTimer() })
        remainingTime = startingDuration
        isCounting = false
        isNewCounter = true
        indicatorMax = startingDuration
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
        if (remainingTime > indicatorMax) {
            indicatorMax = remainingTime
        }
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

    if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        CircularProgressIndicator(
            progress = indicatorProgress.value,
            strokeWidth = 4.dp,
            trackColor = MaterialTheme.colorScheme.inverseOnSurface
        )
        Text(
            text = "${formatTime(hours)}:${formatTime(minutes)}:${formatTime(seconds)}",
            fontSize = 64.sp
        )
        Button(
            onClick = { handleButtonClick() },
            modifier = Modifier.animateContentSize(),
            enabled = startingDuration != 0L || remainingTime != 0L
        ) {
            Text(text = if (isCounting) "Pause" else if (isNewCounter) "Start" else "Resume")
        }
        TimeIncrementButtons(handleClick = { increaseTimer(it) })
        Button(
            onClick = { resetTimer() },
            enabled = !isCounting,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Reset")
        }
    } else {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(
                    progress = indicatorProgress.value,
                    strokeWidth = 4.dp,
                    trackColor = MaterialTheme.colorScheme.inverseOnSurface,
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text(
                    text = "${formatTime(hours)}:${formatTime(minutes)}:${formatTime(seconds)}",
                    fontSize = 64.sp
                )
            }
            TimeIncrementButtons(handleClick = { increaseTimer(it) })
        }
        Column(
            modifier = Modifier
                .fillMaxWidth(0.33f)
                .padding(horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { handleButtonClick() },
                modifier = Modifier.animateContentSize(),
                enabled = startingDuration != 0L || remainingTime != 0L
            ) {
                Text(text = if (isCounting) "Pause" else if (isNewCounter) "Start" else "Resume")
            }
            Button(
                onClick = { resetTimer() },
                enabled = !isCounting,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = "Reset")
            }
        }
    }
}
