package com.thebrianwong.protivity.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.thebrianwong.protivity.viewModels.ChatGPTViewModel

@Composable
fun ChatGPTTextWindow(viewModel: ChatGPTViewModel) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = viewModel.generatedText.value,
            modifier = Modifier
                .verticalScroll(
                    rememberScrollState()
                )
                .clip(RoundedCornerShape(20.dp))
                .fillMaxSize()
                .padding(8.dp),
            textAlign = TextAlign.Center
        )
    }
}
