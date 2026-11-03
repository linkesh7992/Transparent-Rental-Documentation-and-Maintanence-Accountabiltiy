package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.backend.models.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RaiseIssueDialog(
    isTamil: Boolean,
    tenantName: String,
    onRaiseIssue: (MaintenanceIssue) -> Unit,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(MaintenanceCategory.ELECTRICAL) }
    var selectedPriority by remember { mutableStateOf(MaintenancePriority.HIGH) }
    var isVoiceRecorded by remember { mutableStateOf(false) }
    var isRecording by remember { mutableStateOf(false) }
    var attachedPhotoCount by remember { mutableStateOf(1) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.ReportProblem,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isTamil) "புதிய புகார் பதிவு செய்" else "Raise New Maintenance Issue",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Issue Title
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(if (isTamil) "புகார் தலைப்பு" else "Issue Title / Subject") },
                    placeholder = { Text("e.g. Geyser switch spark, Tap leaking") },
                    modifier = Modifier.fillMaxWidth().testTag("raise_issue_title_input"),
                    singleLine = true
                )

                // Category Selector
                Text(
                    text = if (isTamil) "புகார் வகை (Category)" else "Issue Category",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    MaintenanceCategory.values().forEach { cat ->
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick = { selectedCategory = cat },
                            label = { Text(cat.getLocalized(isTamil), fontSize = 12.sp) },
                            leadingIcon = {
                                val icon = when (cat) {
                                    MaintenanceCategory.ELECTRICAL -> Icons.Default.Bolt
                                    MaintenanceCategory.PLUMBING -> Icons.Default.WaterDrop
                                    MaintenanceCategory.CARPENTER -> Icons.Default.Handyman
                                    MaintenanceCategory.OTHERS -> Icons.Default.Category
                                }
                                Icon(icon, contentDescription = null, modifier = Modifier.size(14.dp))
                            }
                        )
                    }
                }

                // Priority Level
                Text(
                    text = if (isTamil) "முன்னுரிமை நிலை (Priority Level)" else "Priority Level",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    MaintenancePriority.values().forEach { pri ->
                        val isSelected = selectedPriority == pri
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) Color(pri.colorValue) else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedPriority = pri }
                        ) {
                            Box(
                                modifier = Modifier.padding(vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = pri.getLocalized(isTamil),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                        }
                    }
                }

                // Description
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(if (isTamil) "விளக்கம் (Text Description)" else "Detailed Description") },
                    placeholder = { Text(if (isTamil) "பிரச்சனையை விரிவாக எழுதுங்கள்..." else "Explain where and when the issue occurs...") },
                    modifier = Modifier.fillMaxWidth().testTag("raise_issue_desc_input"),
                    minLines = 3
                )

                // Voice Recording Simulator
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = {
                                    if (!isRecording) {
                                        isRecording = true
                                        isVoiceRecorded = true
                                    } else {
                                        isRecording = false
                                    }
                                },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(if (isRecording) Color.Red else MaterialTheme.colorScheme.primary)
                            ) {
                                Icon(
                                    imageVector = if (isRecording) Icons.Default.Stop else Icons.Default.Mic,
                                    contentDescription = "Voice Record",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (isTamil) "குரல் பதிவு (Voice Recording)" else "Voice Description Note",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                                )
                                Text(
                                    text = if (isRecording) "Recording... (Tap stop)" else if (isVoiceRecorded) "Voice note attached (14s)" else "Tap mic to record audio note",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isRecording) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        if (isVoiceRecorded) {
                            Icon(Icons.Default.CheckCircle, contentDescription = "Attached", tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                }

                // Photo / Attachment simulator
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { attachedPhotoCount = (attachedPhotoCount % 3) + 1 },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.secondary)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CameraAlt,
                                    contentDescription = "Add Photo",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (isTamil) "புகைப்படங்கள் இணைக்க" else "Upload Photos / Videos",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                                )
                                Text(
                                    text = "$attachedPhotoCount photo(s) attached",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        FilledTonalButton(
                            onClick = { attachedPhotoCount++ },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            modifier = Modifier.height(28.dp)
                        ) {
                            Text("+ Add", fontSize = 11.sp)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val dateStr = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date())
                    val newIssue = MaintenanceIssue(
                        "ISS-2026-" + (1000 + (Math.random() * 9000).toInt()),
                        if (title.isBlank()) "Maintenance Complaint (${selectedCategory.name})" else title,
                        if (description.isBlank()) "Tenant reported a ${selectedCategory.name.lowercase()} issue requiring inspection." else description,
                        selectedCategory,
                        selectedPriority,
                        MaintenanceStatus.NEW,
                        0, // SUBMITTED
                        "prop_001",
                        "Skyline Heights #402",
                        "tenant_001",
                        tenantName,
                        "landlord_001",
                        dateStr,
                        dateStr,
                        List(attachedPhotoCount) { "photo_issue_$it.jpg" },
                        isVoiceRecorded,
                        if (isVoiceRecorded) 14 else 0,
                        if (isVoiceRecorded) "Voice transcription: User mentioned urgent fixing needed for " + selectedCategory.name else "",
                        null,
                        0.0,
                        "",
                        false,
                        0,
                        "",
                        CostResponsibility.LANDLORD_PAYS,
                        listOf("Property_Policy_Doc.pdf"),
                        false,
                        ""
                    )
                    onRaiseIssue(newIssue)
                    onDismiss()
                },
                modifier = Modifier.testTag("submit_complaint_button")
            ) {
                Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (isTamil) "சமர்ப்பி" else "Submit Complaint")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, modifier = Modifier.testTag("cancel_complaint_button")) {
                Text(if (isTamil) "ரத்து" else "Cancel")
            }
        }
    )
}
