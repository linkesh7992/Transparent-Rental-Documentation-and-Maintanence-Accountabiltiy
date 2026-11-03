package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import com.example.backend.models.PaymentRecord
import com.example.backend.models.UserRole

@Composable
fun ReceiptModal(
    record: PaymentRecord,
    isTamil: Boolean,
    activeRole: UserRole,
    onDigitalSign: () -> Unit,
    onRaiseDispute: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var showDisputeDialog by remember { mutableStateOf(false) }
    var disputeReason by remember { mutableStateOf("") }
    var isDownloaded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.ReceiptLong, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isTamil) "கட்டண ரசீது" else "Official Payment Receipt",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(record.paymentStatus.colorValue).copy(alpha = 0.15f)
                ) {
                    Text(
                        text = record.paymentStatus.getLocalized(isTamil),
                        color = Color(record.paymentStatus.colorValue),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Receipt Header Banner
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = record.paymentType.getLocalized(isTamil),
                            style = MaterialTheme.typography.labelMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "₹${record.amount.toInt()}",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Receipt No: ${record.receiptNumber}",
                            style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                    }
                }

                // Dispute Banner if active
                if (record.isHasDispute) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.errorContainer,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Gavel, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = if (isTamil) "விவாதம் / மறுப்பு நிலுவையில் உள்ளது" else "Payment Dispute Filed",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.error
                                )
                                Text(
                                    text = record.disputeReason,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                            }
                        }
                    }
                }

                // Transaction Details List
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = if (isTamil) "பரிவர்த்தனை விவரங்கள்" else "Transaction Details",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        ReceiptRow("Transaction ID", record.transactionId)
                        ReceiptRow("Date & Time", "${record.date}, ${record.time}")
                        ReceiptRow("Payment Method", record.paymentMethod.getLocalized(isTamil))
                        ReceiptRow("Tenant", record.tenantName)
                        ReceiptRow("Landlord", record.landlordName)
                        ReceiptRow("Property", record.propertyTitle)
                        if (record.isAutoPay) {
                            ReceiptRow("Auto-Pay Status", "Enabled (Recurring)")
                        }
                    }
                }

                // Digital Acknowledgement Dual Box
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = if (isTamil) "டிஜிட்டல் கையொப்ப ஏற்பு (Digital Ack)" else "Secure Digital Acknowledgement",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Tenant Sign
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Tenant Digital Ack (${record.tenantName})",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                                )
                                Text(
                                    text = if (record.isDigitalTenantAck) "Signed: ${record.tenantAckTime}" else "Signature pending",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (record.isDigitalTenantAck) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                                )
                            }
                            if (record.isDigitalTenantAck) {
                                Icon(Icons.Default.Verified, contentDescription = "Signed", tint = MaterialTheme.colorScheme.primary)
                            } else if (activeRole == UserRole.TENANT) {
                                TextButton(onClick = onDigitalSign) { Text("Sign Now") }
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))

                        // Landlord Sign
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Landlord Digital Ack (${record.landlordName})",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                                )
                                Text(
                                    text = if (record.isDigitalLandlordAck) "Confirmed: ${record.landlordAckTime}" else "Confirmation pending",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (record.isDigitalLandlordAck) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                                )
                            }
                            if (record.isDigitalLandlordAck) {
                                Icon(Icons.Default.Verified, contentDescription = "Confirmed", tint = MaterialTheme.colorScheme.primary)
                            } else if (activeRole == UserRole.LANDLORD) {
                                TextButton(onClick = onDigitalSign) { Text("Acknowledge") }
                            }
                        }
                    }
                }

                // Download / Export Statement Button
                OutlinedButton(
                    onClick = { isDownloaded = true },
                    modifier = Modifier.fillMaxWidth().testTag("download_receipt_button")
                ) {
                    Icon(
                        imageVector = if (isDownloaded) Icons.Default.Check else Icons.Default.Download,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        if (isDownloaded) (if (isTamil) "பதிவிறக்கப்பட்டது (PDF)" else "Receipt PDF Downloaded")
                        else (if (isTamil) "ரசீதை பதிவிறக்கு (PDF/Report)" else "Download PDF Receipt / Statement")
                    )
                }

                // Dispute Option
                if (!record.isHasDispute) {
                    TextButton(
                        onClick = { showDisputeDialog = true },
                        modifier = Modifier.fillMaxWidth().testTag("dispute_payment_button")
                    ) {
                        Text(
                            text = if (isTamil) "பரிவர்த்தனை மறுப்பு / பிரச்சனை எழுப்பு" else "Report Issue / Dispute Transaction",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss, modifier = Modifier.testTag("close_receipt_modal")) {
                Text(if (isTamil) "முடிந்தது" else "Done")
            }
        }
    )

    if (showDisputeDialog) {
        AlertDialog(
            onDismissRequest = { showDisputeDialog = false },
            title = { Text(if (isTamil) "பரிவர்த்தனை மறுப்பு" else "Raise Payment Dispute") },
            text = {
                Column {
                    Text(
                        text = if (isTamil) "மறுப்புக்கான காரணத்தை உள்ளிடவும்:" else "Please enter reason for dispute (e.g. wrong amount, double debit):",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = disputeReason,
                        onValueChange = { disputeReason = it },
                        modifier = Modifier.fillMaxWidth().testTag("dispute_reason_field"),
                        minLines = 2
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onRaiseDispute(if (disputeReason.isBlank()) "Tenant reported payment variance" else disputeReason)
                        showDisputeDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Submit Dispute")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDisputeDialog = false }) {
                    Text(if (isTamil) "ரத்து" else "Cancel")
                }
            }
        )
    }
}

@Composable
private fun ReceiptRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold))
    }
}
