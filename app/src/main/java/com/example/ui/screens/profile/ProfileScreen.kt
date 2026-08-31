package com.example.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.JournalEntryEntity
import com.example.ui.components.FrostedCard
import com.example.ui.components.GlowingBorderCard
import com.example.ui.components.SnowfallEffect
import com.example.ui.theme.*
import com.example.ui.viewmodel.WinterArcUiState

@Composable
fun ProfileScreen(
    uiState: WinterArcUiState,
    onOpenCheckIn: () -> Unit,
    onOpenNewJournal: () -> Unit,
    onDeleteJournalEntry: (JournalEntryEntity) -> Unit,
    onUpdatePreferences: (
        username: String,
        avatarEmoji: String,
        reminders: Boolean,
        checkIn: Boolean,
        streakProtection: Boolean,
        morningQuotes: Boolean,
        eveningReflection: Boolean
    ) -> Unit,
    onUpdateArcDuration: (Int) -> Unit = {},
    onUpdateArcDetails: (name: String, motivation: String, mainObjective: String, durationDays: Int) -> Unit = { _, _, _, _ -> }
) {
    val profile = uiState.userProfile
    var username by remember(profile?.username) { mutableStateOf(profile?.username ?: "Arc Warrior") }
    var avatarEmoji by remember(profile?.avatarEmoji) { mutableStateOf(profile?.avatarEmoji ?: "❄️") }

    var goalReminders by remember(profile?.goalRemindersEnabled) { mutableStateOf(profile?.goalRemindersEnabled ?: true) }
    var dailyCheckIn by remember(profile?.dailyCheckInEnabled) { mutableStateOf(profile?.dailyCheckInEnabled ?: true) }
    var streakProtection by remember(profile?.streakProtectionEnabled) { mutableStateOf(profile?.streakProtectionEnabled ?: true) }
    var morningMotivation by remember(profile?.morningMotivationEnabled) { mutableStateOf(profile?.morningMotivationEnabled ?: true) }
    var eveningReflection by remember(profile?.eveningReflectionEnabled) { mutableStateOf(profile?.eveningReflectionEnabled ?: true) }

    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showCustomizeArcDialog by remember { mutableStateOf(false) }

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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "WARRIOR PROFILE 👤",
                            style = Typography.displayMedium,
                            color = IceWhite,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Identity, Arc journal, and discipline settings",
                            style = Typography.bodyMedium,
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }

                    IconButton(onClick = { showEditProfileDialog = true }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Profile", tint = IceCyanPrimary)
                    }
                }
            }

            // User Identity Card
            item {
                FrostedCard(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = 18.dp
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(Color(0x3338BDF8))
                                .border(2.dp, IceCyanPrimary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = avatarEmoji, fontSize = 32.sp)
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                text = username,
                                style = Typography.titleLarge,
                                color = IceWhite,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "LEVEL ${profile?.level ?: 1} • ${profile?.totalXp ?: 0} XP",
                                style = Typography.labelMedium,
                                color = IceCyanLight,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "DAY ${uiState.currentDay} OF ${uiState.totalDays} IN ARC",
                                style = Typography.labelSmall,
                                color = TextMuted,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }

            // Journal Section
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
                            text = "ARC JOURNAL 📖",
                            style = Typography.titleMedium,
                            color = IceWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                        Text(
                            text = "${uiState.journalEntries.size} personal reflections recorded",
                            style = Typography.labelSmall,
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }

                    Button(
                        onClick = onOpenNewJournal,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = IceCyanPrimary,
                            contentColor = DarkBg
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                        modifier = Modifier.defaultMinSize(minWidth = 100.dp, minHeight = 38.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Add Reflection",
                                modifier = Modifier.size(16.dp),
                                tint = DarkBg
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "New Entry",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = DarkBg
                            )
                        }
                    }
                }
            }

            if (uiState.journalEntries.isEmpty()) {
                item {
                    FrostedCard(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = 20.dp
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("✍️", fontSize = 28.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No Journal Entries Yet",
                                style = Typography.titleMedium,
                                color = IceWhite
                            )
                            Text(
                                text = "Reflections during daily check-ins will automatically appear here.",
                                style = Typography.bodyMedium,
                                color = TextSecondary,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            } else {
                items(uiState.journalEntries, key = { it.id }) { entry ->
                    FrostedCard(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = 14.dp
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = entry.moodEmoji, fontSize = 18.sp, modifier = Modifier.padding(end = 6.dp))
                                    Text(
                                        text = entry.title,
                                        style = Typography.titleMedium,
                                        color = IceWhite,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                }

                                IconButton(
                                    onClick = { onDeleteJournalEntry(entry) },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = TextMuted, modifier = Modifier.size(16.dp))
                                }
                            }

                            Text(
                                text = entry.content,
                                style = Typography.bodyMedium,
                                color = TextPrimary,
                                fontSize = 13.sp
                            )

                            Text(
                                text = "DAY ${entry.dayNumber} OF ARC",
                                style = Typography.labelSmall,
                                color = IceCyanLight,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }

            // Arc Configuration & Duration Settings
            item {
                Text(
                    text = "ARC CONFIGURATION & DURATION ⏱️",
                    style = Typography.titleMedium,
                    color = IceWhite,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                GlowingBorderCard(
                    modifier = Modifier.fillMaxWidth(),
                    glowColor = IceCyanPrimary,
                    contentPadding = 18.dp
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = uiState.activeArc?.name ?: "Winter Arc Challenge",
                                    style = Typography.titleMedium,
                                    color = IceWhite,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = uiState.activeArc?.mainObjective ?: "Relentless discipline & focus",
                                    style = Typography.bodySmall,
                                    color = IceCyanLight,
                                    fontSize = 12.sp
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0x3338BDF8))
                                    .border(1.dp, IceCyanPrimary, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "ACTIVE",
                                    style = Typography.labelSmall,
                                    color = IceWhite,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                )
                            }
                        }

                        HorizontalDivider(color = DarkCardBorder)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("TOTAL DURATION", style = Typography.labelSmall, color = TextMuted, fontSize = 10.sp)
                                Text("${uiState.totalDays} Days", style = Typography.titleMedium, color = IceWhite, fontWeight = FontWeight.Bold)
                            }
                            Column {
                                Text("CURRENT DAY", style = Typography.labelSmall, color = TextMuted, fontSize = 10.sp)
                                Text("Day ${uiState.currentDay}", style = Typography.titleMedium, color = IceCyanLight, fontWeight = FontWeight.Bold)
                            }
                            Column {
                                Text("DAYS REMAINING", style = Typography.labelSmall, color = TextMuted, fontSize = 10.sp)
                                val remaining = (uiState.totalDays - uiState.currentDay).coerceAtLeast(0)
                                Text("$remaining Days", style = Typography.titleMedium, color = FrostAccent, fontWeight = FontWeight.Bold)
                            }
                        }

                        Button(
                            onClick = { showCustomizeArcDialog = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0x330284C7),
                                contentColor = IceWhite
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.dp, IceCyanPrimary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = "Customize Days",
                                    modifier = Modifier.size(16.dp),
                                    tint = IceCyanLight
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Customize Arc Days & Target",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = IceWhite
                                )
                            }
                        }
                    }
                }
            }

            // Notification & Preferences Settings
            item {
                Text(
                    text = "DISCIPLINE SETTINGS ⚙️",
                    style = Typography.titleMedium,
                    color = IceWhite,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                FrostedCard(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = 16.dp
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        SettingToggleRow(
                            title = "Daily Goal Reminders",
                            subtitle = "Morning notification at 8:00 AM",
                            checked = goalReminders,
                            onCheckedChange = {
                                goalReminders = it
                                onUpdatePreferences(username, avatarEmoji, goalReminders, dailyCheckIn, streakProtection, morningMotivation, eveningReflection)
                            }
                        )

                        HorizontalDivider(color = DarkCardBorder)

                        SettingToggleRow(
                            title = "Daily Check-In Prompt",
                            subtitle = "Evening notification at 9:00 PM",
                            checked = dailyCheckIn,
                            onCheckedChange = {
                                dailyCheckIn = it
                                onUpdatePreferences(username, avatarEmoji, goalReminders, dailyCheckIn, streakProtection, morningMotivation, eveningReflection)
                            }
                        )

                        HorizontalDivider(color = DarkCardBorder)

                        SettingToggleRow(
                            title = "Streak Shield Protection",
                            subtitle = "Save streak with emergency recovery",
                            checked = streakProtection,
                            onCheckedChange = {
                                streakProtection = it
                                onUpdatePreferences(username, avatarEmoji, goalReminders, dailyCheckIn, streakProtection, morningMotivation, eveningReflection)
                            }
                        )

                        HorizontalDivider(color = DarkCardBorder)

                        SettingToggleRow(
                            title = "Morning Motivation Quote",
                            subtitle = "Relentless focus mindset drops",
                            checked = morningMotivation,
                            onCheckedChange = {
                                morningMotivation = it
                                onUpdatePreferences(username, avatarEmoji, goalReminders, dailyCheckIn, streakProtection, morningMotivation, eveningReflection)
                            }
                        )
                    }
                }
            }

            // Developer & Creator Credits Footer
            item {
                FrostedCard(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = 16.dp
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Color(0x330284C7))
                                    .border(1.5.dp, IceCyanPrimary, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "👨‍💻",
                                    fontSize = 20.sp
                                )
                            }

                            Column {
                                Text(
                                    text = "DEVELOPED BY",
                                    style = Typography.labelSmall,
                                    color = TextMuted,
                                    fontSize = 10.sp,
                                    letterSpacing = 1.2.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Mod Deore",
                                    style = Typography.titleMedium,
                                    color = IceWhite,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 16.sp
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0x240284C7))
                                .border(1.dp, Color(0x6638BDF8), RoundedCornerShape(8.dp))
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "🤖",
                                    fontSize = 11.sp
                                )
                                Text(
                                    text = "Android Developer",
                                    style = Typography.labelSmall,
                                    color = IceCyanLight,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }

            // App Version & Tagline
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "WINTER ARC v1.0.0",
                        style = Typography.labelSmall,
                        color = TextMuted,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "DEFINE YOUR ARC. STAY LOCKED IN. BECOME THE SUMMIT. ❄️",
                        style = Typography.labelSmall,
                        color = IceCyanLight,
                        fontSize = 9.sp,
                        letterSpacing = 1.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }
    }

    // Edit Profile Dialog
    if (showEditProfileDialog) {
        val emojis = listOf("❄️", "⚔️", "🔥", "🏔️", "🐺", "🦅", "🥋", "⚡", "🧠", "🎯")
        AlertDialog(
            onDismissRequest = { showEditProfileDialog = false },
            title = { Text("Edit Warrior Profile", color = IceWhite) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Username") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = IceCyanPrimary,
                            unfocusedBorderColor = DarkCardBorder,
                            focusedTextColor = IceWhite,
                            unfocusedTextColor = IceWhite
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Text("Choose Avatar Emoji:", style = Typography.labelSmall, color = TextSecondary)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        emojis.take(5).forEach { emo ->
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(if (avatarEmoji == emo) Color(0x3338BDF8) else DarkCardBg)
                                    .border(if (avatarEmoji == emo) 1.dp else 0.dp, IceCyanPrimary, CircleShape)
                                    .clickable { avatarEmoji = emo },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = emo, fontSize = 18.sp)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdatePreferences(username, avatarEmoji, goalReminders, dailyCheckIn, streakProtection, morningMotivation, eveningReflection)
                        showEditProfileDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = IceCyanPrimary, contentColor = DarkBg),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Save Changes", fontWeight = FontWeight.Bold, color = DarkBg, fontSize = 14.sp)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditProfileDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = DarkBg
        )
    }

    // Customize Arc Duration & Settings Dialog
    if (showCustomizeArcDialog) {
        CustomizeArcDialog(
            activeArc = uiState.activeArc,
            currentTotalDays = uiState.totalDays,
            onDismiss = { showCustomizeArcDialog = false },
            onSave = { name, motiv, obj, duration ->
                onUpdateArcDetails(name, motiv, obj, duration)
                showCustomizeArcDialog = false
            }
        )
    }
}

