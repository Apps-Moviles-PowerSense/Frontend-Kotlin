package com.soda.powersense.devices.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soda.powersense.devices.domain.model.Device

@Composable
fun getPowerSenseSwitchColors() = SwitchDefaults.colors(
    checkedThumbColor = Color.White,
    checkedTrackColor = Color(0xFF81C784),
    uncheckedThumbColor = Color.White,
    uncheckedTrackColor = Color(0xFFDFE3E8),
    uncheckedBorderColor = Color.Transparent
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceView(
    viewModel: DeviceViewModel
) {
    val state by viewModel.state.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F9FA))) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            item {
                Column {
                    Text(
                        text = "Dispositivos",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF454F5B)
                    )
                    Text(
                        text = "Gestiona y controla todos los dispositivos conectados",
                        fontSize = 14.sp,
                        color = Color(0xFF919EAB)
                    )
                }
            }

            // Add Device Button
            item {
                Button(
                    onClick = { /* TODO */ },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7CB342)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Agregar Dispositivo", fontWeight = FontWeight.Bold)
                }
            }

            // Summary Grid
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        SummaryStatCard(
                            label = "Total Dispositivos",
                            value = state.totalCount.toString(),
                            icon = Icons.Default.Bolt,
                            color = Color(0xFF2196F3),
                            modifier = Modifier.weight(1f)
                        )
                        SummaryStatCard(
                            label = "Activos",
                            value = state.activeCount.toString(),
                            icon = Icons.Default.Check,
                            color = Color(0xFF4CAF50),
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        SummaryStatCard(
                            label = "Inactivos",
                            value = state.inactiveCount.toString(),
                            icon = Icons.Default.Circle,
                            color = Color(0xFFB39DDB),
                            modifier = Modifier.weight(1f)
                        )
                        SummaryStatCard(
                            label = "Consumo Actual",
                            value = if (state.totalConsumptionWatts >= 1000) 
                                String.format("%.1f kW", state.totalConsumptionWatts / 1000.0) 
                                else "${state.totalConsumptionWatts}W",
                            icon = Icons.Default.Bolt,
                            color = Color(0xFFFFB300),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Filters
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        placeholder = { Text("Buscar dispositivos...", fontSize = 14.sp) },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null, modifier = Modifier.size(20.dp)) },
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White,
                            unfocusedBorderColor = Color(0xFFDFE3E8)
                        )
                    )
                    
                    FilterDropdown("Todas las habitaciones")
                    FilterDropdown("Todos los tipos")
                }
            }

            // Master Controls
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { viewModel.setAllDevicesStatus(false) },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE57373)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Apagar Todo", fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = { viewModel.setAllDevicesStatus(true) },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF81C784)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Encender Todo", fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Device List
            items(state.devices) { device ->
                DeviceItemCard(
                    device = device,
                    onToggle = { viewModel.toggleDevice(device) }
                )
            }

            // Room Control Section
            item {
                Text(
                    text = "Control por Habitación",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF454F5B),
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
            }

            items(state.rooms) { room ->
                RoomControlCard(
                    room = room,
                    onTurnOff = { viewModel.setRoomDevicesStatus(room.id, false) },
                    onTurnOn = { viewModel.setRoomDevicesStatus(room.id, true) }
                )
            }
            
            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }
}

@Composable
fun SummaryStatCard(label: String, value: String, icon: ImageVector, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(color = color.copy(alpha = 0.1f), shape = androidx.compose.foundation.shape.CircleShape) {
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.padding(8.dp).size(20.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212B36))
            Text(text = label, fontSize = 11.sp, color = Color(0xFF919EAB))
        }
    }
}

@Composable
fun FilterDropdown(label: String) {
    OutlinedTextField(
        value = label,
        onValueChange = {},
        readOnly = true,
        modifier = Modifier.fillMaxWidth().height(52.dp),
        trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) },
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            unfocusedBorderColor = Color(0xFFDFE3E8)
        )
    )
}

@Composable
fun DeviceItemCard(device: Device, onToggle: () -> Unit) {
    val isActive = device.status.lowercase() == "active"
    val icon = when(device.category.uppercase()) {
        "LIGHT" -> Icons.Default.Lightbulb
        "AC" -> Icons.Default.Air
        "TV" -> Icons.Default.Tv
        "REFRIGERATOR" -> Icons.Default.Coffee
        "HEATING" -> Icons.Default.DeviceThermostat
        "COMPUTER" -> Icons.Default.Computer
        else -> Icons.Default.Bolt
    }
    val iconColor = when(device.category.uppercase()) {
        "LIGHT" -> Color(0xFFFFB300)
        "AC" -> Color(0xFF42A5F5)
        "TV" -> Color(0xFFB39DDB)
        "REFRIGERATOR" -> Color(0xFF66BB6A)
        else -> Color(0xFFEF5350)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(color = iconColor, shape = RoundedCornerShape(12.dp)) {
                    Icon(imageVector = icon, contentDescription = null, tint = Color.White, modifier = Modifier.padding(12.dp).size(24.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = device.name, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212B36))
                    Text(text = device.roomName, fontSize = 14.sp, color = Color(0xFF919EAB))
                }
                Switch(
                    checked = isActive,
                    onCheckedChange = { onToggle() },
                    colors = getPowerSenseSwitchColors()
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Consumo", fontSize = 12.sp, color = Color(0xFF919EAB))
                Text(text = "${device.watts}W", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF454F5B))
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            LinearProgressIndicator(
                progress = { if (isActive) (device.watts / 2000f).coerceIn(0f, 1f) else 0f },
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                color = Color(0xFF81C784),
                trackColor = Color(0xFFE8F5E9)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedButton(
                onClick = { /* TODO */ },
                modifier = Modifier.fillMaxWidth().height(40.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFFF4F6F8), contentColor = Color(0xFF637381)),
                border = null
            ) {
                Text("Configurar", fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun RoomControlCard(room: RoomSummary, onTurnOff: () -> Unit, onTurnOn: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Room Info
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(color = Color(0xFFF4F6F8), shape = RoundedCornerShape(8.dp)) {
                    Icon(imageVector = Icons.Default.Home, contentDescription = null, tint = Color(0xFF637381), modifier = Modifier.padding(8.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = room.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212B36))
                    Text(text = "${room.activeDevicesCount} dispositivos activos", fontSize = 12.sp, color = Color(0xFF919EAB))
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onTurnOff,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFEBEE), contentColor = Color(0xFFE57373)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f).height(40.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("Apagar todo", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = onTurnOn,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE8F5E9), contentColor = Color(0xFF81C784)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f).height(40.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("Encender todo", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Consumption Info
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Consumo total", fontSize = 12.sp, color = Color(0xFF919EAB))
                Text(
                    text = String.format("%.2fkW", room.totalConsumption / 1000.0),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF454F5B)
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            LinearProgressIndicator(
                progress = { (room.totalConsumption / 5000f).coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                color = Color(0xFF81C784),
                trackColor = Color(0xFFE8F5E9)
            )
        }
    }
}
