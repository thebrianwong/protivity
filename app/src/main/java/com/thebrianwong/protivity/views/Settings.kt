package com.thebrianwong.protivity.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.thebrianwong.protivity.R
import com.thebrianwong.protivity.composables.settings.SettingsIconText
import com.thebrianwong.protivity.composables.settings.SettingsSwitch
import com.thebrianwong.protivity.composables.settings.SettingsTopBar
import com.thebrianwong.protivity.viewModels.SettingsViewModel

@Composable
fun Settings(viewModel: SettingsViewModel, navigateToHome: () -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SettingsTopBar(navigateToHome)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(android.graphics.Color.parseColor("#F5F5F5")))
                .padding(it)
        ) {
            val settingsIcons = listOf(
                hashMapOf(
                    "setting" to "Alarm",
                    "enabledIcon" to R.drawable.alarm_enabled,
                    "disabledIcon" to R.drawable.alarm_disabled
                ),
                hashMapOf(
                    "setting" to "Vibrate",
                    "enabledIcon" to R.drawable.vibrate_enabled,
                    "disabledIcon" to R.drawable.vibrated_disabled
                ),
                hashMapOf(
                    "setting" to "Clear Text",
                    "description" to "Erases AI text when the timer expires.",
                    "enabledIcon" to R.drawable.clear_text_enabled,
                    "disabledIcon" to R.drawable.clear_text_disabled
                ),
                hashMapOf(
                    "setting" to "Strict Mode",
                    "description" to "Prevents switching to other apps.",
                    "enabledIcon" to R.drawable.clear_text_enabled,
                    "disabledIcon" to R.drawable.clear_text_disabled
                )
            )

            settingsIcons.forEach { data ->
                val setting = data["setting"] as String
                val description = data["description"] as String?
                val enabledIcon = data["enabledIcon"] as Int
                val disabledIcon = data["disabledIcon"] as Int
                val settingIsEnabled = viewModel.getSetting(setting)

                Row(
                    modifier = Modifier
                        .clickable(onClick = {})
                        .padding(vertical = 12.dp, horizontal = 32.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        if (description != null) {
                            Column {
                                Row {
                                    SettingsIconText(
                                        setting = setting,
                                        enabledIcon = enabledIcon,
                                        disabledIcon = disabledIcon,
                                        enabled = settingIsEnabled
                                    )
                                }
                                Text(text = description, fontSize = 12.sp, lineHeight = 1.em)
                            }
                        } else {
                            SettingsIconText(
                                setting = setting,
                                enabledIcon = enabledIcon,
                                disabledIcon = disabledIcon,
                                enabled = settingIsEnabled
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .weight(0.5f)
                            .height(IntrinsicSize.Min),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SettingsSwitch(
                            setting = setting,
                            enabled = settingIsEnabled,
                            handleOnToggle = { viewModel.toggleSetting(setting) })
                    }
                }
            }
        }
    }
}
