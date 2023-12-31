package com.thebrianwong.protivity

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thebrianwong.protivity.classes.BoolDataStoreKeys
import com.thebrianwong.protivity.classes.LongDataStoreKeys
import com.thebrianwong.protivity.classes.NotificationUtils
import com.thebrianwong.protivity.classes.PermissionUtils
import com.thebrianwong.protivity.viewModels.TimerViewModel
import com.thebrianwong.protivity.ui.theme.ProtivityTheme
import com.thebrianwong.protivity.viewModels.ModalViewModel
import com.thebrianwong.protivity.views.Home
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "timer")

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            val coroutine = rememberCoroutineScope()
            val timerViewModel: TimerViewModel = viewModel()
            val modalViewModel: ModalViewModel = viewModel()
            val permissionUtils = PermissionUtils(context)
            val notificationUtils = NotificationUtils(context)
            timerViewModel.setDataStore(dataStore)
            timerViewModel.setCoroutine(coroutine)
            timerViewModel.setPermissionUtils(permissionUtils)
            timerViewModel.setNotificationUtils(notificationUtils)
            timerViewModel.setWindow(window)

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


            val requestPermissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(),
                onResult = { isGranted ->
                    if (!isGranted) {
                        val skippedRationaleDialog =
                            ActivityCompat.shouldShowRequestPermissionRationale(
                                context as MainActivity,
                                Manifest.permission.POST_NOTIFICATIONS
                            )

                        if (skippedRationaleDialog) {
                            Toast.makeText(
                                context,
                                "Enable notifications for optimal experience.",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                context,
                                "Enable notifications in Android Settings for optimal experience.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            )

            LaunchedEffect(context) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
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
