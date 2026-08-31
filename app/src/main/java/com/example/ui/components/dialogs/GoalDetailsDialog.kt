package com.example.ui.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.local.entity.DailyProgressEntity
import com.example.data.local.entity.GoalEntity
import com.example.data.local.entity.GoalMilestoneEntity
import com.example.ui.components.FrostedCard
import com.example.ui.components.GlowingProgressBar
import com.example.ui.theme.*

@Composable
fun GoalDetailsDialog(
    goal: GoalEntity,
    todayProgress: DailyProgressEntity?,
    milestones: List<GoalMilestoneEntity> = emptyList(),
    onDismiss: () -> Unit,
    onTogglePause: (Boolean) -> Unit,
    onDelete: () -> Unit,
    onToggleMilestone: (milestoneId: Long, isCompleted: Boolean) -> Unit
) {
    val currentProg = todayProgress?.currentProgress ?: 0f
    val targetVal = goal.targetValue.coerceAtLeast(0.1f)
    val pct = (currentProg / targetVal).coerceIn(0f, 1f)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.85f)
                .clip(RoundedCornerShape(24.dp)),
            color = DarkBg,
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = goal.categoryIcon,
                            fontSize = 24.sp,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Column {
                            Text(
                                text = goal.name,
                                style = Typography.titleLarge,
                                color = IceWhite,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${goal.categoryName} • ${goal.goalType.displayName}",
                                style = Typography.bodyMedium,
                                color = IceCyanLight,
                                fontSize = 12.sp
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (goal.description.isNotBlank()) {
                        FrostedCard(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = 12.dp
                        ) {
                            Text(
                                text = goal.description,
                                style = Typography.bodyMedium,
                                color = TextPrimary
                            )
                        }
                    }

                    // Progress Overview Card
                    FrostedCard(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = 16.dp
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "TODAY'S PROGRESS",
                                    style = Typography.labelSmall,
                                    color = IceCyanLight,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = "${currentProg.toInt()} / ${targetVal.toInt()} ${goal.targetUnit}",
                                    style = Typography.titleMedium,
                                    color = IceWhite,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            GlowingProgressBar(
                                progress = pct,
                                height = 10.dp,
                                showPercentage = true
                            )
                        }
                    }

                    // Stats Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatBox(
                            title = "CURRENT STREAK",
                            value = "${goal.currentStreak} Days",
                            emoji = "🔥",
                            modifier = Modifier.weight(1f)
                        )
                        StatBox(
                            title = "BEST STREAK",
                            value = "${goal.bestStreak} Days",
                            emoji = "⚡",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatBox(
                            title = "XP EARNED",
                            value = "+${goal.totalXpEarned} XP",
                            emoji = "✨",
                            modifier = Modifier.weight(1f)
                        )
                        StatBox(
                            title = "DIFFICULTY",
                            value = goal.difficulty.displayName,
                            emoji = "⚔️",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Milestones List if any
                    if (milestones.isNotEmpty()) {
                        Text(
                            text = "MILESTONES ROADMAP",
                            style = Typography.labelLarge,
                            color = IceCyanLight,
                            letterSpacing = 1.sp
                        )

                        milestones.forEach { ms ->
                            FrostedCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onToggleMilestone(ms.id, !ms.isCompleted) },
                                contentPadding = 12.dp
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(22.dp)
                                                .clip(CircleShape)
                                                .background(if (ms.isCompleted) IceCyanPrimary else Color(0x331E293B))
                                                .border(1.dp, IceCyanPrimary, CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (ms.isCompleted) {
                                                Icon(
                                                    Icons.Default.Check,
                                                    contentDescription = "Completed",
                                                    tint = DarkBg,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = ms.title,
                                            style = Typography.bodyMedium,
                                            color = if (ms.isCompleted) TextMuted else IceWhite
                                        )
                                    }
                                    if (ms.isCompleted) {
                                        Text(
                                            text = "+15 XP",
                                            style = Typography.labelSmall,
                                            color = IceCyanPrimary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = { onTogglePause(!goal.isPaused) },
                        modifier = Modifier.weight(1f).height(44.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = IceCyanPrimary)
                    ) {
                        Icon(
                            if (goal.isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                            contentDescription = "Pause",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (goal.isPaused) "Resume" else "Pause",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    OutlinedButton(
                        onClick = onDelete,
                        modifier = Modifier.weight(1f).height(44.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFEF4444))
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Delete", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun StatBox(
    title: String,
    value: String,
    emoji: String,
    modifier: Modifier = Modifier
) {
    FrostedCard(
        modifier = modifier,
        contentPadding = 12.dp
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = emoji, fontSize = 15.sp, modifier = Modifier.padding(end = 4.dp))
                Text(
                    text = title,
                    style = Typography.labelSmall,
                    color = IceCyanLight,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
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
