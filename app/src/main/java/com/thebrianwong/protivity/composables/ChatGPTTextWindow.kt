package com.thebrianwong.protivity.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateSizeAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thebrianwong.protivity.viewModels.ChatGPTViewModel

@Composable
fun ChatGPTTextWindow(viewModel: ChatGPTViewModel) {
    val textColor by animateColorAsState(
        targetValue = if (viewModel.currentText.value == "Break's Over!") Color.Red else Color.Black,
        label = "ChatGPT Text Color Animation."
    )
    val textSize by animateFloatAsState(
        targetValue = if (viewModel.currentText.value == "Break's Over!") 32f else LocalTextStyle.current.fontSize.value,
        label = "ChatGPT Text Size Animation."
    )
    val scrollState = rememberScrollState()

    LaunchedEffect(viewModel.currentText.value) {
        scrollState.scrollTo(0)
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = viewModel.currentText.value,
            color = textColor,
            fontSize = textSize.sp,
            modifier = Modifier
                .verticalScroll(scrollState)
                .clip(RoundedCornerShape(20.dp))
                .fillMaxSize()
                .padding(8.dp),
            textAlign = TextAlign.Center
        )
    }
}
