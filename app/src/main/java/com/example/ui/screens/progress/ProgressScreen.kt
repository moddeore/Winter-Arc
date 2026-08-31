package com.example.ui.screens.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.FrostedCard
import com.example.ui.components.GlowingBorderCard
import com.example.ui.components.GlowingProgressBar
import com.example.ui.components.SnowfallEffect
import com.example.ui.theme.*
import com.example.ui.viewmodel.WinterArcUiState

@Composable
fun ProgressScreen(
    uiState: WinterArcUiState,
    onOpenBeforeAfter: () -> Unit
) {
    val totalDays = uiState.totalDays
    val currentDay = uiState.currentDay
    val daysRemaining = (totalDays - currentDay).coerceAtLeast(0)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
    ) {
        SnowfallEffect(snowflakeCount = 25)

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Header
            item {
                Column {
                    Text(
                        text = "YOUR WINTER ARC 📊",
                        style = Typography.displayMedium,
                        color = IceWhite,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Real-time analytics and ${uiState.totalDays}-day frozen progression",
                        style = Typography.bodyMedium,
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }

            // Cinematic Level & XP Header
            item {
                GlowingBorderCard(
                    modifier = Modifier.fillMaxWidth(),
                    glowColor = IceCyanPrimary,
                    contentPadding = 18.dp
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "LEVEL ${uiState.userProfile?.level ?: 1}",
                                    style = Typography.titleLarge,
                                    color = IceWhite,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 22.sp
                                )
                                Text(
                                    text = "${uiState.userProfile?.totalXp ?: 0} Total XP Earned",
                                    style = Typography.labelSmall,
                                    color = IceCyanLight,
                                    fontSize = 13.sp
                                )
                            }

                            Text(
                                text = "❄️ DAY $currentDay / $totalDays",
                                style = Typography.labelLarge,
                                color = IceWhite,
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0x550284C7))
                                    .border(1.dp, IceCyanPrimary, RoundedCornerShape(10.dp))
                                    .padding(horizontal = 12.dp, vertical = 7.dp)
                            )
                        }

                        GlowingProgressBar(
                            progress = uiState.xpInLevel.toFloat() / uiState.xpNeededForNext.toFloat(),
                            height = 10.dp,
                            showPercentage = true
                        )
                    }
                }
            }

            // 6-Card Overview Stats Grid
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OverviewCard(
                            emoji = "🔥",
                            label = "CURRENT STREAK",
                            value = "${uiState.userProfile?.currentStreak ?: 0} Days",
                            modifier = Modifier.weight(1f)
                        )
                        OverviewCard(
                            emoji = "⚡",
                            label = "BEST STREAK",
                            value = "${uiState.userProfile?.bestStreak ?: 0} Days",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OverviewCard(
                            emoji = "🎯",
                            label = "COMPLETION RATE",
                            value = "${uiState.completionRate}%",
                            modifier = Modifier.weight(1f)
                        )
                        OverviewCard(
                            emoji = "⏳",
                            label = "DAYS REMAINING",
                            value = "$daysRemaining Days",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Custom-Day Challenge Visual Calendar
            item {
                FrostedCard(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = 16.dp
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "${uiState.totalDays}-DAY FROZEN CALENDAR",
                                    style = Typography.titleMedium,
                                    color = IceWhite,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Completed check-in days freeze icy blue",
                                    style = Typography.labelSmall,
                                    color = TextSecondary
                                )
                            }

                            Text(
                                text = "${uiState.checkIns.size} / ${uiState.totalDays} Locked",
                                style = Typography.labelSmall,
                                color = IceCyanPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Challenge Grid (10 columns, scrollable if long)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 180.dp, max = 260.dp)
                        ) {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(10),
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                items(uiState.totalDays) { index ->
                                    val dayIndex = index + 1
                                    val isPassed = dayIndex <= currentDay
                                    val isCurrent = dayIndex == currentDay
                                    val hasCheckIn = uiState.checkIns.any { checkIn ->
                                        dayIndex <= uiState.checkIns.size
                                    }

                                    Box(
                                        modifier = Modifier
                                            .aspectRatio(1f)
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(
                                                when {
                                                    isCurrent -> Color(0xFF38BDF8)
                                                    hasCheckIn && isPassed -> Color(0xFF0284C7)
                                                    isPassed -> Color(0x5538BDF8)
                                                    else -> Color(0x1F1E293B)
                                                }
                                            )
                                            .border(
                                                1.dp,
                                                if (isCurrent) IceWhite else if (isPassed) IceCyanPrimary else Color.Transparent,
                                                RoundedCornerShape(6.dp)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "$dayIndex",
                                            style = Typography.labelSmall,
                                            fontSize = 11.sp,
                                            color = when {
                                                isCurrent -> DarkBg
                                                isPassed -> IceWhite
                                                else -> TextMuted
                                            },
                                            fontWeight = if (isCurrent || isPassed) FontWeight.Black else FontWeight.Medium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Dynamic Category Progress Bars
            item {
                FrostedCard(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = 16.dp
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text(
                            text = "CATEGORY PROGRESSION",
                            style = Typography.titleMedium,
                            color = IceWhite,
                            fontWeight = FontWeight.Bold
                        )

                        if (uiState.categoryProgressList.isEmpty()) {
                            Text(
                                text = "Create goals in categories to view real-time breakdown.",
                                style = Typography.bodyMedium,
                                color = TextSecondary
                            )
                        } else {
                            uiState.categoryProgressList.forEach { catProg ->
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = catProg.category.icon,
                                                fontSize = 16.sp,
                                                modifier = Modifier.padding(end = 6.dp)
                                            )
                                            Text(
                                                text = catProg.category.name,
                                                style = Typography.titleMedium,
                                                color = IceWhite,
                                                fontSize = 13.sp
                                            )
                                        }

                                        Text(
                                            text = "${catProg.percentage}%",
                                            style = Typography.labelMedium,
                                            color = IceCyanLight,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    GlowingProgressBar(
                                        progress = catProg.percentage.toFloat() / 100f,
                                        height = 6.dp,
                                        showPercentage = false
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Before vs After Card CTA
            item {
                GlowingBorderCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenBeforeAfter() },
                    glowColor = FrostAccent,
                    contentPadding = 16.dp
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🏔️", fontSize = 28.sp, modifier = Modifier.padding(end = 12.dp))
                            Column {
                                Text(
                                    text = "BEFORE VS AFTER COMPARISON",
                                    style = Typography.titleMedium,
                                    color = IceWhite,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Day 1 Baseline vs Day $currentDay Transformation",
                                    style = Typography.bodyMedium,
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        Text(
                            text = "View",
                            style = Typography.labelMedium,
                            color = IceCyanPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OverviewCard(
    emoji: String,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    FrostedCard(
        modifier = modifier,
        contentPadding = 14.dp
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = emoji, fontSize = 16.sp, modifier = Modifier.padding(end = 6.dp))
                Text(
                    text = label,
                    style = Typography.labelSmall,
                    color = TextSecondary,
                    fontSize = 10.sp
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                style = Typography.titleMedium,
                color = IceWhite,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}
