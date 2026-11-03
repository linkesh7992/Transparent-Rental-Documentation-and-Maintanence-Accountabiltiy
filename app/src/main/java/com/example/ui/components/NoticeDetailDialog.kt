package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.backend.models.NoticeItem
import com.example.backend.models.UserRole

@Composable
fun NoticeDetailDialog(
    notice: NoticeItem,
    isTamil: Boolean,
    activeRole: UserRole,
    onAcknowledge: () -> Unit,
    onDismiss: () -> Unit
) {
    val isAcknowledged = if (activeRole == UserRole.TENANT) notice.isAcknowledgedByTenant else notice.isAcknowledgedByLandlord

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Announcement, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = notice.type.getLocalized(isTamil),
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
                Text(
                    text = notice.title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Reminder: ${notice.reminderDays} Days",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Effective: ${notice.effectiveDate}",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }

                Text(
                    text = notice.content,
                    style = MaterialTheme.typography.bodyMedium
                )

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = if (isTamil) "ஏற்பு நிலை (Acknowledgement Status)" else "Acknowledgement Status",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Tenant Ack: " + if (notice.isAcknowledgedByTenant) "✓ Acknowledged (${notice.tenantAckTimestamp})" else "⏳ Pending",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (notice.isAcknowledgedByTenant) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                        )
                        Text(
                            text = "Landlord Ack: " + if (notice.isAcknowledgedByLandlord) "✓ Confirmed (${notice.landlordAckTimestamp})" else "⏳ Pending",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (notice.isAcknowledgedByLandlord) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        },
        confirmButton = {
            if (!isAcknowledged) {
                Button(onClick = {
                    onAcknowledge()
                    onDismiss()
                }, modifier = Modifier.testTag("ack_notice_button")) {
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (isTamil) "அறிவிப்பை ஏற்றுக்கொள்" else "Acknowledge Notice")
                }
            } else {
                Button(onClick = onDismiss) {
                    Text(if (isTamil) "மூடு" else "Close")
                }
            }
        },
        dismissButton = {
            if (!isAcknowledged) {
                TextButton(onClick = onDismiss) {
                    Text(if (isTamil) "ரத்து" else "Cancel")
                }
            }
        }
    )
}
