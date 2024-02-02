package com.thebrianwong.protivity.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thebrianwong.protivity.R
import com.thebrianwong.protivity.viewModels.SettingsViewModel

@Composable
fun Settings(viewModel: SettingsViewModel, navigateToHome: () -> Unit) {
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
            "enabledIcon" to R.drawable.clear_text_enabled,
            "disabledIcon" to R.drawable.clear_text_disabled
        ),
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                        contentDescription = "Settings Button",
                        modifier = Modifier
                            .padding(16.dp)
                            .height(32.dp)
                            .width(32.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { navigateToHome() }
                    )
                    Text(
                        text = "Settings",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 16.dp)
                    )

                }
                Divider(
                    color = Color.DarkGray, modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(android.graphics.Color.parseColor("#F5F5F5")))
                .padding(it)
        ) {
            settingsIcons.forEach { data ->
                val setting = data["setting"] as String
                val enabledIcon = data["enabledIcon"] as Int
                val disabledIcon = data["disabledIcon"] as Int
                val settingIsEnabled = viewModel.getSetting(setting)

                Row(
                    modifier = Modifier
                        .padding(bottom = 16.dp, start = 32.dp, end = 32.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                    ) {

                        Icon(
                            painter = painterResource(id = if (settingIsEnabled) enabledIcon else disabledIcon),
                            contentDescription = "$setting Setting",
                            modifier = Modifier
                        )
                        Text(
                            text = setting, modifier = Modifier
                                .padding(start = 8.dp)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .weight(0.5f)
                            .height(IntrinsicSize.Min),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Divider(
                            color = Color.DarkGray, modifier = Modifier
                                .fillMaxHeight(0.6f)
                                .width(1.dp)
                        )
                        Switch(
                            checked = settingIsEnabled,
                            onCheckedChange = { viewModel.toggleSetting(setting) },
                            modifier = Modifier
                                .padding(start = 32.dp)
                        )
                    }
                }
            }
        }
    }
}
