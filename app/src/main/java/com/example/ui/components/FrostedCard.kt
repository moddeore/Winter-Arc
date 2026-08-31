package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.*

@Composable
fun FrostedCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(24.dp),
    borderColor: Color = Color(0x1FFFFFFF),
    backgroundColor: Color = Color(0x14BAE6FD),
    contentPadding: Dp = 16.dp,
    content: @Composable BoxScope.() -> Unit
) {
    Surface(
        modifier = modifier.clip(shape),
        shape = shape,
        color = backgroundColor,
        border = BorderStroke(1.dp, borderColor),
        tonalElevation = 0.dp
    ) {
        Box(
            modifier = Modifier.padding(contentPadding),
            content = content
        )
    }
}

@Composable
fun GlowingBorderCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(24.dp),
    glowColor: Color = IceCyanPrimary,
    backgroundColor: Color = Color(0x1F0F172A),
    contentPadding: Dp = 16.dp,
    content: @Composable BoxScope.() -> Unit
) {
    val borderBrush = Brush.linearGradient(
        colors = listOf(
            glowColor.copy(alpha = 0.5f),
            glowColor.copy(alpha = 0.1f),
            glowColor.copy(alpha = 0.35f)
        )
    )

    Surface(
        modifier = modifier.clip(shape),
        shape = shape,
        color = backgroundColor,
        border = BorderStroke(1.dp, borderBrush),
        tonalElevation = 0.dp
    ) {
        Box(
            modifier = Modifier.padding(contentPadding),
            content = content
        )
    }
}

