package com.example.ui.screens.onboarding

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.FrostedCard
import com.example.ui.components.SnowfallEffect
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    onStartArc: (
        name: String,
        motivation: String,
        mainObjective: String,
        durationDays: Int,
        baselineFitness: String,
        baselineStudy: String,
        baselineCoding: String,
        baselineScreenTime: String,
        baselineReflection: String
    ) -> Unit
) {
    var currentStep by remember { mutableIntStateOf(0) }

    // Step 4 Setup Form State
    var arcName by remember { mutableStateOf("My Winter Arc 2026") }
    var motivation by remember { mutableStateOf("I want to become more disciplined, master my craft, and build relentless focus.") }
    var mainObjective by remember { mutableStateOf("Complete 90 days locked in without breaking momentum.") }
    var durationDays by remember { mutableIntStateOf(90) }

    var baselineFitness by remember { mutableStateOf("Workout 4x/week, build cold morning discipline") }
    var baselineStudy by remember { mutableStateOf("2 hours daily deep work") }
    var baselineCoding by remember { mutableStateOf("Build 3 end-to-end applications") }
    var baselineScreenTime by remember { mutableStateOf("< 45m social media limit") }
    var baselineReflection by remember { mutableStateOf("Ready to transform and stay locked in.") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF060914), Color(0xFF0C162D), Color(0xFF050811))
                )
            )
    ) {
        SnowfallEffect(snowflakeCount = 35)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Progress Indicators
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(4) { index ->
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .height(4.dp)
                            .width(if (index == currentStep) 28.dp else 12.dp)
                            .clip(CircleShape)
                            .background(
                                if (index == currentStep) IceCyanPrimary
                                else if (index < currentStep) FrostSecondary
                                else Color(0x3338BDF8)
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Step Content
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (currentStep) {
                    0 -> OnboardingStep1()
                    1 -> OnboardingStep2()
                    2 -> OnboardingStep3()
                    3 -> OnboardingStep4(
                        arcName = arcName,
                        onArcNameChange = { arcName = it },
                        motivation = motivation,
                        onMotivationChange = { motivation = it },
                        mainObjective = mainObjective,
                        onMainObjectiveChange = { mainObjective = it },
                        durationDays = durationDays,
                        onDurationChange = { durationDays = it },
                        baselineFitness = baselineFitness,
                        onBaselineFitnessChange = { baselineFitness = it },
                        baselineStudy = baselineStudy,
                        onBaselineStudyChange = { baselineStudy = it },
                        baselineCoding = baselineCoding,
                        onBaselineCodingChange = { baselineCoding = it },
                        baselineScreenTime = baselineScreenTime,
                        onBaselineScreenTimeChange = { baselineScreenTime = it },
                        baselineReflection = baselineReflection,
                        onBaselineReflectionChange = { baselineReflection = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Footer Navigation Buttons
            if (currentStep < 3) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (currentStep > 0) {
                        TextButton(onClick = { currentStep-- }) {
                            Text("Back", color = TextSecondary)
                        }
                    } else {
                        Spacer(modifier = Modifier.width(48.dp))
                    }

                    Button(
                        onClick = { currentStep++ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = IceCyanPrimary,
                            contentColor = DarkBg
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.height(48.dp)
                    ) {
                        Text("Next", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Next",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            } else {
                Button(
                    onClick = {
                        onStartArc(
                            arcName,
                            motivation,
                            mainObjective,
                            durationDays,
                            baselineFitness,
                            baselineStudy,
                            baselineCoding,
                            baselineScreenTime,
                            baselineReflection
                        )
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = IceCyanPrimary,
                        contentColor = DarkBg
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                ) {
                    Text(
                        text = "❄️ START MY ARC",
                        fontWeight = FontWeight.Black,
                        fontSize = 16.sp,
                        letterSpacing = 1.5.sp
                    )
                }
            }
        }
    }
}

@Composable
fun OnboardingStep1() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(110.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0x3338BDF8), Color(0x1A0F1D33))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text("🏔️", fontSize = 54.sp)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Your Winter Arc\nStarts Now",
            style = Typography.displayMedium,
            textAlign = TextAlign.Center,
            color = IceWhite,
            lineHeight = 34.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "“90 days. One version of yourself you haven't met yet.”",
            style = Typography.titleMedium,
            color = IceCyanLight,
            textAlign = TextAlign.Center,
            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
        )

        Spacer(modifier = Modifier.height(24.dp))

        FrostedCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text(
                    text = "THE CORE PHILOSOPHY",
                    style = Typography.labelSmall,
                    color = IceCyanPrimary,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "You define who you want to become. We help you stay locked in for 90 days with unbreakable discipline, custom targets, and gamified progress.",
                    style = Typography.bodyMedium,
                    color = TextPrimary
                )
            }
        }
    }
}

