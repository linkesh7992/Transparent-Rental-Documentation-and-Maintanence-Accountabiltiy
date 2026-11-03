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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.backend.models.*
import com.example.ui.components.ReceiptModal
import com.example.ui.components.SendPaymentDialog
import com.example.viewmodel.MainUiState
import com.example.viewmodel.MainViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PaymentsScreen(
    viewModel: MainViewModel,
    uiState: MainUiState
) {
    val isTamil = uiState.currentLanguage == AppLanguage.TAMIL
    val isTenant = uiState.activeRole == UserRole.TENANT

    var selectedStatusFilter by remember { mutableStateOf<PaymentStatus?>(null) }
    var selectedTypeFilter by remember { mutableStateOf<PaymentType?>(null) }
    var showMakePaymentDialog by remember { mutableStateOf(false) }
    var selectedPaymentForReceipt by remember { mutableStateOf<PaymentRecord?>(null) }

    val filteredRecords = remember(
        uiState.paymentRecords,
        selectedStatusFilter,
        selectedTypeFilter
    ) {
        uiState.paymentRecords.filter { record ->
            val matchStatus = selectedStatusFilter == null || record.paymentStatus == selectedStatusFilter
            val matchType = selectedTypeFilter == null || record.paymentType == selectedTypeFilter
            matchStatus && matchType
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp)
    ) {
        // 1. Balance & Due Date Banner
        item {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isTamil) "நிலுவைத் தொகை" else "Outstanding Balance",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "Due: 05 Oct 2026",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "₹${uiState.outstandingBalance.toInt()}",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Action Buttons (Pay Now / Request Payment + Download Report)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { showMakePaymentDialog = true },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f).testTag("pay_now_button")
                        ) {
                            Icon(Icons.Default.Payment, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isTamil) (if (isTenant) "பணம் செலுத்து" else "கட்டணம் கோரு") else (if (isTenant) "Pay Now" else "Request Pay"),
                                fontWeight = FontWeight.Bold
                            )
                        }

                        OutlinedButton(
                            onClick = {
                                // Select latest payment to open receipt
                                uiState.paymentRecords.firstOrNull()?.let {
                                    selectedPaymentForReceipt = it
                                }
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f).testTag("download_statement_button")
                        ) {
                            Icon(Icons.Default.ReceiptLong, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (isTamil) "ரசீது அறிக்கை" else "Statement")
                        }
                    }
                }
            }
        }

        // 2. Breakdown Card (Page 3 Requirement)
        item {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = if (isTamil) "கட்டணப் பிரிவு விவரம்" else "Payment Breakdown (OCT 2026)",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    BreakdownRow(if (isTamil) "மாதாந்திர வாடகை" else "Monthly Rent", "₹24,500")
                    BreakdownRow(if (isTamil) "பராமரிப்பு கட்டணம்" else "Maintenance Charges", "₹1,800")
                    BreakdownRow(if (isTamil) "குழாய் பழுதுபார்ப்பு கட்டணம்" else "Plumbing Repair Shared", "₹850")
                    BreakdownRow(if (isTamil) "வைப்புத்தொகை நிலுவை" else "Security Deposit", "₹0")
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (isTamil) "மொத்த தொகை" else "Total Payable",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.ExtraBold)
                        )
                        Text(
                            text = "₹27,150",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }
            }
        }

        // 3. Supported Methods & Auto-Pay Quick Status
        item {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 1.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Autorenew, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isTamil) "தானியங்கி கட்டணம் (Auto-Pay)" else "Auto-Pay / Recurring Debit",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "Supported: UPI, Debit/Credit Card, Net Banking",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    var isAutoPayActive by remember { mutableStateOf(true) }
                    Switch(
                        checked = isAutoPayActive,
                        onCheckedChange = { isAutoPayActive = it },
                        modifier = Modifier.testTag("payments_autopay_toggle")
                    )
                }
            }
        }

        // 4. Payment History & Filter Chips (Page 4 Requirement)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isTamil) "பரிவர்த்தனை வரலாறு" else "Transaction History",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "${filteredRecords.size} records",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // Status Filters Row
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    item {
                        FilterChip(
                            selected = selectedStatusFilter == null,
                            onClick = { selectedStatusFilter = null },
                            label = { Text(if (isTamil) "அனைத்தும்" else "All Status", fontSize = 11.sp) }
                        )
                    }
                    items(PaymentStatus.values()) { status ->
                        FilterChip(
                            selected = selectedStatusFilter == status,
                            onClick = { selectedStatusFilter = if (selectedStatusFilter == status) null else status },
                            label = { Text(status.getLocalized(isTamil), fontSize = 11.sp) }
                        )
                    }
                }

                // Type Filters Row
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    item {
                        FilterChip(
                            selected = selectedTypeFilter == null,
                            onClick = { selectedTypeFilter = null },
                            label = { Text(if (isTamil) "அனைத்து வகைகள்" else "All Types", fontSize = 11.sp) }
                        )
                    }
                    items(PaymentType.values()) { type ->
                        FilterChip(
                            selected = selectedTypeFilter == type,
                            onClick = { selectedTypeFilter = if (selectedTypeFilter == type) null else type },
                            label = { Text(type.getLocalized(isTamil), fontSize = 11.sp) }
                        )
                    }
                }
            }
        }

        // 5. Payment Transaction Cards
        items(filteredRecords) { record ->
            PaymentItemCard(
                record = record,
                isTamil = isTamil,
                onClick = { selectedPaymentForReceipt = record }
            )
        }
    }

    if (showMakePaymentDialog) {
        SendPaymentDialog(
            isTamil = isTamil,
            activeRole = uiState.activeRole,
            tenantName = uiState.currentUser?.name ?: "Arun Kumar",
            landlordName = "Suresh Ramanathan",
            defaultAmount = uiState.outstandingBalance,
            onSendPayment = { viewModel.addPayment(it) },
            onDismiss = { showMakePaymentDialog = false }
        )
    }

    selectedPaymentForReceipt?.let { record ->
        ReceiptModal(
            record = record,
            isTamil = isTamil,
            activeRole = uiState.activeRole,
            onDigitalSign = {
                viewModel.toggleDigitalAck(record.id, !isTenant)
                selectedPaymentForReceipt = uiState.paymentRecords.find { it.id == record.id }
            },
            onRaiseDispute = { reason ->
                viewModel.raisePaymentDispute(record.id, reason)
                selectedPaymentForReceipt = uiState.paymentRecords.find { it.id == record.id }
            },
            onDismiss = { selectedPaymentForReceipt = null }
        )
    }
}

