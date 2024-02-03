package com.thebrianwong.protivity.composables.settings

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SettingsSwitch(setting: String, enabled: Boolean, handleOnToggle: (String) -> Unit) {
    Divider(
        color = Color.DarkGray, modifier = Modifier
            .fillMaxHeight(0.6f)
            .width(1.dp)
    )
    Switch(
        checked = enabled,
        onCheckedChange = { handleOnToggle(setting) },
        modifier = Modifier
            .padding(start = 32.dp)
    )
}