@Composable
fun OnboardingStep2() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(110.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0x33818CF8), Color(0x1A0F1D33))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text("🎯", fontSize = 54.sp)
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "Build Your Own Arc",
            style = Typography.displayMedium,
            textAlign = TextAlign.Center,
            color = IceWhite
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Completely customizable. No rigid cookie-cutter habits.",
            style = Typography.bodyLarge,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            FeatureItem(emoji = "⚡", title = "8 Flexible Goal Types", desc = "Habits, Durations, Quantities, Limits, Milestones & Progress %.")
            FeatureItem(emoji = "🎨", title = "Custom Categories", desc = "Coding, Fitness, Guitar, Languages, Mind, Art or anything.")
            FeatureItem(emoji = "⏱️", title = "Your Own Targets", desc = "Minutes, hours, pages, problems, sessions or percentages.")
        }
    }
}

@Composable
fun OnboardingStep3() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(110.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0x33FBBF24), Color(0x1A0F1D33))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text("🔥", fontSize = 54.sp)
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "Stay Relentlessly\nConsistent",
            style = Typography.displayMedium,
            textAlign = TextAlign.Center,
            color = IceWhite,
            lineHeight = 34.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Gamify your transformation with XP, Levels, and Badges.",
            style = Typography.bodyLarge,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            FeatureItem(emoji = "📈", title = "Level Up with XP", desc = "Earn XP for each goal, check-in, and milestone achieved.")
            FeatureItem(emoji = "❄️", title = "90-Day Frozen Calendar", desc = "Watch your days freeze blue as consistency compounds.")
            FeatureItem(emoji = "🏆", title = "Epic Achievements", desc = "Unlock badges like Locked In, Frozen Mind, and The Summit.")
        }
    }
}

