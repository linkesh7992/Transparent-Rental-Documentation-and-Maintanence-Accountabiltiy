package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.ui.components.IssueDetailModal
import com.example.ui.components.RaiseIssueDialog
import com.example.viewmodel.MainUiState
import com.example.viewmodel.MainViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MaintenanceScreen(
    viewModel: MainViewModel,
    uiState: MainUiState
) {
    val isTamil = uiState.currentLanguage == AppLanguage.TAMIL
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<MaintenanceCategory?>(null) }
    var selectedPriority by remember { mutableStateOf<MaintenancePriority?>(null) }
    var selectedStatus by remember { mutableStateOf<MaintenanceStatus?>(null) }
    var showRaiseDialog by remember { mutableStateOf(false) }
    var selectedIssueForModal by remember { mutableStateOf<MaintenanceIssue?>(null) }

    val filteredIssues = remember(
        uiState.maintenanceIssues,
        searchQuery,
        selectedCategory,
        selectedPriority,
        selectedStatus
    ) {
        uiState.maintenanceIssues.filter { issue ->
            val matchesQuery = searchQuery.isBlank() ||
                    issue.title.contains(searchQuery, ignoreCase = true) ||
                    issue.description.contains(searchQuery, ignoreCase = true) ||
                    issue.id.contains(searchQuery, ignoreCase = true)

            val matchesCategory = selectedCategory == null || issue.category == selectedCategory
            val matchesPriority = selectedPriority == null || issue.priority == selectedPriority
            val matchesStatus = selectedStatus == null || issue.status == selectedStatus

            matchesQuery && matchesCategory && matchesPriority && matchesStatus
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showRaiseDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                modifier = Modifier.testTag("raise_issue_fab")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Raise Issue")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isTamil) "புதிய புகார்" else "Raise Issue",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp)
        ) {
            // 1. Metric Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MetricCard(
                        title = if (isTamil) "செயலில் உள்ளவை" else "Active Issues",
                        count = "${uiState.activeMaintenanceCount}",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = if (isTamil) "அவசர புகார்கள்" else "High Priority",
                        count = "${uiState.maintenanceIssues.count { it.priority == MaintenancePriority.HIGH || it.priority == MaintenancePriority.EMERGENCY }}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = if (isTamil) "முழுமை பெற்றது" else "Resolved",
                        count = "${uiState.maintenanceIssues.count { it.status == MaintenanceStatus.COMPLETED }}",
                        color = Color(0xFF06D6A0),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // 2. Search & Filters
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text(if (isTamil) "புகார் எண், வகை மூலம் தேடு..." else "Search issues by ID, title, keyword...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) { Icon(Icons.Default.Clear, contentDescription = null) }
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().testTag("maintenance_search_input"),
                        singleLine = true
                    )

                    // Categories Filter Row
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        item {
                            FilterChip(
                                selected = selectedCategory == null,
                                onClick = { selectedCategory = null },
                                label = { Text(if (isTamil) "அனைத்து வகைகள்" else "All Categories", fontSize = 11.sp) }
                            )
                        }
                        items(MaintenanceCategory.values()) { cat ->
                            FilterChip(
                                selected = selectedCategory == cat,
                                onClick = { selectedCategory = if (selectedCategory == cat) null else cat },
                                label = { Text(cat.getLocalized(isTamil), fontSize = 11.sp) }
                            )
                        }
                    }

                    // Priority Filter Row
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        item {
                            FilterChip(
                                selected = selectedPriority == null,
                                onClick = { selectedPriority = null },
                                label = { Text(if (isTamil) "அனைத்து முன்னுரிமை" else "All Priorities", fontSize = 11.sp) }
                            )
                        }
                        items(MaintenancePriority.values()) { pri ->
                            FilterChip(
                                selected = selectedPriority == pri,
                                onClick = { selectedPriority = if (selectedPriority == pri) null else pri },
                                label = { Text(pri.getLocalized(isTamil), fontSize = 11.sp) }
                            )
                        }
                    }
                }
            }

            // 3. Issue List
            if (filteredIssues.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.CheckCircleOutline,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (isTamil) "பராமரிப்பு புகார்கள் எதுவும் இல்லை" else "No matching maintenance issues",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(filteredIssues) { issue ->
                    MaintenanceIssueCard(
                        issue = issue,
                        isTamil = isTamil,
                        activeRole = uiState.activeRole,
                        onClick = { selectedIssueForModal = issue }
                    )
                }
            }
        }
    }

    if (showRaiseDialog) {
        RaiseIssueDialog(
            isTamil = isTamil,
            tenantName = uiState.currentUser?.name ?: "Arun Kumar",
            onRaiseIssue = { viewModel.addMaintenanceIssue(it) },
            onDismiss = { showRaiseDialog = false }
        )
    }

    selectedIssueForModal?.let { issue ->
        val chatMessages = viewModel.getChatMessages(issue.id)
        IssueDetailModal(
            issue = issue,
            isTamil = isTamil,
            activeRole = uiState.activeRole,
            chatMessages = chatMessages,
            onSendMessage = { msg -> viewModel.sendChatMessage(issue.id, msg) },
            onAdvanceStep = {
                viewModel.advanceMaintenanceStep(issue.id)
                // Refresh modal issue reference
                selectedIssueForModal = uiState.maintenanceIssues.find { it.id == issue.id }
            },
            onEscalate = { reason ->
                viewModel.escalateIssue(issue.id, reason)
                selectedIssueForModal = uiState.maintenanceIssues.find { it.id == issue.id }
            },
            onRateService = { rating, feedback ->
                viewModel.rateMaintenanceService(issue.id, rating, feedback)
            },
            onDismiss = { selectedIssueForModal = null }
        )
    }
}

