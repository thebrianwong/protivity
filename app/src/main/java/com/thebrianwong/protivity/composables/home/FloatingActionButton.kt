package com.thebrianwong.protivity.composables.home

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FloatingActionButton(newTimer: Boolean, handleClick: () -> Unit) {
    IconButton(
        onClick = { handleClick() },
        modifier = Modifier
            .shadow(elevation = 8.dp, shape = CircleShape)
            .background(MaterialTheme.colorScheme.primary, CircleShape)
    ) {
        Icon(
            imageVector = if (newTimer) Icons.Default.Add else Icons.Default.Edit,
            contentDescription = "Add New Timer.",
            tint = Color.White
        )
    }
}
