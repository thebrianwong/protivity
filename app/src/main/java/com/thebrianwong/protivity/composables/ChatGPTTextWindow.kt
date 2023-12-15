package com.thebrianwong.protivity.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ChatGPTTextWindow() {
    Box(modifier = Modifier.clip(RoundedCornerShape(20.dp))) {
        Text(
            text = "INSERT CHATGPT HERE INSERT CHATGPT HERE INSERT CHATGPT HERE INSERT CHATGPT HERE INSERT CHATGPT HERE INSERT CHATGPT HERE INSERT CHATGPT HERE INSERT CHATGPT HERE INSERT CHATGPT HERE ",
            modifier = Modifier
                .verticalScroll(
                    rememberScrollState()
                )
                .background(MaterialTheme.colorScheme.tertiaryContainer)
                .padding(8.dp),
            textAlign = TextAlign.Justify
        )
    }
}
