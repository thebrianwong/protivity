package com.thebrianwong.protivity.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thebrianwong.protivity.R
import com.thebrianwong.protivity.viewModels.SettingsViewModel

@Composable
fun SettingsDrawer(viewModel: SettingsViewModel) {
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

    ModalDrawerSheet {
        Column(
            modifier = Modifier
                .padding(horizontal = 64.dp, vertical = 32.dp)
                .widthIn(0.dp, 175.dp)
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
