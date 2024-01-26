package com.thebrianwong.protivity.composables

import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.thebrianwong.protivity.R

@Composable
fun SettingsDrawer(dataStore: DataStore<Preferences>) {
    val settingsIcons = listOf(
        hashMapOf(
            "setting" to "Alarm",
            "icon" to R.drawable.baseline_alarm_on_24
        ),
        hashMapOf(
            "setting" to "Vibrate",
            "icon" to R.drawable.baseline_vibration_24
        ),
        hashMapOf(
            "setting" to "Clear Text",
            "icon" to R.drawable.baseline_check_24
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
                val icon = data["icon"] as Int

                Row(
                    modifier = Modifier.padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = "$setting Setting",
                        modifier = Modifier.weight(1f)
                    )
                    Text(text = setting, modifier = Modifier.weight(1f))
                    Switch(checked = true, onCheckedChange = {}, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
