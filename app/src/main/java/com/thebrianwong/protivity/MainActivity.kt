package com.thebrianwong.protivity

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thebrianwong.protivity.viewModels.TimerViewModel
import com.thebrianwong.protivity.ui.theme.ProtivityTheme
import com.thebrianwong.protivity.viewModels.ModalViewModel
import com.thebrianwong.protivity.views.Home

class MainActivity : ComponentActivity() {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "timer")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val timerViewModel: TimerViewModel = viewModel()
            val modalViewModel: ModalViewModel = viewModel()
            timerViewModel.setDataStore(dataStore)
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
