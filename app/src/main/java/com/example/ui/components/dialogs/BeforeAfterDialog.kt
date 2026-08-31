package com.example.ui.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.local.entity.WinterArcEntity
import com.example.ui.components.FrostedCard
import com.example.ui.theme.*

@Composable
fun BeforeAfterDialog(
    arc: WinterArcEntity?,
    currentDay: Int,
    totalDays: Int = 90,
    completedGoalsCount: Int,
    totalXp: Int,
    onDismiss: () -> Unit
) {
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
            border = androidx.compose.foundation.BorderStroke(1.dp, IceCyanPrimary)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "BEFORE VS AFTER",
                            style = Typography.titleLarge,
                            color = IceWhite,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = "DAY 1  →  DAY $currentDay (OF $totalDays)",
                            style = Typography.labelMedium,
                            color = IceCyanLight,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
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
                    // Motivation Header Card
                    FrostedCard(modifier = Modifier.fillMaxWidth()) {
                        Column {
                            Text(
                                text = "CORE OBJECTIVE & MOTIVATION",
                                style = Typography.labelSmall,
                                color = IceCyanPrimary,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "“${arc?.motivation ?: "Become more disciplined and master my habits."}”",
                                style = Typography.bodyLarge,
                                color = IceWhite,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                            )
                        }
                    }

                    // Comparison Cards: Day 1 Baseline vs Current Progress
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Day 1 Baseline
                        FrostedCard(
                            modifier = Modifier.weight(1f),
                            contentPadding = 12.dp,
                            backgroundColor = Color(0x990A1120)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = "DAY 1 BASELINE ❄️",
                                    style = Typography.labelSmall,
                                    color = TextSecondary,
                                    fontWeight = FontWeight.Bold
                                )

                                BaselineItem(label = "Fitness", text = arc?.baselineFitness ?: "Starting routine")
                                BaselineItem(label = "Study", text = arc?.baselineStudy ?: "Deep work starter")
                                BaselineItem(label = "Coding", text = arc?.baselineCoding ?: "Beginner build")
                                BaselineItem(label = "Screen Time", text = arc?.baselineScreenTime ?: "< 45m limit")
                            }
                        }

                        // Day X Current Transformation
                        FrostedCard(
                            modifier = Modifier.weight(1f),
                            contentPadding = 12.dp,
                            backgroundColor = Color(0xCC0F1E38)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = "DAY $currentDay STATUS 🔥",
                                    style = Typography.labelSmall,
                                    color = IceCyanLight,
                                    fontWeight = FontWeight.Bold
                                )

                                BaselineItem(label = "XP Earned", text = "$totalXp XP")
                                BaselineItem(label = "Actions Done", text = "$completedGoalsCount completed")
                                BaselineItem(label = "Days In Arc", text = "$currentDay of $totalDays")
                                BaselineItem(label = "Discipline", text = "Relentless Focus")
                            }
                        }
                    }

                    // Reflection Note
                    if (!arc?.baselineReflection.isNullOrBlank()) {
                        FrostedCard(modifier = Modifier.fillMaxWidth()) {
                            Column {
                                Text(
                                    text = "DAY 1 STARTING REFLECTION",
                                    style = Typography.labelSmall,
                                    color = IceCyanLight,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = arc?.baselineReflection ?: "",
                                    style = Typography.bodyMedium,
                                    color = TextPrimary
                                )
                            }
                        }
                    }

                    // Motivational quote
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(Color(0x3338BDF8), Color(0x1F818CF8))
                                )
                            )
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "“This isn't about becoming perfect. It's about becoming better than the person you were yesterday.”",
                            style = Typography.bodyMedium,
                            color = IceWhite,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BaselineItem(label: String, text: String) {
    Column {
        Text(text = label, style = Typography.labelSmall, color = IceCyanLight, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        Text(text = text, style = Typography.bodyMedium, color = IceWhite, fontSize = 13.sp, maxLines = 2)
    }
}
