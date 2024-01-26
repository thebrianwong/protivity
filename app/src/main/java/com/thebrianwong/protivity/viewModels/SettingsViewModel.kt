package com.thebrianwong.protivity.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope

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

}
