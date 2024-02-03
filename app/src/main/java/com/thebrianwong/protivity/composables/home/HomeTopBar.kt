package com.thebrianwong.protivity.composables.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.thebrianwong.protivity.R

@Composable
fun HomeTopBar(navigateToSettings: () -> Unit) {
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
                ) { navigateToSettings() }
        )
    }
}