@Composable
private fun BreakdownRow(label: String, amount: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = amount, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold))
    }
}

@Composable
private fun PaymentItemCard(
    record: PaymentRecord,
    isTamil: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth().clickable { onClick() }.testTag("payment_record_${record.id}")
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color(record.paymentStatus.colorValue).copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                val icon = when (record.paymentStatus) {
                    PaymentStatus.SUCCESSFUL -> Icons.Default.Check
                    PaymentStatus.PENDING -> Icons.Default.HourglassTop
                    PaymentStatus.FAILED -> Icons.Default.Close
                    PaymentStatus.REFUNDED -> Icons.Default.Replay
                }
                Icon(icon, contentDescription = null, tint = Color(record.paymentStatus.colorValue), modifier = Modifier.size(20.dp))
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = record.paymentType.getLocalized(isTamil),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "${record.date} • ${record.paymentMethod.name}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (record.isDigitalTenantAck && record.isDigitalLandlordAck) {
                    Text(
                        text = "✓ Dual Digitally Signed",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "₹${record.amount.toInt()}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = if (record.paymentStatus == PaymentStatus.SUCCESSFUL) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                )
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color(record.paymentStatus.colorValue).copy(alpha = 0.15f)
                ) {
                    Text(
                        text = record.paymentStatus.getLocalized(isTamil),
                        color = Color(record.paymentStatus.colorValue),
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}
