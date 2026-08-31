package com.example.ui.screens.goals

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.CategoryEntity
import com.example.data.local.entity.GoalEntity
import com.example.ui.components.FrostedCard
import com.example.ui.components.SnowfallEffect
import com.example.ui.theme.*
import com.example.ui.viewmodel.WinterArcUiState

@Composable
fun GoalsScreen(
    uiState: WinterArcUiState,
    onGoalClick: (GoalEntity) -> Unit,
    onCreateGoalClick: () -> Unit,
    onCreateCategoryClick: () -> Unit,
    onTogglePause: (goalId: Long, isPaused: Boolean) -> Unit
) {
    var selectedCategoryId by remember { mutableStateOf<Long?>(null) } // null for All

    val filteredGoals = remember(uiState.goals, selectedCategoryId) {
        if (selectedCategoryId == null) {
            uiState.goals
        } else {
            uiState.goals.filter { it.categoryId == selectedCategoryId }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
    ) {
        SnowfallEffect(snowflakeCount = 20)

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    ) {
                        Text(
                            text = "CUSTOM GOALS 🎯",
                            style = Typography.titleLarge,
                            color = IceWhite,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "Tailor every habit & target",
                            style = Typography.bodyMedium,
                            color = TextSecondary,
                            fontSize = 13.sp
                        )
                    }

                    Button(
                        onClick = onCreateGoalClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = IceCyanPrimary,
                            contentColor = DarkBg
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                        modifier = Modifier.defaultMinSize(minWidth = 100.dp, minHeight = 40.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Add Goal",
                                modifier = Modifier.size(18.dp),
                                tint = DarkBg
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "New Goal",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = DarkBg
                            )
                        }
                    }
                }
            }

            // Category Filter Chips
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        val isSelected = selectedCategoryId == null
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) Color(0x330284C7) else Color(0x1F0F172A))
                                .border(
                                    1.dp,
                                    if (isSelected) IceCyanPrimary else Color(0x1FFFFFFF),
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { selectedCategoryId = null }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "All (${uiState.goals.size})",
                                color = if (isSelected) IceWhite else TextSecondary,
                                style = Typography.titleMedium,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 14.sp
                            )
                        }
                    }

                    items(uiState.categories) { cat ->
                        val isSelected = selectedCategoryId == cat.id
                        val count = uiState.goals.count { it.categoryId == cat.id }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) Color(0x330284C7) else Color(0x1F0F172A))
                                .border(
                                    1.dp,
                                    if (isSelected) IceCyanPrimary else Color(0x1FFFFFFF),
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { selectedCategoryId = cat.id }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = cat.icon, modifier = Modifier.padding(end = 4.dp), fontSize = 14.sp)
                                Text(
                                    text = "${cat.name} ($count)",
                                    color = if (isSelected) IceWhite else TextSecondary,
                                    style = Typography.titleMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }

                    // Add Custom Category button chip
                    item {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x1A818CF8))
                                .border(1.dp, Color(0x4D818CF8), RoundedCornerShape(12.dp))
                                .clickable { onCreateCategoryClick() }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "＋ Category",
                                color = FrostAccent,
                                style = Typography.titleMedium,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Goals List
            if (filteredGoals.isEmpty()) {
                item {
                    FrostedCard(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = 24.dp
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("❄️", fontSize = 36.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "No Goals in this category",
                                style = Typography.titleMedium,
                                color = IceWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Create your first winter habit to start earning XP",
                                style = Typography.bodyMedium,
                                color = TextSecondary,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = onCreateGoalClick,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = IceCyanPrimary,
                                    contentColor = DarkBg
                                ),
                                shape = RoundedCornerShape(12.dp),
                                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        Icons.Default.Add,
                                        contentDescription = null,
                                        tint = DarkBg,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Create Custom Goal",
                                        fontWeight = FontWeight.Black,
                                        fontSize = 14.sp,
                                        color = DarkBg
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                items(filteredGoals, key = { it.id }) { goal ->
                    GoalListCard(
                        goal = goal,
                        onClick = { onGoalClick(goal) },
                        onTogglePause = { onTogglePause(goal.id, !goal.isPaused) }
                    )
                }
            }
        }
    }
}

@Composable
fun GoalListCard(
    goal: GoalEntity,
    onClick: () -> Unit,
    onTogglePause: () -> Unit
) {
    FrostedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        backgroundColor = if (goal.isPaused) Color(0x660B1220) else FrostCardGlass,
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
                    Text(
                        text = goal.categoryIcon,
                        fontSize = 22.sp,
                        modifier = Modifier.padding(end = 10.dp)
                    )
                    Column {
                        Text(
                            text = goal.name,
                            style = Typography.titleMedium,
                            color = if (goal.isPaused) TextMuted else IceWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "${goal.categoryName} • ${goal.goalType.displayName}",
                            style = Typography.labelSmall,
                            color = IceCyanLight,
                            fontSize = 13.sp
                        )
                    }
                }

                // Pause / Active Badge
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (goal.isPaused) {
                        Text(
                            text = "PAUSED",
                            style = Typography.labelSmall,
                            color = TextSecondary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0x3364748B))
                                .border(1.dp, Color(0x5564748B), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    } else {
                        val (bgColor, borderColor, textColor) = when (goal.difficulty) {
                            com.example.data.local.entity.GoalDifficulty.EASY -> Triple(Color(0x2410B981), Color(0x6634D399), Color(0xFF6EE7B7))
                            com.example.data.local.entity.GoalDifficulty.MEDIUM -> Triple(Color(0x240284C7), Color(0x6638BDF8), Color(0xFFBAE6FD))
                            com.example.data.local.entity.GoalDifficulty.HARD -> Triple(Color(0x2EF59E0B), Color(0x66FBBF24), Color(0xFFFDE68A))
                            com.example.data.local.entity.GoalDifficulty.MAJOR -> Triple(Color(0x2EF43F5E), Color(0x66FB7185), Color(0xFFFFE4E6))
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(bgColor)
                                .border(1.dp, borderColor, RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "+${goal.difficulty.xpReward} XP",
                                style = Typography.labelSmall,
                                color = textColor,
                                fontWeight = FontWeight.Black,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            if (goal.description.isNotBlank()) {
                Text(
                    text = goal.description,
                    style = Typography.bodyMedium,
                    color = TextSecondary,
                    fontSize = 12.sp,
                    maxLines = 2
                )
            }

            // Target Details and Streaks
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Target: ${goal.targetValue.toInt()} ${goal.targetUnit} (${goal.targetFrequency})",
                    style = Typography.labelMedium,
                    color = TextSecondary,
                    fontSize = 12.sp
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🔥", fontSize = 12.sp)
                    Text(
                        text = "${goal.currentStreak}d streak",
                        style = Typography.labelSmall,
                        color = FrostGold,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 2.dp)
                    )
                }
            }
        }
    }
}
