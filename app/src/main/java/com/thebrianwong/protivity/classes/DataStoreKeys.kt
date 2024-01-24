package com.thebrianwong.protivity.classes

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey

enum class LongDataStoreKeys(val key: Preferences.Key<Long>) {
    STARTING_TIME(longPreferencesKey("startingTime")),
    REMAINING_TIME(longPreferencesKey("remainingTime")),
    MAX_TIME(longPreferencesKey("maxTime")),
}

enum class BoolDataStoreKeys(val key: Preferences.Key<Boolean>) {
    NEW_COUNTER(booleanPreferencesKey("newCounter")),
    SHOULD_VIBRATE(booleanPreferencesKey("shouldVibrate")),
    SHOULD_PLAY_ALARM(booleanPreferencesKey("shouldPlayAlarm")),
    SHOULD_RESET_TEXT(booleanPreferencesKey("shouldResetValue"))
}
