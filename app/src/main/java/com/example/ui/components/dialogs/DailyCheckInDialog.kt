package com.example.ui.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.window.Dialog
import com.example.data.local.entity.DayMood
import com.example.ui.components.FrostedCard
import com.example.ui.theme.*

@Composable
fun DailyCheckInDialog(
    dayNumber: Int,
    completedGoalsCount: Int,
    totalGoalsCount: Int,
    onDismiss: () -> Unit,
    onSubmit: (mood: DayMood, reflection: String) -> Unit
) {
    var selectedMood by remember { mutableStateOf(DayMood.FIRE) }
    var reflectionText by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .clip(RoundedCornerShape(24.dp)),
            color = DarkBg,
            border = androidx.compose.foundation.BorderStroke(1.dp, IceCyanPrimary)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Top close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "DAY $dayNumber COMPLETE ❄️",
                        style = Typography.titleLarge,
                        color = IceWhite,
                        fontWeight = FontWeight.Black
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                // XP Reward Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(Color(0x3338BDF8), Color(0x33818CF8))
                            )
                        )
                        .padding(14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("✨", fontSize = 24.sp, modifier = Modifier.padding(end = 8.dp))
                        Column {
                            Text(
                                text = "+100 CHECK-IN XP",
                                style = Typography.titleMedium,
                                color = IceCyanLight,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "$completedGoalsCount of $totalGoalsCount goals completed today",
                                style = Typography.bodyMedium,
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                // Mood Question
                Text(
                    text = "How was your discipline today?",
                    style = Typography.titleMedium,
                    color = IceWhite
                )

                // Mood selector buttons: 😔 😐 🙂 🔥
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    DayMood.entries.forEach { mood ->
                        val isSelected = selectedMood == mood
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isSelected) Color(0x3338BDF8) else DarkCardBg)
                                .border(
                                    1.dp,
                                    if (isSelected) IceCyanPrimary else DarkCardBorder,
                                    RoundedCornerShape(14.dp)
                                )
                                .clickable { selectedMood = mood }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = mood.emoji, fontSize = 24.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = mood.label,
                                    style = Typography.labelSmall,
                                    color = if (isSelected) IceWhite else TextMuted,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }

                // Journal / Reflection input
                OutlinedTextField(
                    value = reflectionText,
                    onValueChange = { reflectionText = it },
                    label = { Text("Daily Arc Reflection") },
                    placeholder = { Text("Today I learned / overcame...") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = IceCyanPrimary,
                        unfocusedBorderColor = DarkCardBorder,
                        focusedTextColor = IceWhite,
                        unfocusedTextColor = IceWhite
                    ),
                    shape = RoundedCornerShape(14.dp),
                    minLines = 3,
                    maxLines = 4
                )

                Button(
                    onClick = {
                        onSubmit(selectedMood, reflectionText.trim())
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = IceCyanPrimary,
                        contentColor = DarkBg
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(
                        text = "LOCK IN DAY $dayNumber ❄️",
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp,
                        color = DarkBg,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}
