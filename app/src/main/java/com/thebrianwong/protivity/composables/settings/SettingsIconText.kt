package com.thebrianwong.protivity.composables.settings

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun SettingsIconText(setting: String, enabledIcon: Int, disabledIcon: Int, enabled: Boolean) {
    Crossfade(targetState = enabled, label = "$setting Icon Change Animation") { enabled ->
        if (enabled) {
            Icon(
                painter = painterResource(id = enabledIcon),
                contentDescription = "$setting Setting",
            )
        } else {
            Icon(
                painter = painterResource(id = disabledIcon),
                contentDescription = "$setting Setting",
            )
        }
    }
    Text(
        text = setting,
        modifier = Modifier.padding(start = 8.dp)
    )
}
