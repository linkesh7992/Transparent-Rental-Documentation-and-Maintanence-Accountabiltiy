package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.backend.models.User

@Composable
fun EditProfileDialog(
    user: User,
    isTamil: Boolean,
    onSaveProfile: (String, String, String, String) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf(user.name) }
    var email by remember { mutableStateOf(user.email) }
    var phone by remember { mutableStateOf(user.phone) }
    var address by remember { mutableStateOf(user.address) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Edit, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isTamil) "சுயவிவரத்தை திருத்து" else "Edit Profile",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(if (isTamil) "பெயர்" else "Full Name") },
                    modifier = Modifier.fillMaxWidth().testTag("edit_name_input"),
                    singleLine = true
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(if (isTamil) "மின்னஞ்சல்" else "Email Address") },
                    modifier = Modifier.fillMaxWidth().testTag("edit_email_input"),
                    singleLine = true
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text(if (isTamil) "தொலைபேசி எண்" else "Mobile Number") },
                    modifier = Modifier.fillMaxWidth().testTag("edit_phone_input"),
                    singleLine = true
                )
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text(if (isTamil) "முகவரி" else "Address") },
                    modifier = Modifier.fillMaxWidth().testTag("edit_address_input"),
                    minLines = 2
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSaveProfile(name, email, phone, address)
                    onDismiss()
                },
                modifier = Modifier.testTag("save_profile_button")
            ) {
                Text(if (isTamil) "சேமி" else "Save Changes")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(if (isTamil) "ரத்து" else "Cancel")
            }
        }
    )
}
