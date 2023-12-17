package com.thebrianwong.protivity.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.thebrianwong.protivity.composables.ChatGPTTextWindow
import com.thebrianwong.protivity.composables.FloatingActionButton
import com.thebrianwong.protivity.composables.TimeModal
import com.thebrianwong.protivity.composables.Timer

@Composable
fun Home() {
    var duration by remember { mutableStateOf<Long?>(null) }
    var displayModal by remember { mutableStateOf(false) }
    Scaffold(
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                newTimer = duration == null,
                handleClick = {
                    displayModal = true
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(vertical = 64.dp, horizontal = 48.dp)
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (duration != null) {
                Timer(startingDuration = duration!!)
                Divider(modifier = Modifier.padding(bottom = 8.dp))
                ChatGPTTextWindow()
            } else {
                Text(text = "Click on the \"+\" to add a timer!")
            }
            if (displayModal) {
                TimeModal(
                    newTimer = duration == null,
                    handleConfirm = { duration = it },
                    handleDismiss = { displayModal = false }
                )
            }
        }
    }
}
