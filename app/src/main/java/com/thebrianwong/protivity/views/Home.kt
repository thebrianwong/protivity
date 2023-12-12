package com.thebrianwong.protivity.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
            FloatingActionButton({
                displayModal = true
            })
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 64.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (duration != null) {
                Timer(startingDuration = duration!!)
            }
            if (displayModal) {
                TimeModal(
                    handleConfirm = { duration = it },
                    handleDismiss = { displayModal = false }
                )
            }
        }
    }
}
