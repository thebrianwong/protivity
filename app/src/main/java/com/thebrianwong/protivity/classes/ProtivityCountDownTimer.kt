package com.thebrianwong.protivity.classes

import android.os.CountDownTimer

class ProtivityCountDownTimer(
    duration: Long,
    val updateTimeCounter: (Long) -> Unit,
    val handleOnFinish: () -> Unit
) :
    CountDownTimer(duration, 10) {
    override fun onTick(newTime: Long) {
        updateTimeCounter(newTime)
    }

    override fun onFinish() {
        handleOnFinish()
    }
}
