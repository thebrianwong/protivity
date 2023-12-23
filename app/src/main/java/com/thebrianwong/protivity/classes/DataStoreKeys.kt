package com.thebrianwong.protivity.classes

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey

enum class DataStoreKeys(val key: Preferences.Key<Long>) {
    STARTING_TIME(longPreferencesKey("startingTime")),
    REMAINING_TIME(longPreferencesKey("remainingTime")),
    MAX_TIME(longPreferencesKey("maxTime"))
}
