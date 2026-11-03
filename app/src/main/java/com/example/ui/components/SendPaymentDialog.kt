package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.backend.models.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SendPaymentDialog(
    isTamil: Boolean,
    activeRole: UserRole,
    tenantName: String,
    landlordName: String,
    defaultAmount: Double,
    onSendPayment: (PaymentRecord) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedType by remember { mutableStateOf(PaymentType.MONTHLY_RENT) }
    var selectedMethod by remember { mutableStateOf(PaymentMethod.UPI) }
    var amountStr by remember { mutableStateOf(if (defaultAmount > 0) defaultAmount.toInt().toString() else "24500") }
    var enableAutoPay by remember { mutableStateOf(true) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Payment, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isTamil) {
                        if (activeRole == UserRole.TENANT) "பணம் செலுத்துதல்" else "கட்டணம் கோரிக்கை"
                    } else {
                        if (activeRole == UserRole.TENANT) "Make Secure Payment" else "Request Tenant Payment"
                    },
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
                // Payment Type Selection
                Text(
                    text = if (isTamil) "கட்டண வகை" else "Payment Type",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    PaymentType.values().forEach { type ->
                        FilterChip(
                            selected = selectedType == type,
                            onClick = { selectedType = type },
                            label = { Text(type.getLocalized(isTamil), fontSize = 11.sp) }
                        )
                    }
                }

                // Amount Field
                OutlinedTextField(
                    value = amountStr,
                    onValueChange = { amountStr = it },
                    label = { Text(if (isTamil) "தொகை (₹ Amount)" else "Amount (₹)") },
                    leadingIcon = { Text("₹", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 12.dp)) },
                    modifier = Modifier.fillMaxWidth().testTag("payment_amount_input"),
                    singleLine = true
                )

                // Payment Method
                Text(
                    text = if (isTamil) "செலுத்தும் முறை" else "Supported Payment Methods",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    PaymentMethod.values().forEach { method ->
                        val isSelected = selectedMethod == method
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                            onClick = { selectedMethod = method },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(selected = isSelected, onClick = { selectedMethod = method })
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = method.getLocalized(isTamil),
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                                )
                            }
                        }
                    }
                }

                // Auto-Pay Switch
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
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isTamil) "தானியங்கி மாதாந்திர கட்டணம் (Auto-Pay)" else "Auto-Pay / Recurring Monthly",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = if (isTamil) "மாதம் 5-ஆம் தேதி தானாகவே செலுத்தப்படும்" else "Auto-debit on 5th of every month",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = enableAutoPay,
                            onCheckedChange = { enableAutoPay = it },
                            modifier = Modifier.testTag("auto_pay_switch")
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amount = amountStr.toDoubleOrNull() ?: 24500.0
                    val dateStr = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
                    val timeStr = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
                    val txnId = "TXN/" + selectedMethod.name + "/" + System.currentTimeMillis().toString().takeLast(8)
                    val recNo = "REC-RW-" + (202600 + (Math.random() * 900).toInt())

                    val record = PaymentRecord(
                        "PAY-" + System.currentTimeMillis(),
                        "prop_001",
                        "Skyline Heights #402",
                        "tenant_001",
                        tenantName,
                        "landlord_001",
                        landlordName,
                        amount,
                        selectedType,
                        PaymentStatus.SUCCESSFUL,
                        selectedMethod,
                        txnId,
                        dateStr,
                        timeStr,
                        dateStr,
                        enableAutoPay,
                        recNo,
                        true,
                        true,
                        "$dateStr, $timeStr",
                        "$dateStr, $timeStr",
                        false,
                        "",
                        if (activeRole == UserRole.TENANT) "Tenant" else "Landlord",
                        if (selectedType == PaymentType.MONTHLY_RENT) amount else 0.0,
                        if (selectedType == PaymentType.SECURITY_DEPOSIT) amount else 0.0,
                        if (selectedType == PaymentType.MAINTENANCE) amount else 0.0,
                        if (selectedType == PaymentType.REPAIR_SERVICE) amount else 0.0,
                        0.0
                    )
                    onSendPayment(record)
                    onDismiss()
                },
                modifier = Modifier.testTag("confirm_payment_submission")
            ) {
                Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (isTamil) "உறுதி செய்து செலுத்து" else "Confirm & Pay Now")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(if (isTamil) "ரத்து" else "Cancel")
            }
        }
    )
}
