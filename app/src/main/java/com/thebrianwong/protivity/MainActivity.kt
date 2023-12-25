package com.thebrianwong.protivity

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thebrianwong.protivity.classes.BoolDataStoreKeys
import com.thebrianwong.protivity.classes.LongDataStoreKeys
import com.thebrianwong.protivity.viewModels.TimerViewModel
import com.thebrianwong.protivity.ui.theme.ProtivityTheme
import com.thebrianwong.protivity.viewModels.ModalViewModel
import com.thebrianwong.protivity.views.Home
import kotlinx.coroutines.flow.map

class MainActivity : ComponentActivity() {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "timer")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val coroutine = rememberCoroutineScope()
            val timerViewModel: TimerViewModel = viewModel()
            val modalViewModel: ModalViewModel = viewModel()
            timerViewModel.setDataStore(dataStore)
            timerViewModel.setCoroutine(coroutine)

            if (timerViewModel.timer.value == null) {
                val savedTimerValues = dataStore.data.map { preferences ->
                    listOf(
                        preferences[LongDataStoreKeys.STARTING_TIME.key],
                        preferences[LongDataStoreKeys.REMAINING_TIME.key],
                        preferences[LongDataStoreKeys.MAX_TIME.key]
                    )
                }
                val savedNewCounterState = dataStore.data.map { preferences ->
                    preferences[BoolDataStoreKeys.NEW_COUNTER.key]
                }
                val timerValues: List<Long?> by savedTimerValues.collectAsState(
                    initial = listOf(
                        null,
                        null,
                        null
                    )
                )
                val isNewCounter by savedNewCounterState.collectAsState(initial = null)

                val savedStartingTime = timerValues[0]
                val savedRemainingTime = timerValues[1]
                val savedMaxTime = timerValues[2]
                if (savedStartingTime != null && savedRemainingTime != null && savedMaxTime != null) {
                    timerViewModel.loadTimer(
                        savedStartingTime, savedRemainingTime, savedMaxTime, isNewCounter!!
                    )
                }
            }

            ProtivityTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Home(timerViewModel, modalViewModel, dataStore)
                }
            }
        }
    }
}
