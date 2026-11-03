package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.backend.models.Property
import java.util.*

@Composable
fun AddPropertyDialog(
    isTamil: Boolean,
    landlordName: String,
    landlordPhone: String,
    onAddProperty: (Property) -> Unit,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var titleTamil by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("Chennai") }
    var priceStr by remember { mutableStateOf("") }
    var depositStr by remember { mutableStateOf("") }
    var propertyType by remember { mutableStateOf("Apartment") }
    var bedroomsStr by remember { mutableStateOf("2") }
    var bathroomsStr by remember { mutableStateOf("2") }
    var sqftStr by remember { mutableStateOf("1100") }
    var furnishing by remember { mutableStateOf("Semi-Furnished") }
    var description by remember { mutableStateOf("") }

    val propertyTypes = listOf("Apartment", "Villa", "Independent House", "Studio")
    val furnishingOptions = listOf("Fully Furnished", "Semi-Furnished", "Unfurnished")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Home, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isTamil) "புதிய சொத்து சேர்க்க" else "Add New Property Listing",
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
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(if (isTamil) "சொத்து தலைப்பு (English)" else "Property Title (English)") },
                    modifier = Modifier.fillMaxWidth().testTag("add_prop_title_input"),
                    singleLine = true
                )

                OutlinedTextField(
                    value = titleTamil,
                    onValueChange = { titleTamil = it },
                    label = { Text(if (isTamil) "சொத்து தலைப்பு (தமிழ்)" else "Property Title (Tamil)") },
                    modifier = Modifier.fillMaxWidth().testTag("add_prop_title_ta_input"),
                    singleLine = true
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = priceStr,
                        onValueChange = { priceStr = it },
                        label = { Text(if (isTamil) "வாடகை (₹/mo)" else "Rent (₹/mo)") },
                        modifier = Modifier.weight(1f).testTag("add_prop_rent_input"),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = depositStr,
                        onValueChange = { depositStr = it },
                        label = { Text(if (isTamil) "வைப்புத்தொகை (₹)" else "Deposit (₹)") },
                        modifier = Modifier.weight(1f).testTag("add_prop_deposit_input"),
                        singleLine = true
                    )
                }

                Text(
                    text = if (isTamil) "சொத்து வகை" else "Property Type",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    propertyTypes.forEach { type ->
                        FilterChip(
                            selected = propertyType == type,
                            onClick = { propertyType = type },
                            label = { Text(type, style = MaterialTheme.typography.labelSmall) }
                        )
                    }
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = bedroomsStr,
                        onValueChange = { bedroomsStr = it },
                        label = { Text("BHK") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = bathroomsStr,
                        onValueChange = { bathroomsStr = it },
                        label = { Text("Baths") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = sqftStr,
                        onValueChange = { sqftStr = it },
                        label = { Text("Sq.Ft") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text(if (isTamil) "முகவரி" else "Address & Area") },
                    modifier = Modifier.fillMaxWidth().testTag("add_prop_address_input"),
                    singleLine = true
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(if (isTamil) "விளக்கம்" else "Description") },
                    modifier = Modifier.fillMaxWidth().testTag("add_prop_desc_input"),
                    minLines = 2
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val price = priceStr.toDoubleOrNull() ?: 18000.0
                    val deposit = depositStr.toDoubleOrNull() ?: (price * 5)
                    val bedrooms = bedroomsStr.toIntOrNull() ?: 2
                    val bathrooms = bathroomsStr.toIntOrNull() ?: 2
                    val sqft = sqftStr.toIntOrNull() ?: 1000

                    val newProperty = Property(
                        "prop_" + System.currentTimeMillis(),
                        if (title.isBlank()) "Spacious $bedrooms BHK $propertyType" else title,
                        if (titleTamil.isBlank()) "வசதியான $bedrooms BHK வீடு" else titleTamil,
                        if (description.isBlank()) "Newly listed rental property in prime location." else description,
                        if (address.isBlank()) "Main Road, Chennai" else address,
                        city,
                        price,
                        deposit,
                        propertyType,
                        bedrooms,
                        bathrooms,
                        sqft,
                        true,
                        true,
                        true,
                        5.0,
                        1,
                        "landlord_001",
                        landlordName,
                        landlordPhone,
                        "",
                        false,
                        false,
                        listOf("Lift", "Car Parking", "24/7 Water", "Security"),
                        listOf("new_listing"),
                        furnishing
                    )
                    onAddProperty(newProperty)
                    onDismiss()
                },
                modifier = Modifier.testTag("submit_add_property_button")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (isTamil) "சேர்" else "Add Listing")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(if (isTamil) "ரத்து" else "Cancel")
            }
        }
    )
}
