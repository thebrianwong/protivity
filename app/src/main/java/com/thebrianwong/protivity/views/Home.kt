package com.thebrianwong.protivity.views

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.thebrianwong.protivity.R
import com.thebrianwong.protivity.classes.BoolDataStoreKeys
import com.thebrianwong.protivity.classes.LongDataStoreKeys
import com.thebrianwong.protivity.viewModels.TimerViewModel
import com.thebrianwong.protivity.composables.ChatGPTTextWindow
import com.thebrianwong.protivity.composables.FloatingActionButton
import com.thebrianwong.protivity.composables.SettingsDrawer
import com.thebrianwong.protivity.composables.TimeModal
import com.thebrianwong.protivity.composables.Timer
import com.thebrianwong.protivity.viewModels.ChatGPTViewModel
import com.thebrianwong.protivity.viewModels.ModalViewModel
import com.thebrianwong.protivity.viewModels.SettingsViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@Composable
fun Home(
    timerViewModel: TimerViewModel,
    modalViewModel: ModalViewModel,
    chatGPTViewModel: ChatGPTViewModel,
    settingsViewModel: SettingsViewModel,
    dataStore: DataStore<Preferences>
) {
    var displayModal by rememberSaveable { mutableStateOf(false) }
    val configuration = LocalConfiguration.current
    val modifier = Modifier
        .fillMaxSize()
        .padding(vertical = 64.dp, horizontal = 48.dp)
        .shadow(elevation = 8.dp, shape = RoundedCornerShape(20.dp))
        .background(MaterialTheme.colorScheme.secondaryContainer)
        .padding(32.dp)
    val coroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(drawerContent = { SettingsDrawer(viewModel = settingsViewModel) }) {


        Scaffold(
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = {
                FloatingActionButton(
                    newTimer = timerViewModel.timer.value == null,
                    handleClick = {
                        displayModal = true
                        modalViewModel.handleOpenModal()
                    }
                )
            }
        ) {
            Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth()) {
                Icon(painter = painterResource(id = R.drawable.baseline_settings_24),
                    contentDescription = "Settings Button",
                    modifier = Modifier
                        .padding(16.dp)
                        .height(32.dp)
                        .width(32.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { println("dddd")}
                )
            }
            if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                Column(
                    modifier = modifier.padding(it),
                    verticalArrangement = (if (timerViewModel.timer.value != null) Arrangement.spacedBy(
                        16.dp,
                        Alignment.Top
                    ) else Arrangement.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (timerViewModel.timer.value != null) {
                        Timer(timer = timerViewModel)
                        Divider(modifier = Modifier.padding(bottom = 8.dp))
                        ChatGPTTextWindow(chatGPTViewModel)
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Click on the \"+\" to add a timer!")
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(text = "Swipe right to change settings!")
                        }
                    }
                }
            } else {
                Row(
                    modifier = modifier.padding(it),
                    horizontalArrangement = Arrangement.spacedBy(
                        16.dp,
                        Alignment.CenterHorizontally
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (timerViewModel.timer.value != null) {
                        Timer(timer = timerViewModel)
                        Divider(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(1.dp)
                        )
                        ChatGPTTextWindow(chatGPTViewModel)
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Click on the \"+\" to add a timer!")
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(text = "Swipe right to change settings!")
                        }
                    }
                }
            }
            if (displayModal) {
                TimeModal(
                    modalViewModel = modalViewModel,
                    newTimer = timerViewModel.timer.value == null,
                    handleConfirm = {
                        timerViewModel.disposeTimer()
                        timerViewModel.createTimer(it)
                        coroutineScope.launch {
                            dataStore.edit { timerValues ->
                                timerValues[LongDataStoreKeys.STARTING_TIME.key] = it
                                timerValues[LongDataStoreKeys.REMAINING_TIME.key] = it
                                timerValues[LongDataStoreKeys.MAX_TIME.key] = it
                            }
                        }
                    },
                    handleDismiss = { displayModal = false }
                )
            }
        }
    }
}
