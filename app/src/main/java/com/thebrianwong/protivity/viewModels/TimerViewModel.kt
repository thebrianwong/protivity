package com.thebrianwong.protivity.viewModels

import android.os.Build
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import com.thebrianwong.protivity.classes.BoolDataStoreKeys
import com.thebrianwong.protivity.classes.LongDataStoreKeys
import com.thebrianwong.protivity.classes.NotificationUtils
import com.thebrianwong.protivity.classes.PermissionUtils
import com.thebrianwong.protivity.classes.ProtivityCountDownTimer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class TimerViewModel : ViewModel() {
    private val _timer = mutableStateOf<ProtivityCountDownTimer?>(null)
    private val _startingTime = mutableLongStateOf(0L)
    private val _remainingTime = mutableLongStateOf(0L)
    private val _maxTime = mutableLongStateOf(_startingTime.longValue)
    private val _isNewCounter = mutableStateOf(true)
    private val _isCounting = mutableStateOf(false)
    private val _window = mutableStateOf<Window?>(null)

    private val _coroutine = mutableStateOf<CoroutineScope?>(null)
    private val _dataStore = mutableStateOf<DataStore<Preferences>?>(null)
    private val _permissionUtils = mutableStateOf<PermissionUtils?>(null)
    private val _notificationUtils = mutableStateOf<NotificationUtils?>(null)

    val timer = _timer
    val startingTime = _startingTime
    val remainingTime = _remainingTime
    val maxTime = _maxTime
    val isNewCounter = _isNewCounter
    val isCounting = _isCounting

    fun setCoroutine(coroutineScope: CoroutineScope) {
        _coroutine.value = coroutineScope
    }

    fun setDataStore(dataStore: DataStore<Preferences>) {
        _dataStore.value = dataStore
    }

    fun setPermissionUtils(permissionUtils: PermissionUtils) {
        _permissionUtils.value = permissionUtils
    }

    fun setNotificationUtils(notificationUtils: NotificationUtils) {
        _notificationUtils.value = notificationUtils
    }

    fun setWindow(window: Window) {
        _window.value = window
    }

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
        _coroutine.value?.launch {
            _dataStore.value?.edit { timerValues ->
                timerValues[LongDataStoreKeys.REMAINING_TIME.key] = newTime
            }
        }
    }

    private fun dispatchTimerNotification() {
        if ( _permissionUtils.value?.hasNotificationPermission() == true) {
            _notificationUtils.value?.dispatchNotification()
        }
    }

    private fun enableScreenTimeout() {
        _window.value?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun disableScreenTimeout() {
        _window.value?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun handleFinish() {
        resetTimer()
        dispatchTimerNotification()
        enableScreenTimeout()
    }

    fun resetTimer() {
        _timer.value =
            ProtivityCountDownTimer(_startingTime.longValue, { handleTick(it) }, { handleFinish() })
        _remainingTime.longValue = _startingTime.longValue
        _maxTime.longValue = _startingTime.longValue
        _isNewCounter.value = true
        _isCounting.value = false
        _coroutine.value?.launch {
            _dataStore.value?.edit { timerValues ->
                timerValues[LongDataStoreKeys.REMAINING_TIME.key] = _startingTime.longValue
                timerValues[LongDataStoreKeys.MAX_TIME.key] = _startingTime.longValue
                timerValues[BoolDataStoreKeys.NEW_COUNTER.key] = true
            }
        }
    }


    fun startOrPauseTimer() {
        if (_isCounting.value) {
            _timer.value?.cancel()
            _isCounting.value = false
            enableScreenTimeout()
        } else {
            _timer.value = ProtivityCountDownTimer(
                _remainingTime.longValue,
                { handleTick(it) },
                { handleFinish() })
            _timer.value?.start()
            _isCounting.value = true
            _isNewCounter.value = false
            disableScreenTimeout()
            _coroutine.value?.launch {
                _dataStore.value?.edit { timerValues ->
                    timerValues[BoolDataStoreKeys.NEW_COUNTER.key] = false
                }
            }
        }
    }

    fun increaseTimer(timeIncrement: Long) {
        _remainingTime.longValue += timeIncrement * 1000
        if (!_isCounting.value) {
            _startingTime.longValue = _remainingTime.longValue
        }
        if (_remainingTime.longValue > _maxTime.longValue) {
            _maxTime.longValue = _remainingTime.longValue
            if (_isCounting.value) {
                _coroutine.value?.launch {
                    _dataStore.value?.edit { timerValues ->
                        timerValues[LongDataStoreKeys.REMAINING_TIME.key] = _remainingTime.longValue
                        timerValues[LongDataStoreKeys.MAX_TIME.key] = maxTime.longValue
                    }
                }
            } else {
                _coroutine.value?.launch {
                    _dataStore.value?.edit { timerValues ->
                        timerValues[LongDataStoreKeys.STARTING_TIME.key] = _startingTime.longValue
                        timerValues[LongDataStoreKeys.REMAINING_TIME.key] = _remainingTime.longValue
                        timerValues[LongDataStoreKeys.MAX_TIME.key] = _maxTime.longValue
                    }
                }
            }
        } else {
            if (_isCounting.value) {
                _coroutine.value?.launch {
                    _dataStore.value?.edit { timerValues ->
                        timerValues[LongDataStoreKeys.REMAINING_TIME.key] = _remainingTime.longValue
                    }
                }
            } else {
                _coroutine.value?.launch {
                    _dataStore.value?.edit { timerValues ->
                        timerValues[LongDataStoreKeys.STARTING_TIME.key] = _startingTime.longValue
                        timerValues[LongDataStoreKeys.REMAINING_TIME.key] = _remainingTime.longValue
                    }
                }
            }
        }
        _timer.value?.cancel()
        _timer.value = ProtivityCountDownTimer(
            _remainingTime.longValue,
            { handleTick(it) },
            { handleFinish() })
        if (_isCounting.value) {
            _timer.value?.start()
        }
    }

    fun createTimer(time: Long) {
        _timer.value = ProtivityCountDownTimer(time, { handleTick(it) }, { handleFinish() })
        _startingTime.longValue = time
        _remainingTime.longValue = time
        _maxTime.longValue = time
        _isNewCounter.value = true
        _coroutine.value?.launch {
            _dataStore.value?.edit { timerValues ->
                timerValues[LongDataStoreKeys.STARTING_TIME.key] = time
                timerValues[LongDataStoreKeys.REMAINING_TIME.key] = time
                timerValues[LongDataStoreKeys.MAX_TIME.key] = time
                timerValues[BoolDataStoreKeys.NEW_COUNTER.key] = true
            }
        }
    }

    fun loadTimer(startingTime: Long, remainingTime: Long, maxTime: Long, isNewCounter: Boolean) {
        _timer.value = ProtivityCountDownTimer(remainingTime, { handleTick(it) }, { handleFinish() })
        _startingTime.longValue = startingTime
        _remainingTime.longValue = remainingTime
        _maxTime.longValue = maxTime
        _isNewCounter.value = isNewCounter
    }

    fun disposeTimer() {
        _timer.value?.cancel()
        _isCounting.value = false
    }
}
