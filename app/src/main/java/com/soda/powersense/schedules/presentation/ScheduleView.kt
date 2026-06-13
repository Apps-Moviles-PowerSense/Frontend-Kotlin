package com.soda.powersense.schedules.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.soda.powersense.devices.presentation.getPowerSenseSwitchColors
import com.soda.powersense.schedules.domain.model.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleView(
    viewModel: ScheduleViewModel
) {
    val state by viewModel.state.collectAsState()
    var showRoomMenu by remember { mutableStateOf(false) }

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
                        text = "Programación",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF454F5B)
                    )
                    Text(
                        text = "Automatiza el encendido y apagado de tus dispositivos",
                        fontSize = 14.sp,
                        color = Color(0xFF919EAB)
                    )
                }
            }

            // New Schedule Button
            item {
                Button(
                    onClick = { viewModel.onOpenCreateDialog() },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7CB342)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Nueva Programacion", fontWeight = FontWeight.Bold)
                }
            }

            // Tabs/Filter Chips
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Dispositivos", "Habitaciones", "Areas Comunes", "Reglas").forEach { tab ->
                        ScheduleFilterChip(
                            label = tab, 
                            selected = state.selectedTab == tab,
                            onClick = { viewModel.onTabSelected(tab) }
                        )
                    }
                }
            }

            // Search and Room filter
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = state.searchQuery,
                        onValueChange = viewModel::onSearchQueryChange,
                        placeholder = { Text("Buscar dispositivo...", fontSize = 14.sp) },
                        modifier = Modifier.weight(1f).height(48.dp),
                        leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null, modifier = Modifier.size(18.dp)) },
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White,
                            unfocusedBorderColor = Color(0xFFDFE3E8)
                        ),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Box {
                        OutlinedButton(
                            onClick = { showRoomMenu = true },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(48.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF637381))
                        ) {
                            Text(state.selectedRoom ?: "Habitaciones", fontSize = 12.sp)
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                        
                        DropdownMenu(
                            expanded = showRoomMenu,
                            onDismissRequest = { showRoomMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Todas") },
                                onClick = { 
                                    viewModel.onRoomSelected(null)
                                    showRoomMenu = false 
                                }
                            )
                            state.rooms.forEach { room ->
                                DropdownMenuItem(
                                    text = { Text(room) },
                                    onClick = { 
                                        viewModel.onRoomSelected(room)
                                        showRoomMenu = false 
                                    }
                                )
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Programaciones Activas",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF454F5B)
                )
            }

            // Active Schedules
            if (state.filteredSchedules.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "No se encontraron programaciones con los filtros actuales", 
                            color = Color.Gray,
                            modifier = Modifier.padding(24.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            } else {
                items(state.filteredSchedules) { schedule ->
                    ScheduleItemCard(
                        schedule = schedule,
                        onToggle = { viewModel.toggleSchedule(schedule) }
                    )
                }
            }

            // Quick Schedules
            item {
                Text(
                    text = "Programación Rápida",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF454F5B),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        state.quickSchedules.forEach { quick ->
                            QuickScheduleItem(quick)
                        }
                    }
                }
            }

            // Statistics
            item {
                Text(
                    text = "Estadísticas",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF454F5B)
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        StatRow("Dispositivos programados", state.stats.scheduledDevices)
                        StatRow("Horarios activos", state.stats.activeSchedules.toString())
                        StatRow("Ahorro estimado", "${state.stats.estimatedSavings}%", Color(0xFF4CAF50))
                    }
                }
            }

            // Smart Rules
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF66BB6A))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("Reglas Inteligentes", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        SmartRuleItem("Modo Nocturno", "Apaga luces automáticamente")
                        SmartRuleItem("Ahorro de Energía", "Optimiza según tarifas")
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { /* TODO */ },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Agregar Regla", color = Color.White)
                        }
                    }
                }
            }
            
            item { Spacer(modifier = Modifier.height(20.dp)) }
        }

        if (state.isCreateDialogOpen) {
            CreateScheduleDialog(
                devices = state.availableDevices,
                onDismiss = { viewModel.onCloseCreateDialog() },
                onConfirm = { deviceId, start, end, days ->
                    viewModel.createSchedule(deviceId, start, end, days)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateScheduleDialog(
    devices: List<com.soda.powersense.devices.domain.model.Device>,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, List<String>) -> Unit
) {
    var selectedDeviceId by remember { mutableStateOf(devices.firstOrNull()?.id ?: "") }
    var startTime by remember { mutableStateOf("08:00") }
    var endTime by remember { mutableStateOf("18:00") }
    val selectedDays = remember { mutableStateListOf("MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY") }
    
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    val daysOfWeek = listOf(
        "MONDAY" to "Lun", "TUESDAY" to "Mar", "WEDNESDAY" to "Mié", 
        "THURSDAY" to "Jue", "FRIDAY" to "Vie", "SATURDAY" to "Sáb", "SUNDAY" to "Dom"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva Programación", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Device Selector
                Column {
                    Text("Dispositivo", style = MaterialTheme.typography.labelMedium)
                    var expanded by remember { mutableStateOf(false) }
                    val selectedDeviceName = devices.find { it.id == selectedDeviceId }?.name ?: "Seleccionar"
                    
                    Box {
                        OutlinedButton(
                            onClick = { expanded = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.DarkGray)
                        ) {
                            Text(selectedDeviceName)
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(Icons.Default.ArrowDropDown, null)
                        }
                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            devices.forEach { device ->
                                DropdownMenuItem(
                                    text = { Text(device.name) },
                                    onClick = {
                                        selectedDeviceId = device.id
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Time Pickers
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TimeDisplayField(
                        label = "Encendido",
                        time = startTime,
                        onClick = { showStartTimePicker = true },
                        modifier = Modifier.weight(1f)
                    )
                    TimeDisplayField(
                        label = "Apagado",
                        time = endTime,
                        onClick = { showEndTimePicker = true },
                        modifier = Modifier.weight(1f)
                    )
                }

                // Days Selector
                Column {
                    Text("Días", style = MaterialTheme.typography.labelMedium)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        daysOfWeek.forEach { (key, label) ->
                            val isSelected = selectedDays.contains(key)
                            Surface(
                                color = if (isSelected) Color(0xFF81C784) else Color(0xFFF4F6F8),
                                shape = CircleShape,
                                modifier = Modifier
                                    .size(36.dp)
                                    .clickable {
                                        if (isSelected) selectedDays.remove(key) else selectedDays.add(key)
                                    },
                                border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDFE3E8))
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = label.take(1),
                                        fontSize = 12.sp,
                                        color = if (isSelected) Color.White else Color.Gray,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(selectedDeviceId, startTime, endTime, selectedDays.toList()) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF81C784)),
                enabled = selectedDeviceId.isNotEmpty()
            ) {
                Text("Crear")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar", color = Color.Gray) }
        }
    )

    if (showStartTimePicker) {
        TimePickerDialog(
            onDismiss = { showStartTimePicker = false },
            onConfirm = { hour, minute ->
                startTime = String.format("%02d:%02d", hour, minute)
                showStartTimePicker = false
            }
        )
    }

    if (showEndTimePicker) {
        TimePickerDialog(
            onDismiss = { showEndTimePicker = false },
            onConfirm = { hour, minute ->
                endTime = String.format("%02d:%02d", hour, minute)
                showEndTimePicker = false
            }
        )
    }
}

@Composable
fun TimeDisplayField(label: String, time: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
                .clickable { onClick() },
            shape = RoundedCornerShape(8.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDFE3E8)),
            color = Color.White
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(time, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Icon(Icons.Outlined.AccessTime, null, modifier = Modifier.size(18.dp), tint = Color.Gray)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(onDismiss: () -> Unit, onConfirm: (Int, Int) -> Unit) {
    val state = rememberTimePickerState()
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { onConfirm(state.hour, state.minute) }) { Text("OK", color = Color(0xFF81C784)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar", color = Color.Gray) }
        },
        text = { TimePicker(state = state) }
    )
}

@Composable
fun ScheduleItemCard(schedule: Schedule, onToggle: () -> Unit) {
    val icon = when(schedule.deviceCategory.uppercase()) {
        "LIGHT" -> Icons.Default.Lightbulb
        "AC" -> Icons.Default.Air
        else -> Icons.Default.Bolt
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = Color(0xFF81C784).copy(alpha = 0.1f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF66BB6A), modifier = Modifier.padding(12.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = schedule.deviceName, 
                            fontSize = 18.sp, 
                            fontWeight = FontWeight.Bold, 
                            color = Color(0xFF212B36),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        if (schedule.enabled) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(color = Color(0xFFE8F5E9), shape = RoundedCornerShape(4.dp)) {
                                Text(
                                    text = "Activo", 
                                    color = Color(0xFF4CAF50), 
                                    fontSize = 10.sp, 
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), 
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    Text(text = schedule.roomName, fontSize = 14.sp, color = Color(0xFF919EAB))
                }
                Switch(
                    checked = schedule.enabled,
                    onCheckedChange = { onToggle() },
                    colors = getPowerSenseSwitchColors()
                )
            }
            
            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Color(0xFFF4F6F8))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                ScheduleTimeInfo("Encendido", schedule.startTime, "Lun a Vie", Color(0xFF4CAF50))
                ScheduleTimeInfo("Apagado", schedule.endTime, "Todos los dias", Color(0xFFF44336))
            }
        }
    }
}

@Composable
fun ScheduleTimeInfo(label: String, time: String, days: String, dotColor: Color) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(8.dp).background(dotColor, CircleShape))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = label, fontSize = 12.sp, color = Color(0xFF919EAB))
        }
        Text(text = "$time - $days", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF454F5B), modifier = Modifier.padding(start = 16.dp))
    }
}

