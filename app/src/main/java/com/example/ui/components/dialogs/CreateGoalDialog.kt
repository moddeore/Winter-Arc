package com.example.ui.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
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
import com.example.data.local.entity.CategoryEntity
import com.example.data.local.entity.GoalDifficulty
import com.example.data.local.entity.GoalType
import com.example.ui.components.FrostedCard
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGoalDialog(
    categories: List<CategoryEntity>,
    onDismiss: () -> Unit,
    onCreateGoal: (
        name: String,
        description: String,
        categoryId: Long,
        categoryName: String,
        categoryIcon: String,
        goalType: GoalType,
        targetValue: Float,
        targetUnit: String,
        targetFrequency: String,
        difficulty: GoalDifficulty,
        milestones: List<String>
    ) -> Unit,
    onRequestCreateCategory: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(categories.firstOrNull()) }
    var selectedGoalType by remember { mutableStateOf(GoalType.DAILY_HABIT) }
    var targetValueStr by remember { mutableStateOf("1") }
    var targetUnit by remember { mutableStateOf("session") }
    var targetFrequency by remember { mutableStateOf("Daily") }
    var difficulty by remember { mutableStateOf(GoalDifficulty.MEDIUM) }

    // Milestone list
    val milestones = remember { mutableStateListOf<String>() }
    var newMilestoneText by remember { mutableStateOf("") }

    // Preset unit suggestions based on GoalType
    LaunchedEffect(selectedGoalType) {
        when (selectedGoalType) {
            GoalType.DURATION_GOAL -> {
                targetUnit = "min"
                if (targetValueStr == "1") targetValueStr = "60"
            }
            GoalType.QUANTITY_GOAL -> {
                targetUnit = "problems"
                if (targetValueStr == "1") targetValueStr = "5"
            }
            GoalType.LIMIT_GOAL -> {
                targetUnit = "min"
                if (targetValueStr == "1") targetValueStr = "45"
            }
            GoalType.PROGRESS_GOAL -> {
                targetUnit = "%"
                targetValueStr = "100"
            }
            GoalType.WEEKLY_GOAL -> {
                targetUnit = "times"
                targetFrequency = "4x / week"
                if (targetValueStr == "1") targetValueStr = "4"
            }
            GoalType.MILESTONE_GOAL -> {
                targetUnit = "milestones"
                targetValueStr = if (milestones.isNotEmpty()) milestones.size.toString() else "5"
            }
            else -> {
                targetUnit = "times"
                targetValueStr = "1"
            }
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.88f)
                .clip(RoundedCornerShape(24.dp)),
            color = DarkBg,
            border = androidx.compose.foundation.BorderStroke(1.dp, IceGlow)
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
                    Column {
                        Text(
                            text = "CREATE CUSTOM GOAL",
                            style = Typography.titleLarge,
                            color = IceWhite,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Define your standard of discipline",
                            style = Typography.bodyMedium,
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Scrollable Form
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Goal Name
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Goal Name *") },
                        placeholder = { Text("e.g. Learn Python, Cold Shower, Deep Study") },
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

                    // Description
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description / Rules") },
                        placeholder = { Text("e.g. Complete 3 coding problems before 9 AM.") },
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

                    // Category Selector
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "CATEGORY",
                                style = Typography.labelSmall,
                                color = IceCyanLight,
                                letterSpacing = 1.sp
                            )
                            TextButton(
                                onClick = onRequestCreateCategory,
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text("＋ New Category", color = IceCyanPrimary, fontSize = 12.sp)
                            }
                        }

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            items(categories) { cat ->
                                val isSelected = selectedCategory?.id == cat.id
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isSelected) Color(0x3338BDF8) else DarkCardBg)
                                        .border(
                                            1.dp,
                                            if (isSelected) IceCyanPrimary else DarkCardBorder,
                                            RoundedCornerShape(12.dp)
                                        )
                                        .clickable { selectedCategory = cat }
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = cat.icon, modifier = Modifier.padding(end = 4.dp))
                                        Text(
                                            text = cat.name,
                                            color = if (isSelected) IceWhite else TextSecondary,
                                            style = Typography.titleMedium,
                                            fontSize = 13.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Goal Type Selector
                    Column {
                        Text(
                            text = "GOAL TYPE",
                            style = Typography.labelSmall,
                            color = IceCyanLight,
                            letterSpacing = 1.sp
                        )
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(top = 6.dp)
                        ) {
                            items(GoalType.entries) { type ->
                                val isSelected = selectedGoalType == type
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isSelected) Color(0x33818CF8) else DarkCardBg)
                                        .border(
                                            1.dp,
                                            if (isSelected) FrostAccent else DarkCardBorder,
                                            RoundedCornerShape(12.dp)
                                        )
                                        .clickable { selectedGoalType = type }
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = type.displayName,
                                        color = if (isSelected) IceWhite else TextSecondary,
                                        style = Typography.titleMedium,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }
                    }

                    // Target & Units
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = targetValueStr,
                            onValueChange = { targetValueStr = it },
                            label = { Text("Target Value") },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = IceCyanPrimary,
                                unfocusedBorderColor = DarkCardBorder,
                                focusedTextColor = IceWhite,
                                unfocusedTextColor = IceWhite
                            ),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = targetUnit,
                            onValueChange = { targetUnit = it },
                            label = { Text("Unit") },
                            placeholder = { Text("min, hrs, pages, %") },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = IceCyanPrimary,
                                unfocusedBorderColor = DarkCardBorder,
                                focusedTextColor = IceWhite,
                                unfocusedTextColor = IceWhite
                            ),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                    }

                    // Frequency
                    OutlinedTextField(
                        value = targetFrequency,
                        onValueChange = { targetFrequency = it },
                        label = { Text("Frequency") },
                        placeholder = { Text("Daily, 4x/week, Once") },
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

                    // Milestone Builder (if MILESTONE_GOAL)
                    if (selectedGoalType == GoalType.MILESTONE_GOAL) {
                        Column {
                            Text(
                                text = "MILESTONES / ROADMAP",
                                style = Typography.labelSmall,
                                color = IceCyanLight,
                                letterSpacing = 1.sp
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = newMilestoneText,
                                    onValueChange = { newMilestoneText = it },
                                    placeholder = { Text("e.g. Variables, Loops, OOP") },
                                    modifier = Modifier.weight(1f),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = IceCyanPrimary,
                                        unfocusedBorderColor = DarkCardBorder,
                                        focusedTextColor = IceWhite,
                                        unfocusedTextColor = IceWhite
                                    ),
                                    shape = RoundedCornerShape(10.dp),
                                    singleLine = true
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                IconButton(
                                    onClick = {
                                        if (newMilestoneText.isNotBlank()) {
                                            milestones.add(newMilestoneText.trim())
                                            newMilestoneText = ""
                                        }
                                    },
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(IceCyanPrimary)
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Add", tint = DarkBg)
                                }
                            }

                            milestones.forEachIndexed { idx, ms ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${idx + 1}. $ms",
                                        style = Typography.bodyMedium,
                                        color = IceWhite
                                    )
                                    IconButton(
                                        onClick = { milestones.removeAt(idx) },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(Icons.Default.Close, contentDescription = "Remove", tint = TextMuted)
                                    }
                                }
                            }
                        }
                    }

                    // Difficulty Selector & XP Preview
                    Column {
                        Text(
                            text = "DIFFICULTY & XP REWARD",
                            style = Typography.labelSmall,
                            color = IceCyanLight,
                            letterSpacing = 1.sp
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            GoalDifficulty.entries.forEach { diff ->
                                val isSelected = difficulty == diff
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isSelected) Color(0x3338BDF8) else DarkCardBg)
                                        .border(
                                            1.dp,
                                            if (isSelected) IceCyanPrimary else DarkCardBorder,
                                            RoundedCornerShape(12.dp)
                                        )
                                        .clickable { difficulty = diff }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = diff.displayName.take(6),
                                            style = Typography.labelMedium,
                                            color = if (isSelected) IceWhite else TextSecondary,
                                            fontSize = 11.sp
                                        )
                                        Text(
                                            text = "+${diff.xpReward} XP",
                                            style = Typography.labelSmall,
                                            color = if (isSelected) IceCyanLight else TextMuted,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Submit Button
                Button(
                    onClick = {
                        if (name.isNotBlank()) {
                            val cat = selectedCategory ?: categories.firstOrNull() ?: CategoryEntity(
                                id = 1,
                                name = "Personal",
                                icon = "✨",
                                colorHex = "#38BDF8"
                            )
                            val targetVal = targetValueStr.toFloatOrNull() ?: 1f
                            onCreateGoal(
                                name,
                                description,
                                cat.id,
                                cat.name,
                                cat.icon,
                                selectedGoalType,
                                targetVal,
                                targetUnit,
                                targetFrequency,
                                difficulty,
                                milestones.toList()
                            )
                        }
                    },
                    enabled = name.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = IceCyanPrimary,
                        contentColor = DarkBg,
                        disabledContainerColor = Color(0x3338BDF8),
                        disabledContentColor = TextMuted
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(
                        text = "＋ ADD CUSTOM GOAL (+${difficulty.xpReward} XP)",
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp,
                        color = if (name.isNotBlank()) DarkBg else TextMuted,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}
