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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.thebrianwong.protivity.viewModels.TimerViewModel

@Composable
fun Timer(timer: TimerViewModel, dataStore: DataStore<Preferences>) {
    val indicatorProgress = animateFloatAsState(
        targetValue = (timer.maxTime.longValue - timer.remainingTime.longValue) / timer.maxTime.longValue.toFloat(),
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = "Timer Progress",
        visibilityThreshold = 0.1f
    )
    val configuration = LocalConfiguration.current

    fun formatTime(timeUnit: Int) = if (timeUnit < 10) "0$timeUnit" else timeUnit

    if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        CircularProgressIndicator(
            progress = indicatorProgress.value,
            strokeWidth = 4.dp,
            trackColor = MaterialTheme.colorScheme.inverseOnSurface
        )
        Text(
            text = "${formatTime(timer.getHours())}:${formatTime(timer.getMinutes())}:${
                formatTime(
                    timer.getSeconds()
                )
            }",
            fontSize = 64.sp
        )
        Button(
            onClick = { timer.startOrPauseTimer() },
            modifier = Modifier.animateContentSize(),
            enabled = timer.startingTime.longValue != 0L || timer.remainingTime.longValue != 0L
        ) {
            Text(text = if (timer.isCounting.value) "Pause" else if (timer.isNewCounter.value) "Start" else "Resume")
        }
        TimeIncrementButtons(handleClick = { timer.increaseTimer(it) })
        Button(
            onClick = { timer.resetTimer() },
            enabled = !timer.isCounting.value,
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
                    text = "${formatTime(timer.getHours())}:${formatTime(timer.getMinutes())}:${
                        formatTime(
                            timer.getSeconds()
                        )
                    }",
                    fontSize = 64.sp
                )
            }
            TimeIncrementButtons(handleClick = { timer.increaseTimer(it) })
        }
        Column(
            modifier = Modifier
                .fillMaxWidth(0.33f)
                .padding(horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { timer.startOrPauseTimer() },
                modifier = Modifier.animateContentSize(),
                enabled = timer.startingTime.longValue != 0L || timer.remainingTime.longValue != 0L
            ) {
                Text(text = if (timer.isCounting.value) "Pause" else if (timer.isNewCounter.value) "Start" else "Resume")
            }
            Button(
                onClick = { timer.resetTimer() },
                enabled = !timer.isCounting.value,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = "Reset")
            }
        }
    }
}
