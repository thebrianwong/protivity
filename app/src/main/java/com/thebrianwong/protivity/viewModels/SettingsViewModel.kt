package com.thebrianwong.protivity.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import com.thebrianwong.protivity.classes.BoolDataStoreKeys
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {
    private val _alarmEnabled = mutableStateOf(true)
    private val _vibrateEnabled = mutableStateOf(true)
    private val _clearTextEnabled = mutableStateOf(true)

    private val _coroutine = mutableStateOf<CoroutineScope?>(null)
    private val _dataStore = mutableStateOf<DataStore<Preferences>?>(null)

    val alarmEnabled = _alarmEnabled.value
    val vibrateEnabled = _vibrateEnabled.value
    val clearTextEnabled = _clearTextEnabled.value

    fun setCoroutine(coroutineScope: CoroutineScope) {
        _coroutine.value = coroutineScope
    }

    fun setDataStore(dataStore: DataStore<Preferences>) {
        _dataStore.value = dataStore
    }

    fun loadSettings(alarmEnabled: Boolean, vibrateEnabled: Boolean, clearTextEnabled: Boolean) {
        _alarmEnabled.value = alarmEnabled
        _vibrateEnabled.value = vibrateEnabled
        _clearTextEnabled.value = clearTextEnabled
    }

    fun getSetting(setting: String): Boolean {
        return when (setting) {
            "Alarm" -> _alarmEnabled.value
            "Vibrate" -> _vibrateEnabled.value
            "Clear Text" -> _clearTextEnabled.value
            else -> true
        }
    }

    fun toggleSetting(setting: String) {
        when (setting) {
            "Alarm" -> {
                _alarmEnabled.value = !_alarmEnabled.value
                _coroutine.value?.launch {
                    _dataStore.value?.edit { settings ->
                        settings[BoolDataStoreKeys.SHOULD_PLAY_ALARM.key] = _alarmEnabled.value
                    }
                }
            }
            "Vibrate" -> {
                _vibrateEnabled.value = !_vibrateEnabled.value
                _coroutine.value?.launch {
                    _dataStore.value?.edit { settings ->
                        settings[BoolDataStoreKeys.SHOULD_VIBRATE.key] = _vibrateEnabled.value
                    }
                }
            }
            "Clear Text" -> {
                _clearTextEnabled.value = !_clearTextEnabled.value
                _coroutine.value?.launch {
                    _dataStore.value?.edit { settings ->
                        settings[BoolDataStoreKeys.SHOULD_CLEAR_TEXT.key] = _clearTextEnabled.value
                    }
                }
            }
        }
    }
}
