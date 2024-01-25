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
    ModalDrawerSheet {
        Column(
            modifier = Modifier
                .padding(horizontal = 64.dp, vertical = 32.dp)
                .widthIn(0.dp, 128.dp)
                .border(3.dp, Color.Red)
            ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Settings",
                fontSize = 32.sp,
                modifier = Modifier.padding(top = 32.dp, bottom = 48.dp)
            )
            Row(
                modifier = Modifier
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_alarm_on_24),
                    contentDescription = "Alarm Setting",
                    modifier = Modifier.weight(1f)
                )
                Text(text = "Alarm", modifier = Modifier.weight(2.5f).padding(horizontal = 8.dp))
                Switch(modifier = Modifier.weight(0.1f), checked = true, onCheckedChange = {})
            }
            Row(
                modifier = Modifier.padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_vibration_24),
                    contentDescription = "Vibrate Setting",
                    modifier = Modifier.weight(1f)
                )
                Text(text = "Vibrate", modifier = Modifier.weight(2.5f).padding(horizontal = 8.dp))
                Switch(modifier = Modifier.weight(0.1f), checked = true, onCheckedChange = {})
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_check_24),
                    contentDescription = "Clear Text Setting",
                    modifier = Modifier.weight(1f)
                )
                Text(text = "Clear Text",  modifier = Modifier.weight(2.5f).padding(horizontal = 8.dp))
                Switch(modifier = Modifier.weight(0.1f), checked = true, onCheckedChange = {})
            }
        }
    }
}
