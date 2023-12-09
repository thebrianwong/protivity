package com.thebrianwong.protivity.views

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.thebrianwong.protivity.composables.Timer

@Composable
fun Home() {
    Scaffold {
        Text(text = "", modifier = Modifier.padding(it))
        Timer(duration = 360000 * 1000)
    }
}
