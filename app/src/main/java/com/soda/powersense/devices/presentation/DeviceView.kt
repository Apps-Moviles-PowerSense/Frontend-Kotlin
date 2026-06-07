package com.soda.powersense.devices.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Power
import androidx.compose.material.icons.filled.PowerOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.soda.powersense.devices.domain.model.Device

@Composable
fun DeviceView(
    viewModel: DeviceViewModel
) {
    val state by viewModel.state.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        if (state.isLoading && state.devices.isEmpty()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (state.error != null && state.devices.isEmpty()) {
            Text(
                text = state.error ?: "Unknown error",
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.error
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Text(
                        text = "Mis Dispositivos",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
                items(state.devices) { device ->
                    DeviceItem(
                        device = device,
                        onToggle = { viewModel.toggleDevice(device) }
                    )
                }
            }
        }
    }
}

@Composable
fun DeviceItem(
    device: Device,
    onToggle: () -> Unit
) {
    val isActive = device.status.lowercase() == "active"
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = device.name, style = MaterialTheme.typography.titleLarge)
                Text(text = "${device.roomName} • ${device.category}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "${device.watts}W", style = MaterialTheme.typography.bodySmall)
            }
            
            IconButton(onClick = onToggle) {
                Icon(
                    imageVector = if (isActive) Icons.Default.Power else Icons.Default.PowerOff,
                    contentDescription = if (isActive) "Apagar" else "Encender",
                    tint = if (isActive) Color.Green else Color.Gray
                )
            }
        }
    }
}