@Composable
fun CustomizeArcDialog(
    activeArc: com.example.data.local.entity.WinterArcEntity?,
    currentTotalDays: Int,
    onDismiss: () -> Unit,
    onSave: (name: String, motivation: String, mainObjective: String, durationDays: Int) -> Unit
) {
    var arcName by remember(activeArc?.name) { mutableStateOf(activeArc?.name ?: "My Winter Arc") }
    var motivation by remember(activeArc?.motivation) { mutableStateOf(activeArc?.motivation ?: "") }
    var mainObjective by remember(activeArc?.mainObjective) { mutableStateOf(activeArc?.mainObjective ?: "") }

    val presetDurations = listOf(30, 45, 60, 75, 90, 100, 120, 180, 365)
    var selectedDuration by remember(currentTotalDays) { mutableIntStateOf(currentTotalDays) }
    var isCustom by remember(currentTotalDays) { mutableStateOf(!presetDurations.contains(currentTotalDays)) }
    var customDaysText by remember(currentTotalDays) { mutableStateOf(currentTotalDays.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text("Customize Arc Duration ⏱️", color = IceWhite, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(
                    "Standard is 90 days, but you can set any duration for your challenge.",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Arc Name
                OutlinedTextField(
                    value = arcName,
                    onValueChange = { arcName = it },
                    label = { Text("Arc Challenge Name") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = IceCyanPrimary,
                        unfocusedBorderColor = DarkCardBorder,
                        focusedTextColor = IceWhite,
                        unfocusedTextColor = IceWhite
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Main Objective
                OutlinedTextField(
                    value = mainObjective,
                    onValueChange = { mainObjective = it },
                    label = { Text("Primary Objective") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = IceCyanPrimary,
                        unfocusedBorderColor = DarkCardBorder,
                        focusedTextColor = IceWhite,
                        unfocusedTextColor = IceWhite
                    ),
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )

                // Duration Selector
                Text(
                    text = "SELECT TOTAL DURATION (DAYS):",
                    style = Typography.labelSmall,
                    color = IceCyanLight,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    presetDurations.forEach { days ->
                        val isSelected = !isCustom && selectedDuration == days
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) Color(0x440284C7) else Color(0x1F0F172A))
                                .border(
                                    1.dp,
                                    if (isSelected) IceCyanPrimary else Color(0x22FFFFFF),
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable {
                                    isCustom = false
                                    selectedDuration = days
                                    customDaysText = days.toString()
                                }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = if (days == 90) "90d ★" else "${days}d",
                                style = Typography.labelMedium,
                                color = if (isSelected) IceWhite else TextSecondary,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isCustom) Color(0x44818CF8) else Color(0x1F0F172A))
                            .border(
                                1.dp,
                                if (isCustom) FrostAccent else Color(0x22FFFFFF),
                                RoundedCornerShape(8.dp)
                            )
                            .clickable { isCustom = true }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Custom",
                            style = Typography.labelMedium,
                            color = if (isCustom) IceWhite else TextSecondary,
                            fontWeight = if (isCustom) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }

                if (isCustom) {
                    OutlinedTextField(
                        value = customDaysText,
                        onValueChange = { input ->
                            val digitsOnly = input.filter { it.isDigit() }
                            customDaysText = digitsOnly
                            val parsed = digitsOnly.toIntOrNull()
                            if (parsed != null && parsed in 7..365) {
                                selectedDuration = parsed
                            }
                        },
                        label = { Text("Exact Days (7 – 365)") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FrostAccent,
                            unfocusedBorderColor = DarkCardBorder,
                            focusedTextColor = IceWhite,
                            unfocusedTextColor = IceWhite
                        ),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Recalibration Hint Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0x1A38BDF8))
                        .border(1.dp, Color(0x3338BDF8), RoundedCornerShape(10.dp))
                        .padding(10.dp)
                ) {
                    Text(
                        text = "❄️ All charts, $selectedDuration-day calendar cells, and summit elevation milestones will automatically recalibrate.",
                        style = Typography.bodySmall,
                        color = IceCyanLight,
                        fontSize = 11.sp
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val finalDays = if (isCustom) {
                        customDaysText.toIntOrNull()?.coerceIn(7, 365) ?: selectedDuration
                    } else {
                        selectedDuration
                    }
                    onSave(arcName, motivation, mainObjective, finalDays)
                },
                colors = ButtonDefaults.buttonColors(containerColor = IceCyanPrimary, contentColor = DarkBg),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Save Arc Settings", fontWeight = FontWeight.Bold, color = DarkBg, fontSize = 14.sp)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextSecondary)
            }
        },
        containerColor = DarkBg
    )
}

@Composable
fun SettingToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = Typography.titleMedium, color = IceWhite, fontSize = 14.sp)
            Text(text = subtitle, style = Typography.bodyMedium, color = TextSecondary, fontSize = 12.sp)
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = DarkBg,
                checkedTrackColor = IceCyanPrimary,
                uncheckedThumbColor = TextMuted,
                uncheckedTrackColor = DarkCardBg
            )
        )
    }
}
