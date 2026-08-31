package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

enum class WinterStage(
    val stageName: String,
    val description: String,
    val icon: String,
    val startRatio: Float,
    val endRatio: Float
) {
    SNOWY_FOREST("Snowy Forest", "The descent into the cold begins.", "🌨️", 0f, 0.22f),
    FROZEN_MOUNTAINS("Frozen Mountains", "Scaling the icy ridge of discipline.", "🏔️", 0.22f, 0.44f),
    FROZEN_NIGHT("Frozen Night", "Forging unbreakable focus in the dark.", "🌌", 0.44f, 0.66f),
    MOUNTAIN_CAMP("Mountain Camp", "The fire within burns hotter than the cold.", "🔥", 0.66f, 0.88f),
    SUNRISE_SUMMIT("Sunrise Summit", "Standing at the peak transformed.", "🌅", 0.88f, 1.0f);

    fun getDayRange(totalDays: Int): Pair<Int, Int> {
        val total = maxOf(totalDays, 1)
        val min = if (this == SNOWY_FOREST) 1 else ((startRatio * total).toInt() + 1).coerceIn(1, total)
        val max = if (this == SUNRISE_SUMMIT) total else (endRatio * total).toInt().coerceIn(min, total)
        return Pair(min, max)
    }

    companion object {
        fun fromDay(day: Int, totalDays: Int = 90): WinterStage {
            val total = maxOf(totalDays, 1)
            val ratio = (day.toFloat() / total.toFloat()).coerceIn(0f, 1f)
            return when {
                ratio <= 0.22f -> SNOWY_FOREST
                ratio <= 0.44f -> FROZEN_MOUNTAINS
                ratio <= 0.66f -> FROZEN_NIGHT
                ratio <= 0.88f -> MOUNTAIN_CAMP
                else -> SUNRISE_SUMMIT
            }
        }
    }
}

@Composable
fun WinterEnvironmentBanner(
    currentDay: Int,
    totalDays: Int = 90,
    modifier: Modifier = Modifier
) {
    val stage = WinterStage.fromDay(currentDay, totalDays)
    val (minDay, maxDay) = stage.getDayRange(totalDays)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(130.dp)
            .clip(RoundedCornerShape(24.dp))
    ) {
        // Dynamic procedural background Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            when (stage) {
                WinterStage.SNOWY_FOREST -> {
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFF091428), Color(0xFF0D223A), Color(0xFF08121E))
                        )
                    )
                    // Pine tree silhouettes
                    val treePath = Path().apply {
                        moveTo(0f, h)
                        lineTo(w * 0.1f, h * 0.4f)
                        lineTo(w * 0.2f, h)
                        lineTo(w * 0.35f, h * 0.3f)
                        lineTo(w * 0.5f, h)
                        lineTo(w * 0.65f, h * 0.45f)
                        lineTo(w * 0.8f, h)
                        lineTo(w * 0.9f, h * 0.35f)
                        lineTo(w, h)
                        close()
                    }
                    drawPath(treePath, Color(0x33102A45))
                }
                WinterStage.FROZEN_MOUNTAINS -> {
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFF071224), Color(0xFF0A2540), Color(0xFF050B14))
                        )
                    )
                    // Aurora glow
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0x3038BDF8), Color.Transparent),
                            center = Offset(w * 0.7f, h * 0.2f),
                            radius = w * 0.6f
                        )
                    )
                    // Jagged peak
                    val mountainPath = Path().apply {
                        moveTo(0f, h)
                        lineTo(w * 0.25f, h * 0.25f)
                        lineTo(w * 0.45f, h * 0.6f)
                        lineTo(w * 0.75f, h * 0.15f)
                        lineTo(w, h)
                        close()
                    }
                    drawPath(mountainPath, Color(0x3D38BDF8))
                }
                WinterStage.FROZEN_NIGHT -> {
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFF050814), Color(0xFF0B142B), Color(0xFF03050A))
                        )
                    )
                    // Deep cosmic glow
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0x33818CF8), Color.Transparent),
                            center = Offset(w * 0.5f, h * 0.3f),
                            radius = w * 0.5f
                        )
                    )
                }
                WinterStage.MOUNTAIN_CAMP -> {
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFF0E1322), Color(0xFF1F1A28), Color(0xFF0B0E18))
                        )
                    )
                    // Campfire warm glow
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0x55F97316), Color.Transparent),
                            center = Offset(w * 0.8f, h * 0.75f),
                            radius = w * 0.4f
                        )
                    )
                }
                WinterStage.SUNRISE_SUMMIT -> {
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFF1E1B38), Color(0xFF33203E), Color(0xFF140D22))
                        )
                    )
                    // Golden ice sunrise glow
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0x66FBBF24), Color(0x33F43F5E), Color.Transparent),
                            center = Offset(w * 0.5f, h * 0.1f),
                            radius = w * 0.7f
                        )
                    )
                }
            }
        }

        // Snowflakes overlay
        SnowfallEffect(snowflakeCount = 20)

        // Glassmorphic Info Banner Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color(0xCC070B14))
                    )
                )
                .padding(16.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stage.icon,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(end = 6.dp)
                        )
                        Text(
                            text = "STAGE ${stage.ordinal + 1}: ${stage.stageName.uppercase()}",
                            style = Typography.labelLarge,
                            color = IceWhite,
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp,
                            letterSpacing = 1.sp
                        )
                    }
                    Text(
                        text = stage.description,
                        style = Typography.bodyMedium,
                        color = IceCyanLight,
                        fontSize = 13.sp
                    )
                }

                Text(
                    text = "DAYS $minDay–$maxDay",
                    style = Typography.labelSmall,
                    color = IceWhite,
                    fontWeight = FontWeight.Black,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0x550284C7))
                        .border(1.dp, IceCyanPrimary, RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }
    }
}