@Composable
private fun MetricCard(title: String, count: String, color: Color, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(count, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold, color = color))
            Text(title, style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun MaintenanceIssueCard(
    issue: MaintenanceIssue,
    isTamil: Boolean,
    activeRole: UserRole,
    onClick: () -> Unit
) {
    val allSteps = MaintenanceTimelineStep.values()
    val currentStepName = allSteps.getOrNull(issue.currentTimelineStepIndex)?.getLocalized(isTamil) ?: "In Progress"

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("issue_card_${issue.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Row: ID, Category icon, Priority Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val catIcon = when (issue.category) {
                        MaintenanceCategory.ELECTRICAL -> Icons.Default.Bolt
                        MaintenanceCategory.PLUMBING -> Icons.Default.WaterDrop
                        MaintenanceCategory.CARPENTER -> Icons.Default.Handyman
                        MaintenanceCategory.OTHERS -> Icons.Default.Build
                    }
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(catIcon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = issue.id,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(issue.priority.colorValue).copy(alpha = 0.15f)
                ) {
                    Text(
                        text = issue.priority.getLocalized(isTamil),
                        color = Color(issue.priority.colorValue),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = issue.title,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
            )

            Text(
                text = issue.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Timeline Step Progress Indicator (Step X of 9)
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Timeline,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Step ${issue.currentTimelineStepIndex + 1}/9: $currentStepName",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold, fontSize = 10.sp)
                        )
                    }
                    Text(
                        text = issue.status.getLocalized(isTamil),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 10.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Footer Details (Date, Tech, Chat indicator)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = issue.createdAt,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (issue.isEscalated) {
                        Text(
                            text = "⚠️ ESCALATED",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.error, fontSize = 9.sp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    if (issue.isHasVoiceRecording) {
                        Icon(Icons.Default.Mic, contentDescription = "Voice", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                    }

                    Icon(Icons.Default.ChatBubbleOutline, contentDescription = "Chat", tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(14.dp))
                }
            }
        }
    }
}
