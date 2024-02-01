package com.thebrianwong.protivity.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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
            Row(modifier = Modifier.fillMaxWidth()) {
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
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
//                .border(3.dp, Color.Red)
            ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Settings",
                fontSize = 32.sp,
                modifier = Modifier.padding(top = 32.dp, bottom = 48.dp)
            )
            settingsIcons.forEach { data ->
                val setting = data["setting"] as String
                val enabledIcon = data["enabledIcon"] as Int
                val disabledIcon = data["disabledIcon"] as Int
                val settingIsEnabled = viewModel.getSetting(setting)

                Row(
                    modifier = Modifier.padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = if (settingIsEnabled) enabledIcon else disabledIcon),
                        contentDescription = "$setting Setting",
                        modifier = Modifier.weight(1f)
                    )
                    Text(text = setting, modifier = Modifier.weight(1f))
                    Switch(
                        checked = settingIsEnabled,
                        onCheckedChange = { viewModel.toggleSetting(setting) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
