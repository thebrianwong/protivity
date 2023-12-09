package com.thebrianwong.protivity.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun FloatingActionButton(handleClick: () -> Unit) {
    IconButton(
        onClick = { handleClick() },
        modifier = Modifier.background(MaterialTheme.colorScheme.primary, CircleShape)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add New Timer.",
            tint = Color.White
        )
    }
}
