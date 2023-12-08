package com.thebrianwong.protivity.composables

import android.os.CountDownTimer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun Timer(duration: Long) {
    var remainingTime by remember { mutableLongStateOf(duration) }
//    val hours = (remainingTime / 3600).toInt()
//    val minutes = (remainingTime - (hours * 3600)).toInt()
//    val seconds = ()
    val minutes = (remainingTime / 60).toInt()
    val seconds = remainingTime - minutes
    val hours = (minutes / 60).toInt()


    val timer = object:CountDownTimer(duration, 1000){
        override fun onTick(newTime: Long) {
            remainingTime = newTime / 1000
        }
        override fun onFinish() {
//            figure out how to send phone alerts
        }
    }

    DisposableEffect(key1 = duration){
        timer.start()
        onDispose {
            timer.cancel()
        }
    }

//    Text(text = "Remaining second: ${remainingTime / 1}")
    Text(text = "Remaining time: $hours:$minutes:$seconds")
}
