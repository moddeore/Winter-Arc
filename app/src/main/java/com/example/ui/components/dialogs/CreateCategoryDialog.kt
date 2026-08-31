package com.example.ui.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
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
fun CreateCategoryDialog(
    onDismiss: () -> Unit,
    onCreateCategory: (name: String, icon: String, colorHex: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var selectedIcon by remember { mutableStateOf("🎸") }
    var selectedColor by remember { mutableStateOf("#38BDF8") }

    val iconOptions = listOf(
        "🎸", "🎨", "🎵", "🗣️", "📷", "💼", "🍳", "🥊", "🧗", "⚡",
        "🥋", "🚴", "🏊", "♟️", "🎹", "✍️", "🚀", "🔬", "🧘", "🛠️"
    )

    val colorOptions = listOf(
        "#38BDF8", "#818CF8", "#F43F5E", "#10B981", "#F59E0B",
        "#EC4899", "#A855F7", "#14B8A6", "#EAB308", "#64748B"
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(24.dp)),
            color = DarkBg,
            border = androidx.compose.foundation.BorderStroke(1.dp, IceGlow)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "NEW CUSTOM CATEGORY",
                        style = Typography.titleMedium,
                        color = IceWhite,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Category Name") },
                    placeholder = { Text("e.g. Guitar, Art, German, Chess") },
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

                Text(
                    text = "SELECT ICON / EMOJI",
                    style = Typography.labelSmall,
                    color = IceCyanLight,
                    letterSpacing = 1.sp
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(5),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(iconOptions) { emoji ->
                        val isSelected = selectedIcon == emoji
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) Color(0x3338BDF8) else DarkCardBg)
                                .border(
                                    1.dp,
                                    if (isSelected) IceCyanPrimary else DarkCardBorder,
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable { selectedIcon = emoji },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = emoji, fontSize = 20.sp)
                        }
                    }
                }

                Text(
                    text = "ACCENT COLOR",
                    style = Typography.labelSmall,
                    color = IceCyanLight,
                    letterSpacing = 1.sp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    colorOptions.take(6).forEach { hex ->
                        val parsedColor = try {
                            Color(android.graphics.Color.parseColor(hex))
                        } catch (e: Exception) {
                            IceCyanPrimary
                        }
                        val isSelected = selectedColor == hex

                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(parsedColor)
                                .border(
                                    if (isSelected) 2.dp else 0.dp,
                                    IceWhite,
                                    CircleShape
                                )
                                .clickable { selectedColor = hex }
                        )
                    }
                }

                Button(
                    onClick = {
                        if (name.isNotBlank()) {
                            onCreateCategory(name.trim(), selectedIcon, selectedColor)
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
                        .height(48.dp)
                ) {
                    Text(
                        text = "CREATE CATEGORY",
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
