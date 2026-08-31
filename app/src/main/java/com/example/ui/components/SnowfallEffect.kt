package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

data class Snowflake(
    var xRatio: Float,
    var yRatio: Float,
    val radius: Float,
    val speed: Float,
    val swayAmplitude: Float,
    val swayPhase: Float,
    val alpha: Float
)

@Composable
fun SnowfallEffect(
    modifier: Modifier = Modifier,
    snowflakeCount: Int = 45,
    snowColor: Color = Color.White
) {
    val infiniteTransition = rememberInfiniteTransition(label = "snowfall")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "time"
    )

    val snowflakes = remember {
        List(snowflakeCount) {
            Snowflake(
                xRatio = Random.nextFloat(),
                yRatio = Random.nextFloat(),
                radius = Random.nextFloat() * 2.5f + 1f,
                speed = Random.nextFloat() * 0.4f + 0.2f,
                swayAmplitude = Random.nextFloat() * 0.05f + 0.02f,
                swayPhase = Random.nextFloat() * (Math.PI.toFloat() * 2f),
                alpha = Random.nextFloat() * 0.6f + 0.2f
            )
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        snowflakes.forEach { flake ->
            // Calculate current y based on time and individual speed
            val currentYRatio = (flake.yRatio + time * flake.speed) % 1f
            // Sway horizontally with sine wave
            val sway = kotlin.math.sin(time * 6.28f + flake.swayPhase) * flake.swayAmplitude
            val currentXRatio = (flake.xRatio + sway).coerceIn(0f, 1f)

            val x = currentXRatio * width
            val y = currentYRatio * height

            drawCircle(
                color = snowColor.copy(alpha = flake.alpha),
                radius = flake.radius,
                center = Offset(x, y)
            )
        }
    }
}
