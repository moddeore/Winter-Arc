package com.example.ui.screens.achievements

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.data.local.entity.AchievementEntity
import com.example.ui.components.FrostedCard
import com.example.ui.components.GlowingBorderCard
import com.example.ui.components.GlowingProgressBar
import com.example.ui.components.SnowfallEffect
import com.example.ui.theme.*
import com.example.ui.viewmodel.WinterArcUiState

@Composable
fun AchievementsScreen(
    uiState: WinterArcUiState
) {
    val level = uiState.userProfile?.level ?: 1
    val rankTitle = when {
        level >= 20 -> "Summit Master 🏔️"
        level >= 15 -> "Frozen Titan ⚡"
        level >= 10 -> "Arc Sentinel ⚔️"
        level >= 5 -> "Glacier Scout ❄️"
        else -> "Frost Novice 🌨️"
    }

    val chunkedAchievements = uiState.achievements.chunked(2)

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
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Header
            item {
                Column {
                    Text(
                        text = "ACHIEVEMENTS & RANKS 🏆",
                        style = Typography.displayMedium,
                        color = IceWhite,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Unlock badges and level up your discipline",
                        style = Typography.bodyMedium,
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }

            // Level & Rank Hero Banner
            item {
                GlowingBorderCard(
                    modifier = Modifier.fillMaxWidth(),
                    glowColor = FrostGold,
                    backgroundColor = Color(0xDD0D1527),
                    contentPadding = 20.dp
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "WARRIOR RANK",
                                    style = Typography.labelSmall,
                                    color = FrostGold,
                                    letterSpacing = 1.5.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = rankTitle,
                                    style = Typography.headlineMedium,
                                    color = IceWhite,
                                    fontWeight = FontWeight.Black
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.radialGradient(
                                            colors = listOf(Color(0x66FBBF24), Color(0x22FBBF24))
                                        )
                                    )
                                    .border(1.dp, FrostGold, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "LVL\n$level",
                                    style = Typography.labelSmall,
                                    color = IceWhite,
                                    fontWeight = FontWeight.Black,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                    lineHeight = 12.sp
                                )
                            }
                        }

                        GlowingProgressBar(
                            progress = uiState.xpInLevel.toFloat() / uiState.xpNeededForNext.toFloat(),
                            glowGradient = listOf(FrostGold, Color(0xFFF59E0B), Color(0xFFD97706)),
                            height = 8.dp
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${uiState.userProfile?.totalXp ?: 0} Total XP",
                                style = Typography.labelSmall,
                                color = IceCyanLight
                            )
                            Text(
                                text = "Next Rank: Level ${level + 1}",
                                style = Typography.labelSmall,
                                color = TextMuted
                            )
                        }
                    }
                }
            }

            // Badges Section Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "CHALLENGE BADGES",
                        style = Typography.titleMedium,
                        color = IceWhite,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${uiState.achievements.count { it.isUnlocked }} / ${uiState.achievements.size} Unlocked",
                        style = Typography.labelSmall,
                        color = IceCyanPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Badges Grid
            items(chunkedAchievements) { pair ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    pair.forEach { ach ->
                        AchievementBadgeCard(
                            achievement = ach,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (pair.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun AchievementBadgeCard(
    achievement: AchievementEntity,
    modifier: Modifier = Modifier
) {
    val isUnlocked = achievement.isUnlocked

    FrostedCard(
        modifier = modifier.height(180.dp),
        backgroundColor = if (isUnlocked) Color(0xCC0E1D36) else Color(0x44080F1D),
        borderColor = if (isUnlocked) IceCyanPrimary else DarkCardBorder,
        contentPadding = 14.dp
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isUnlocked) Color(0x3338BDF8) else Color(0x221E293B))
                        .border(
                            1.dp,
                            if (isUnlocked) IceCyanPrimary else Color(0x3364748B),
                            RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isUnlocked) achievement.icon else "🔒",
                        fontSize = 22.sp
                    )
                }

                Text(
                    text = "+${achievement.xpReward} XP",
                    style = Typography.labelSmall,
                    color = if (isUnlocked) IceCyanLight else TextMuted,
                    fontWeight = FontWeight.Bold
                )
            }

            Column {
                Text(
                    text = achievement.title,
                    style = Typography.titleMedium,
                    color = if (isUnlocked) IceWhite else TextSecondary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = achievement.description,
                    style = Typography.bodyMedium,
                    color = if (isUnlocked) IceCyanLight.copy(alpha = 0.8f) else TextMuted,
                    fontSize = 12.sp,
                    maxLines = 3
                )
            }

            Text(
                text = if (isUnlocked) "✓ UNLOCKED" else "LOCKED",
                style = Typography.labelSmall,
                color = if (isUnlocked) IceCyanLight else TextMuted,
                fontWeight = FontWeight.Black,
                fontSize = 11.sp,
                letterSpacing = 1.sp
            )
        }
    }
}
