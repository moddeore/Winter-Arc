package com.example.ui.screens.home

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.GoalEntity
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.GoalWithProgress
import com.example.ui.viewmodel.WinterArcUiState

@Composable
fun HomeScreen(
    uiState: WinterArcUiState,
    onGoalClick: (GoalEntity) -> Unit,
    onToggleGoalComplete: (Long) -> Unit,
    onIncrementGoalProgress: (goalId: Long, increment: Float) -> Unit,
    onAddGoalClick: () -> Unit,
    onOpenCheckIn: () -> Unit
) {
    val level = uiState.userProfile?.level ?: 1
    val currentStreak = uiState.userProfile?.currentStreak ?: 0
    val totalXp = uiState.userProfile?.totalXp ?: 0
    val completedCount = uiState.goalsWithProgress.count { it.todayProgress?.isCompleted == true }
    val totalGoalsCount = uiState.goalsWithProgress.size

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
    ) {
        // Radial / vertical ambient background glow
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0x2E0E7490),
                            Color(0x141E293B),
                            Color.Transparent
                        )
                    )
                )
        )

        SnowfallEffect(snowflakeCount = 24)

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(top = 20.dp, bottom = 110.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Editorial Header Bar
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text(
                            text = (uiState.activeArc?.name ?: "WINTER ARC 2026").uppercase(),
                            style = Typography.labelSmall,
                            color = IceCyanPrimary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 3.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Stay locked in.",
                            fontFamily = FontFamily.Serif,
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Light,
                            fontSize = 28.sp,
                            color = IceWhite,
                            letterSpacing = (-0.5).sp
                        )
                    }

                    // Editorial Level Pill Badge with Mini Progress Track
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0x2E0284C7))
                            .border(1.dp, IceCyanPrimary, RoundedCornerShape(20.dp))
                            .padding(horizontal = 14.dp, vertical = 7.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "LVL $level",
                                style = Typography.labelMedium,
                                color = IceWhite,
                                fontWeight = FontWeight.Black,
                                fontSize = 13.sp,
                                letterSpacing = 0.5.sp
                            )
                            // Mini horizontal XP progress bar
                            Box(
                                modifier = Modifier
                                    .width(48.dp)
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(Color(0x44FFFFFF))
                            ) {
                                val ratio = (uiState.xpInLevel.toFloat() / uiState.xpNeededForNext.toFloat()).coerceIn(0f, 1f)
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .fillMaxWidth(ratio)
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(IceCyanLight)
                                )
                            }
                        }
                    }
                }
            }

            // Editorial Active Streak Hero Card with Ghost Watermark Number
            item {
                FrostedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    backgroundColor = Color(0x1A0F172A),
                    borderColor = Color(0x2238BDF8),
                    contentPadding = 22.dp
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        // Large Ghost Watermark Number in top right
                        Text(
                            text = "$currentStreak",
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.Black,
                            fontStyle = FontStyle.Italic,
                            fontSize = 88.sp,
                            color = Color(0x0FFFFFFF),
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = 10.dp, y = (-12).dp)
                        )

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Micro label
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "ACTIVE STREAK",
                                    style = Typography.labelSmall,
                                    color = IceCyanLight,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 2.sp
                                )
                            }

                            // Big Streak Numeral
                            Row(
                                verticalAlignment = Alignment.Bottom,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "$currentStreak",
                                    fontSize = 50.sp,
                                    fontWeight = FontWeight.Black,
                                    color = IceWhite,
                                    letterSpacing = (-1.5).sp,
                                    lineHeight = 50.sp
                                )
                                Text(
                                    text = "DAYS",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = TextSecondary,
                                    letterSpacing = 1.sp
                                )
                            }

                            // 7-day consistency glow segments
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                val activeSegments = (currentStreak % 7).let { if (it == 0 && currentStreak > 0) 7 else it }
                                for (i in 1..7) {
                                    val isFilled = i <= activeSegments
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(5.dp)
                                            .clip(RoundedCornerShape(3.dp))
                                            .background(
                                                if (isFilled) Brush.horizontalGradient(
                                                    listOf(IceCyanLight, IceCyanPrimary)
                                                ) else Brush.horizontalGradient(
                                                    listOf(Color(0x1FFFFFFF), Color(0x1FFFFFFF))
                                                )
                                            )
                                    )
                                }
                            }

                            // Bottom metadata row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "$totalXp XP TOTAL",
                                    style = Typography.labelSmall,
                                    color = TextSecondary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.5.sp
                                )

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0x2E0284C7))
                                        .border(1.dp, Color(0x6638BDF8), RoundedCornerShape(8.dp))
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = "DAY ${uiState.currentDay} OF ${uiState.totalDays}",
                                        style = Typography.labelSmall,
                                        color = IceWhite,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 12.sp,
                                        letterSpacing = 1.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Procedural Winter Environment Canvas Banner
            item {
                WinterEnvironmentBanner(
                    currentDay = uiState.currentDay,
                    totalDays = uiState.totalDays
                )
            }

            // Daily Check-In Prompt Card (if not yet checked in)
            if (uiState.todayCheckIn == null) {
                item {
                    GlowingBorderCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenCheckIn() },
                        glowColor = IceCyanPrimary,
                        backgroundColor = Color(0x1F0F172A),
                        shape = RoundedCornerShape(24.dp),
                        contentPadding = 16.dp
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(Color(0x1F38BDF8))
                                        .border(1.dp, Color(0x4438BDF8), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("❄️", fontSize = 18.sp)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "DAY ${uiState.currentDay} CHECK-IN READY",
                                        style = Typography.titleMedium,
                                        color = IceWhite,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                    Text(
                                        text = "Reflect on today's discipline & claim +100 XP",
                                        style = Typography.bodyMedium,
                                        color = IceCyanLight,
                                        fontSize = 13.sp
                                    )
                                }
                            }

                            Icon(
                                Icons.Default.ChevronRight,
                                contentDescription = "Check In",
                                tint = IceCyanPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }
            }

            // Section Header: Today's Objectives (Editorial Styling)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "TODAY'S OBJECTIVES",
                        style = Typography.labelSmall,
                        color = TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )

                    Text(
                        text = "$completedCount / $totalGoalsCount COMPLETED",
                        style = Typography.labelSmall,
                        color = IceCyanPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }

            // Empty state if no goals
            if (uiState.goalsWithProgress.isEmpty()) {
                item {
                    FrostedCard(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = 28.dp
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("🏔️", fontSize = 36.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "No Objectives Active",
                                style = Typography.titleMedium,
                                color = IceWhite,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Create your discipline targets and hold yourself accountable throughout the Arc.",
                                style = Typography.bodyMedium,
                                color = TextSecondary,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            // List of Objectives
            items(uiState.goalsWithProgress, key = { it.goal.id }) { item ->
                EditorialGoalItemCard(
                    goalWithProgress = item,
                    onClick = { onGoalClick(item.goal) },
                    onToggleComplete = { onToggleGoalComplete(item.goal.id) },
                    onIncrement = { inc -> onIncrementGoalProgress(item.goal.id, inc) }
                )
            }

            // Editorial Dashed Add Goal Button
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onAddGoalClick() },
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0x08FFFFFF),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        Color(0x1FFFFFFF)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 14.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Add Goal",
                            tint = TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ADD OBJECTIVE",
                            style = Typography.labelSmall,
                            color = TextSecondary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            letterSpacing = 2.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EditorialGoalItemCard(
    goalWithProgress: GoalWithProgress,
    onClick: () -> Unit,
    onToggleComplete: () -> Unit,
    onIncrement: (Float) -> Unit
) {
    val goal = goalWithProgress.goal
    val progress = goalWithProgress.todayProgress
    val isCompleted = progress?.isCompleted == true
    val currentProg = progress?.currentProgress ?: 0f
    val targetVal = goal.targetValue.coerceAtLeast(0.1f)
    val pct = (currentProg / targetVal).coerceIn(0f, 1f)

    FrostedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        backgroundColor = if (isCompleted) Color(0x0CBAE6FD) else Color(0x14BAE6FD),
        borderColor = if (isCompleted) Color(0x1F38BDF8) else Color(0x14FFFFFF),
        contentPadding = 16.dp
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    // Checkbox button or completed circle
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) Color(0x3338BDF8) else Color(0x14FFFFFF))
                            .border(
                                1.dp,
                                if (isCompleted) IceCyanPrimary else Color(0x22FFFFFF),
                                CircleShape
                            )
                            .clickable { onToggleComplete() },
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = "Completed",
                                tint = IceCyanLight,
                                modifier = Modifier.size(16.dp)
                            )
                        } else {
                            Text(
                                text = goal.categoryIcon,
                                fontSize = 14.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = goal.name,
                            style = Typography.titleMedium,
                            color = if (isCompleted) IceWhite.copy(alpha = 0.7f) else IceWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            textDecoration = if (isCompleted) TextDecoration.LineThrough else TextDecoration.None
                        )
                        Text(
                            text = if (isCompleted) "Completed • ${goal.targetFrequency}" else "${currentProg.toInt()} / ${targetVal.toInt()} ${goal.targetUnit} • ${goal.categoryName}",
                            style = Typography.bodyMedium,
                            color = if (isCompleted) TextMuted else TextSecondary,
                            fontSize = 13.sp
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val (xpBg, xpBorder, xpText) = when (goal.difficulty) {
                        com.example.data.local.entity.GoalDifficulty.EASY -> Triple(Color(0x2410B981), Color(0x6634D399), Color(0xFF6EE7B7))
                        com.example.data.local.entity.GoalDifficulty.MEDIUM -> Triple(Color(0x240284C7), Color(0x6638BDF8), Color(0xFFBAE6FD))
                        com.example.data.local.entity.GoalDifficulty.HARD -> Triple(Color(0x2EF59E0B), Color(0x66FBBF24), Color(0xFFFDE68A))
                        com.example.data.local.entity.GoalDifficulty.MAJOR -> Triple(Color(0x2EF43F5E), Color(0x66FB7185), Color(0xFFFFE4E6))
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(xpBg)
                            .border(1.dp, xpBorder, RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "+${goal.difficulty.xpReward} XP",
                            style = Typography.labelSmall,
                            color = xpText,
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp,
                            letterSpacing = 0.5.sp
                        )
                    }

                    if (!isCompleted && goal.targetValue > 1f) {
                        val step = if (goal.targetUnit == "min") 15f else 1f
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0x2E818CF8))
                                .border(1.dp, Color(0x66818CF8), RoundedCornerShape(8.dp))
                                .clickable { onIncrement(step) }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "+${step.toInt()}",
                                style = Typography.labelSmall,
                                color = FrostAccent,
                                fontWeight = FontWeight.Black,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            // Glowing slim progress track
            GlowingProgressBar(
                progress = pct,
                height = 5.dp,
                trackColor = Color(0x14FFFFFF),
                glowGradient = listOf(IceCyanLight, IceCyanPrimary),
                showPercentage = false
            )
        }
    }
}