@Composable
fun FeatureItem(emoji: String, title: String, desc: String) {
    FrostedCard(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = 12.dp
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = emoji, fontSize = 22.sp, modifier = Modifier.padding(end = 12.dp))
            Column {
                Text(text = title, style = Typography.titleMedium, color = IceWhite, fontSize = 14.sp)
                Text(text = desc, style = Typography.bodyMedium, color = TextSecondary, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun OnboardingStep4(
    arcName: String,
    onArcNameChange: (String) -> Unit,
    motivation: String,
    onMotivationChange: (String) -> Unit,
    mainObjective: String,
    onMainObjectiveChange: (String) -> Unit,
    durationDays: Int,
    onDurationChange: (Int) -> Unit,
    baselineFitness: String,
    onBaselineFitnessChange: (String) -> Unit,
    baselineStudy: String,
    onBaselineStudyChange: (String) -> Unit,
    baselineCoding: String,
    onBaselineCodingChange: (String) -> Unit,
    baselineScreenTime: String,
    onBaselineScreenTimeChange: (String) -> Unit,
    baselineReflection: String,
    onBaselineReflectionChange: (String) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(bottom = 16.dp)
    ) {
        Text(
            text = "Define Your Winter Arc",
            style = Typography.headlineMedium,
            color = IceWhite
        )
        Text(
            text = "Set your personal challenge guidelines and optional Day 1 baseline.",
            style = Typography.bodyMedium,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Arc Name
        OutlinedTextField(
            value = arcName,
            onValueChange = onArcNameChange,
            label = { Text("Arc Name") },
            placeholder = { Text("e.g. My Winter Arc 2026") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = IceCyanPrimary,
                unfocusedBorderColor = DarkCardBorder,
                focusedTextColor = IceWhite,
                unfocusedTextColor = IceWhite
            ),
            shape = RoundedCornerShape(14.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Personal Motivation
        OutlinedTextField(
            value = motivation,
            onValueChange = onMotivationChange,
            label = { Text("Personal Motivation") },
            placeholder = { Text("Why are you entering the Winter Arc?") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = IceCyanPrimary,
                unfocusedBorderColor = DarkCardBorder,
                focusedTextColor = IceWhite,
                unfocusedTextColor = IceWhite
            ),
            shape = RoundedCornerShape(14.dp),
            maxLines = 3
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Main Objective
        OutlinedTextField(
            value = mainObjective,
            onValueChange = onMainObjectiveChange,
            label = { Text("Main Objective") },
            placeholder = { Text("What is your single biggest target?") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = IceCyanPrimary,
                unfocusedBorderColor = DarkCardBorder,
                focusedTextColor = IceWhite,
                unfocusedTextColor = IceWhite
            ),
            shape = RoundedCornerShape(14.dp),
            maxLines = 2
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Arc Duration Selection
        Text(
            text = "ARC DURATION",
            style = Typography.labelLarge,
            color = IceCyanLight,
            letterSpacing = 1.sp
        )
        Text(
            text = "Default is 90 days (standard Winter Arc). You can also customize this anytime later in settings.",
            style = Typography.bodyMedium,
            color = TextSecondary,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        val presetDurations = listOf(30, 60, 75, 90, 100, 120)
        var isCustomDuration by remember { mutableStateOf(!presetDurations.contains(durationDays)) }
        var customDurationText by remember { mutableStateOf(durationDays.toString()) }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            presetDurations.forEach { days ->
                val isSelected = !isCustomDuration && durationDays == days
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) Color(0x330284C7) else Color(0x1F0F172A))
                        .border(
                            1.dp,
                            if (isSelected) IceCyanPrimary else Color(0x1FFFFFFF),
                            RoundedCornerShape(10.dp)
                        )
                        .clickable {
                            isCustomDuration = false
                            onDurationChange(days)
                        }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = if (days == 90) "90 Days ★" else "$days Days",
                        style = Typography.labelMedium,
                        color = if (isSelected) IceCyanPrimary else TextSecondary,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isCustomDuration) Color(0x33818CF8) else Color(0x1F0F172A))
                    .border(
                        1.dp,
                        if (isCustomDuration) FrostAccent else Color(0x1FFFFFFF),
                        RoundedCornerShape(10.dp)
                    )
                    .clickable { isCustomDuration = true }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Custom...",
                    style = Typography.labelMedium,
                    color = if (isCustomDuration) FrostAccent else TextSecondary,
                    fontWeight = if (isCustomDuration) FontWeight.Bold else FontWeight.Medium
                )
            }
        }

        if (isCustomDuration) {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = customDurationText,
                onValueChange = { input ->
                    val filtered = input.filter { it.isDigit() }
                    customDurationText = filtered
                    val daysVal = filtered.toIntOrNull()
                    if (daysVal != null && daysVal in 7..365) {
                        onDurationChange(daysVal)
                    }
                },
                label = { Text("Custom Days (7 - 365)") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = FrostAccent,
                    unfocusedBorderColor = DarkCardBorder,
                    focusedTextColor = IceWhite,
                    unfocusedTextColor = IceWhite
                ),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "DAY 1 BASELINE (OPTIONAL)",
            style = Typography.labelLarge,
            color = IceCyanLight,
            letterSpacing = 1.sp
        )
        Text(
            text = "Record your starting point to compare at Day $durationDays.",
            style = Typography.bodyMedium,
            color = TextMuted,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = baselineFitness,
            onValueChange = onBaselineFitnessChange,
            label = { Text("Fitness Baseline") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = IceCyanPrimary,
                unfocusedBorderColor = DarkCardBorder,
                focusedTextColor = IceWhite,
                unfocusedTextColor = IceWhite
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = baselineCoding,
            onValueChange = onBaselineCodingChange,
            label = { Text("Coding / Skill Baseline") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = IceCyanPrimary,
                unfocusedBorderColor = DarkCardBorder,
                focusedTextColor = IceWhite,
                unfocusedTextColor = IceWhite
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = baselineScreenTime,
            onValueChange = onBaselineScreenTimeChange,
            label = { Text("Screen Time / Distraction Baseline") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = IceCyanPrimary,
                unfocusedBorderColor = DarkCardBorder,
                focusedTextColor = IceWhite,
                unfocusedTextColor = IceWhite
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = baselineReflection,
            onValueChange = onBaselineReflectionChange,
            label = { Text("Day 1 Mindset Reflection") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = IceCyanPrimary,
                unfocusedBorderColor = DarkCardBorder,
                focusedTextColor = IceWhite,
                unfocusedTextColor = IceWhite
            ),
            shape = RoundedCornerShape(12.dp),
            maxLines = 2
        )
    }
}
