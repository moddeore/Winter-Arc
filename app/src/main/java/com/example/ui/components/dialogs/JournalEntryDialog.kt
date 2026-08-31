package com.example.ui.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.example.ui.theme.*

@Composable
fun JournalEntryDialog(
    dayNumber: Int,
    onDismiss: () -> Unit,
    onSubmit: (title: String, content: String, moodEmoji: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedMoodEmoji by remember { mutableStateOf("❄️") }

    val moodOptions = listOf("❄️", "🔥", "⚔️", "🏔️", "🧠", "💪", "⚡")

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .clip(RoundedCornerShape(24.dp)),
            color = DarkBg,
            border = androidx.compose.foundation.BorderStroke(1.dp, IceGlow)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "NEW ARC JOURNAL ENTRY",
                        style = Typography.titleMedium,
                        color = IceWhite,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    placeholder = { Text("e.g. Breaking through the mental wall") },
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

                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Journal Notes & Lessons") },
                    placeholder = { Text("What did you conquer today? What tested your discipline?") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = IceCyanPrimary,
                        unfocusedBorderColor = DarkCardBorder,
                        focusedTextColor = IceWhite,
                        unfocusedTextColor = IceWhite
                    ),
                    shape = RoundedCornerShape(12.dp),
                    minLines = 4,
                    maxLines = 6
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Mood Tag:", style = Typography.labelSmall, color = TextSecondary)
                    moodOptions.forEach { emoji ->
                        val isSelected = selectedMoodEmoji == emoji
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) Color(0x3338BDF8) else DarkCardBg)
                                .padding(4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = emoji, fontSize = 18.sp)
                        }
                    }
                }

                Button(
                    onClick = {
                        if (content.isNotBlank()) {
                            onSubmit(
                                title.ifBlank { "Day $dayNumber Entry" },
                                content.trim(),
                                selectedMoodEmoji
                            )
                        }
                    },
                    enabled = content.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = IceCyanPrimary,
                        contentColor = DarkBg,
                        disabledContainerColor = Color(0x3338BDF8),
                        disabledContentColor = TextMuted
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(
                        text = "SAVE TO JOURNAL 📖",
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp,
                        color = if (content.isNotBlank()) DarkBg else TextMuted
                    )
                }
            }
        }
    }
}
