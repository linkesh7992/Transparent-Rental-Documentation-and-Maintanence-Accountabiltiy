package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
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

@Composable
fun IssueDetailModal(
    issue: MaintenanceIssue,
    isTamil: Boolean,
    activeRole: UserRole,
    chatMessages: List<ChatMessage>,
    onSendMessage: (String) -> Unit,
    onAdvanceStep: () -> Unit,
    onEscalate: (String) -> Unit,
    onRateService: (Int, String) -> Unit,
    onDismiss: () -> Unit
) {
    var showChatTab by remember { mutableStateOf(false) }
    var chatInput by remember { mutableStateOf("") }
    var showEscalateDialog by remember { mutableStateOf(false) }
    var escalateReason by remember { mutableStateOf("") }
    var ratingInput by remember { mutableStateOf(if (issue.tenantRating > 0) issue.tenantRating else 5) }
    var feedbackInput by remember { mutableStateOf(issue.tenantRatingFeedback ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = issue.id,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(issue.priority.colorValue).copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = issue.priority.getLocalized(isTamil),
                            color = Color(issue.priority.colorValue),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
                Text(
                    text = issue.title,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 480.dp)
            ) {
                // Tab Switcher between "Overview & Timeline" and "Live Chat"
                TabRow(
                    selectedTabIndex = if (showChatTab) 1 else 0,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Tab(
                        selected = !showChatTab,
                        onClick = { showChatTab = false },
                        text = { Text(if (isTamil) "விவரம் & காலவரிசை" else "Overview & Timeline", fontSize = 12.sp) }
                    )
                    Tab(
                        selected = showChatTab,
                        onClick = { showChatTab = true },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(if (isTamil) "அரட்டை" else "Chat", fontSize = 12.sp)
                                if (chatMessages.isNotEmpty()) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Badge { Text("${chatMessages.size}") }
                                }
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (!showChatTab) {
                    // OVERVIEW TAB
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Status & Escalation banner
                        if (issue.isEscalated) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.errorContainer,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = if (isTamil) "⚠️ அதிமுக்கிய அவசர தீர்வுக்காக தீவிரப்படுத்தப்பட்டது" else "⚠️ Escalated to Urgent Resolution",
                                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.error
                                        )
                                        if (!issue.escalationReason.isNullOrBlank()) {
                                            Text(
                                                text = issue.escalationReason,
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.onErrorContainer
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // 9-STEP TIMELINE TRACKER
                        Text(
                            text = if (isTamil) "9-கட்ட புகார் காலவரிசை (Timeline)" else "9-Step Progress Timeline",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )

                        val allSteps = MaintenanceTimelineStep.values()
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(10.dp))
                                .padding(10.dp)
                        ) {
                            allSteps.forEachIndexed { index, step ->
                                val isDone = index <= issue.currentTimelineStepIndex
                                val isCurrent = index == issue.currentTimelineStepIndex

                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (isDone) MaterialTheme.colorScheme.primary
                                                else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (isDone) {
                                            Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                                        } else {
                                            Text("${index + 1}", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "${index + 1}. ${step.getLocalized(isTamil)}",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isCurrent) MaterialTheme.colorScheme.primary else if (isDone) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                                        ),
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }

                            // Advance Step Button (Useful for testing workflows)
                            Spacer(modifier = Modifier.height(8.dp))
                            FilledTonalButton(
                                onClick = onAdvanceStep,
                                modifier = Modifier.fillMaxWidth().testTag("advance_step_button"),
                                enabled = issue.currentTimelineStepIndex < 8
                            ) {
                                Icon(Icons.Default.FastForward, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isTamil) "அடுத்த கட்டத்திற்கு நகர்த்து" else "Advance Progress Step",
                                    fontSize = 12.sp
                                )
                            }
                        }

                        // Technician Assigned Details
                        if (issue.assignedTechnician != null) {
                            val tech = issue.assignedTechnician
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = if (isTamil) "ஒதுக்கப்பட்ட பணியாளர் விவரம்" else "Assigned Technician Details",
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(CircleShape)
                                                .background(MaterialTheme.colorScheme.primary),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(Icons.Default.Engineering, contentDescription = null, tint = Color.White)
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(tech.name, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                                            Text("${tech.specialty} • ★ ${tech.rating}", style = MaterialTheme.typography.labelSmall)
                                        }
                                        IconButton(onClick = {}) {
                                            Icon(Icons.Default.Phone, contentDescription = "Call", tint = MaterialTheme.colorScheme.primary)
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Visit Time: ${tech.expectedVisitTime}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }

                        // Repair Bill & Cost Responsibility
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (isTamil) "பழுதுபார்ப்பு ரசீது & பொறுப்பு" else "Repair Bill & Responsibility",
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = "₹${issue.repairBillAmount.toInt()}",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Responsibility: ${issue.costResponsibility.getLocalized(isTamil)}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    AssistChip(
                                        onClick = {},
                                        label = { Text("Invoice Download", fontSize = 10.sp) },
                                        leadingIcon = { Icon(Icons.Default.FileDownload, contentDescription = null, modifier = Modifier.size(12.dp)) }
                                    )
                                    AssistChip(
                                        onClick = {},
                                        label = { Text("Warranty Card", fontSize = 10.sp) },
                                        leadingIcon = { Icon(Icons.Default.VerifiedUser, contentDescription = null, modifier = Modifier.size(12.dp)) }
                                    )
                                }
                            }
                        }

                        // Service Rating (1-5 Stars)
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = if (isTamil) "சேவை மதிப்பீடு (Service Rating)" else "Tenant Service Rating",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    (1..5).forEach { star ->
                                        IconButton(
                                            onClick = {
                                                ratingInput = star
                                                onRateService(star, feedbackInput)
                                            },
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(
                                                imageVector = if (star <= ratingInput) Icons.Filled.Star else Icons.Outlined.StarBorder,
                                                contentDescription = "$star star",
                                                tint = if (star <= ratingInput) Color(0xFFFFB703) else MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                                if (issue.tenantRatingFeedback.isNotBlank()) {
                                    Text(
                                        text = "\"${issue.tenantRatingFeedback}\"",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        // Escalate Button
                        if (!issue.isEscalated && issue.currentTimelineStepIndex < 8) {
                            OutlinedButton(
                                onClick = { showEscalateDialog = true },
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                                modifier = Modifier.fillMaxWidth().testTag("escalate_issue_button")
                            ) {
                                Icon(Icons.Default.ReportProblem, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(if (isTamil) "புகாரை தீவிரமாக்கு (Escalate)" else "Escalate Unresolved Issue")
                            }
                        }
                    }
                } else {
                    // CHAT TAB (Tenant <-> Landlord)
                    Column(modifier = Modifier.fillMaxSize()) {
                        LazyColumn(
                            modifier = Modifier.weight(1f).fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            items(chatMessages) { msg ->
                                val isMe = (activeRole == msg.senderRole)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(
                                            topStart = 12.dp,
                                            topEnd = 12.dp,
                                            bottomStart = if (isMe) 12.dp else 2.dp,
                                            bottomEnd = if (isMe) 2.dp else 12.dp
                                        ),
                                        color = if (isMe) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                        modifier = Modifier.widthIn(max = 240.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(8.dp)) {
                                            Text(
                                                text = "${msg.senderName} (${msg.senderRole.name})",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isMe) Color.White.copy(alpha = 0.8f) else MaterialTheme.colorScheme.primary
                                                )
                                            )
                                            Text(
                                                text = msg.message,
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    color = if (isMe) Color.White else MaterialTheme.colorScheme.onSurface
                                                )
                                            )
                                            Text(
                                                text = msg.timestamp,
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontSize = 8.sp,
                                                    color = if (isMe) Color.White.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onSurfaceVariant
                                                ),
                                                modifier = Modifier.align(Alignment.End)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Chat Input Bar
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = chatInput,
                                onValueChange = { chatInput = it },
                                placeholder = { Text(if (isTamil) "செய்தியை எழுதுங்கள்..." else "Type message...") },
                                modifier = Modifier.weight(1f).testTag("chat_input_field"),
                                maxLines = 2
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            IconButton(
                                onClick = {
                                    if (chatInput.isNotBlank()) {
                                        onSendMessage(chatInput)
                                        chatInput = ""
                                    }
                                },
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary)
                                    .testTag("send_chat_button")
                            ) {
                                Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss, modifier = Modifier.testTag("close_issue_detail")) {
                Text(if (isTamil) "மூடு" else "Close")
            }
        }
    )

    if (showEscalateDialog) {
        AlertDialog(
            onDismissRequest = { showEscalateDialog = false },
            title = { Text(if (isTamil) "புகாரை தீவிரப்படுத்துதல்" else "Escalate Issue") },
            text = {
                Column {
                    Text(
                        text = if (isTamil) "காரணத்தை குறிப்பிடவும்:" else "State the escalation reason (e.g. no response in 24h, hazard):",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = escalateReason,
                        onValueChange = { escalateReason = it },
                        modifier = Modifier.fillMaxWidth().testTag("escalate_reason_input"),
                        minLines = 2
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onEscalate(if (escalateReason.isBlank()) "Automated escalation due to resolution SLA timeout" else escalateReason)
                        showEscalateDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Escalate")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEscalateDialog = false }) {
                    Text(if (isTamil) "ரத்து" else "Cancel")
                }
            }
        )
    }
}
