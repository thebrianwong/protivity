package com.thebrianwong.protivity.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import com.thebrianwong.protivity.enums.BoolDataStoreKeys
import com.thebrianwong.protivity.enums.SettingsOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {
    private val _alarmEnabled = mutableStateOf(true)
    private val _vibrateEnabled = mutableStateOf(true)
    private val _clearTextEnabled = mutableStateOf(true)
    private val _strictModeEnabled = mutableStateOf(true)
    private val _strictModeCallback = mutableStateOf<((Boolean) -> Unit)?>(null)

    private val _coroutine = mutableStateOf<CoroutineScope?>(null)
    private val _dataStore = mutableStateOf<DataStore<Preferences>?>(null)

    private val _changeNotiSettingsCallback =
        mutableStateOf<((Boolean, Boolean) -> Unit)?>(null)

    val alarmEnabled = _alarmEnabled.value
    val vibrateEnabled = _vibrateEnabled.value
    val clearTextEnabled = _clearTextEnabled.value

    fun setCoroutine(coroutineScope: CoroutineScope) {
        _coroutine.value = coroutineScope
    }

    fun setDataStore(dataStore: DataStore<Preferences>) {
        _dataStore.value = dataStore
    }

    fun setChangeNotiSettingsCallback(callback: (Boolean, Boolean) -> Unit) {
        _changeNotiSettingsCallback.value = callback
    }

    fun setStrictModeCallback(callback: (Boolean) -> Unit) {
        _strictModeCallback.value = callback
    }

    fun loadSettings(
        alarmEnabled: Boolean,
        vibrateEnabled: Boolean,
        clearTextEnabled: Boolean,
        strictModeEnabled: Boolean
    ) {
        _alarmEnabled.value = alarmEnabled
        _vibrateEnabled.value = vibrateEnabled
        _clearTextEnabled.value = clearTextEnabled
        _strictModeEnabled.value = strictModeEnabled
    }

    fun getSetting(setting: SettingsOptions): Boolean {
        return when (setting) {
            SettingsOptions.ALARM -> _alarmEnabled.value
            SettingsOptions.VIBRATE -> _vibrateEnabled.value
            SettingsOptions.CLEAR_TEXT -> _clearTextEnabled.value
            SettingsOptions.STRICT_MODE -> _strictModeEnabled.value
        }
    }

    fun toggleSetting(setting: SettingsOptions) {
        when (setting) {
            SettingsOptions.ALARM -> {
                _alarmEnabled.value = !_alarmEnabled.value
                _coroutine.value?.launch {
                    _dataStore.value?.edit { settings ->
                        settings[BoolDataStoreKeys.SHOULD_PLAY_ALARM.key] = _alarmEnabled.value
                    }
                }
            }

            SettingsOptions.VIBRATE -> {
                _vibrateEnabled.value = !_vibrateEnabled.value
                _coroutine.value?.launch {
                    _dataStore.value?.edit { settings ->
                        settings[BoolDataStoreKeys.SHOULD_VIBRATE.key] = _vibrateEnabled.value
                    }
                }
            }

            SettingsOptions.CLEAR_TEXT -> {
                _clearTextEnabled.value = !_clearTextEnabled.value
                _coroutine.value?.launch {
                    _dataStore.value?.edit { settings ->
                        settings[BoolDataStoreKeys.SHOULD_CLEAR_TEXT.key] = _clearTextEnabled.value
                    }
                }
            }

            SettingsOptions.STRICT_MODE -> {
                _strictModeEnabled.value = !_strictModeEnabled.value
                _strictModeCallback.value?.let { it(_strictModeEnabled.value) }
                _coroutine.value?.launch {
                    _dataStore.value?.edit { settings ->
                        settings[BoolDataStoreKeys.IS_STRICT_MODE.key] = _strictModeEnabled.value
                    }
                }
            }
        }
        _changeNotiSettingsCallback.value?.let {
            it(
                _alarmEnabled.value,
                _vibrateEnabled.value
            )
        }
    }
}
