package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GovIdVerificationDialog(
    isTamil: Boolean,
    onVerify: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedIdType by remember { mutableStateOf("Aadhaar Card") }
    var idNumber by remember { mutableStateOf("") }
    var isUploaded by remember { mutableStateOf(false) }

    val idTypes = listOf("Aadhaar Card", "Passport", "Driving License", "Voter ID")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isTamil) "அரசு அடையாள சரிபார்ப்பு" else "Government ID Verification",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = if (isTamil) "அடையாள வகையைத் தேர்ந்தெடுக்கவும்:" else "Select Government ID Document:",
                    style = MaterialTheme.typography.bodySmall
                )

                idTypes.forEach { type ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (selectedIdType == type) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                        onClick = { selectedIdType = type },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(selected = selectedIdType == type, onClick = { selectedIdType = type })
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(type, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }

                OutlinedTextField(
                    value = idNumber,
                    onValueChange = { idNumber = it },
                    label = { Text(if (isTamil) "ஆவண எண்" else "Document Number") },
                    placeholder = { Text("e.g. XXXX-XXXX-1234") },
                    modifier = Modifier.fillMaxWidth().testTag("gov_id_number_input"),
                    singleLine = true
                )

                OutlinedButton(
                    onClick = { isUploaded = true },
                    modifier = Modifier.fillMaxWidth().testTag("upload_id_doc_button")
                ) {
                    Icon(
                        imageVector = if (isUploaded) Icons.Default.VerifiedUser else Icons.Default.UploadFile,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (isUploaded) "Document Uploaded ✓" else "Upload ID Photo / Scan")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onVerify(selectedIdType)
                    onDismiss()
                },
                modifier = Modifier.testTag("submit_gov_id_verification")
            ) {
                Text(if (isTamil) "சரிபார் & ஒப்புதல் அளி" else "Verify & Approve")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(if (isTamil) "ரத்து" else "Cancel")
            }
        }
    )
}
