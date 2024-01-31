package com.thebrianwong.protivity

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.view.Window
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.apollographql.apollo3.ApolloClient
import com.thebrianwong.protivity.classes.BoolDataStoreKeys
import com.thebrianwong.protivity.classes.LongDataStoreKeys
import com.thebrianwong.protivity.classes.NotificationUtils
import com.thebrianwong.protivity.classes.PermissionUtils
import com.thebrianwong.protivity.classes.Screen
import com.thebrianwong.protivity.viewModels.ChatGPTViewModel
import com.thebrianwong.protivity.viewModels.ModalViewModel
import com.thebrianwong.protivity.viewModels.SettingsViewModel
import com.thebrianwong.protivity.viewModels.TimerViewModel
import com.thebrianwong.protivity.views.Home
import com.thebrianwong.protivity.views.Settings
import kotlinx.coroutines.flow.map

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun ProtivityApp(dataStore: DataStore<Preferences>, window: Window) {
    val context = LocalContext.current
    val coroutine = rememberCoroutineScope()
    val navController: NavController = rememberNavController()

    val timerViewModel: TimerViewModel = viewModel()
    val modalViewModel: ModalViewModel = viewModel()
    val settingsViewModel: SettingsViewModel = viewModel()
    val permissionUtils by remember { mutableStateOf(PermissionUtils(context)) }
    val notificationUtils by remember { mutableStateOf(NotificationUtils(context)) }

    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val hasInternetConnection = connectivityManager.activeNetwork

    val chatGPTViewModel: ChatGPTViewModel = viewModel()
    val graphQLEndpoint = stringResource(R.string.GRAPHQL_ENDPOINT)
    var apolloClient by remember {
        mutableStateOf<ApolloClient?>(null)
    }

    if (apolloClient == null && hasInternetConnection != null) {
        apolloClient = ApolloClient.Builder()
            .serverUrl(graphQLEndpoint)
            .build()
    }

    timerViewModel.setDataStore(dataStore)
    timerViewModel.setCoroutine(coroutine)
    timerViewModel.setPermissionUtils(permissionUtils)
    timerViewModel.setNotificationUtils(notificationUtils)
    timerViewModel.setWindow(window)

    settingsViewModel.setDataStore(dataStore)
    settingsViewModel.setCoroutine(coroutine)
    settingsViewModel.setChangeNotiSettingsCallback { alarmEnabled, vibrateEnabled ->
        notificationUtils.changeNotificationChannels(
            alarmEnabled,
            vibrateEnabled
        )
    }

    chatGPTViewModel.setCoroutine(coroutine)
    if (apolloClient != null) {
        chatGPTViewModel.setApolloClient(apolloClient!!)
    }
    chatGPTViewModel.setIndicateNetworkErrorCallback {
        Toast.makeText(
            context,
            "There is a network connection issue. Please try again later.",
            Toast.LENGTH_LONG
        ).show()
    }
    timerViewModel.setGenTextCallback { chatGPTViewModel.changeDisplayText(it) }
    timerViewModel.setResetTextCallback { chatGPTViewModel.resetText() }

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

    val rawSettingsData = dataStore.data.map { settings ->
        listOf(
            settings[BoolDataStoreKeys.SHOULD_PLAY_ALARM.key],
            settings[BoolDataStoreKeys.SHOULD_VIBRATE.key],
            settings[BoolDataStoreKeys.SHOULD_CLEAR_TEXT.key]
        )
    }
    val settingsData: List<Boolean?> by rawSettingsData.collectAsState(
        initial = listOf(
            true,
            true,
            true
        )
    )
    val alarmSetting = settingsData[0]
    val vibrateSetting = settingsData[1]
    val clearTextSetting = settingsData[2]
    settingsViewModel.loadSettings(
        alarmSetting ?: true,
        vibrateSetting ?: true,
        clearTextSetting ?: true
    )

    notificationUtils.changeNotificationChannels(alarmSetting ?: true, vibrateSetting ?: true)
    timerViewModel.setShouldResetText(clearTextSetting ?: true)

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

        if (chatGPTViewModel.initializing.value) {
            chatGPTViewModel.initializeText(10000)

            if (hasInternetConnection == null) {
                Toast.makeText(
                    context,
                    "There is a network connection issue. Please try again later.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    }

    NavHost(navController = navController as NavHostController, startDestination = Screen.HomeScreen.route) {
        composable(Screen.HomeScreen.route) {
            Home(timerViewModel, modalViewModel, chatGPTViewModel, settingsViewModel, dataStore)
        }
        composable(Screen.SettingsScreen.route) {
            Settings(settingsViewModel)
        }
    }

}