@Composable
fun QuickScheduleItem(quick: QuickSchedule) {
    val icon = when(quick.icon) {
        "home" -> Icons.Default.Home
        "bed" -> Icons.Default.Bed
        else -> Icons.Default.Apartment
    }
    Surface(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        color = if(quick.id == "1") Color(0xFFE8F5E9) else Color(0xFFF4F6F8),
        shape = RoundedCornerShape(8.dp),
        onClick = { /* TODO: Trigger Quick Schedule */ }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = if(quick.id == "1") Color(0xFF4CAF50) else Color(0xFF637381))
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = quick.name, color = if(quick.id == "1") Color(0xFF4CAF50) else Color(0xFF637381))
        }
    }
}

@Composable
fun StatRow(label: String, value: String, valueColor: Color = Color(0xFF454F5B)) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = label, color = Color(0xFF919EAB), fontSize = 14.sp)
        Text(text = value, color = valueColor, fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}

@Composable
fun SmartRuleItem(title: String, subtitle: String) {
    var checked by remember { mutableStateOf(true) }
    Surface(
        color = Color.White.copy(alpha = 0.1f),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, color = Color.White, fontWeight = FontWeight.Bold)
                Text(text = subtitle, color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
            }
            Switch(
                checked = checked,
                onCheckedChange = { checked = it },
                colors = getPowerSenseSwitchColors()
            )
        }
    }
}

@Composable
fun ScheduleFilterChip(label: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        color = if (selected) Color.White else Color.Transparent,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.height(32.dp),
        shadowElevation = if (selected) 2.dp else 0.dp,
        onClick = onClick
    ) {
        Box(modifier = Modifier.padding(horizontal = 12.dp), contentAlignment = Alignment.Center) {
            Text(text = label, fontSize = 12.sp, color = if (selected) Color(0xFF212B36) else Color(0xFF919EAB))
        }
    }
}
