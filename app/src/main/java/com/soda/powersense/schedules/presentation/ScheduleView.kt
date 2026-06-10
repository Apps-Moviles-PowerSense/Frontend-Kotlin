package com.soda.powersense.schedules.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soda.powersense.schedules.domain.model.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleView(
    viewModel: ScheduleViewModel
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
                    onClick = { /* TODO */ },
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
                    ScheduleFilterChip("Dispositivos", true)
                    ScheduleFilterChip("Habitaciones", false)
                    ScheduleFilterChip("Areas Comunes", false)
                    ScheduleFilterChip("Reglas Automaticas", false)
                }
            }

            // Search and Room filter
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        placeholder = { Text("Buscar dispositivo...", fontSize = 14.sp) },
                        modifier = Modifier.weight(1f).height(48.dp),
                        leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null, modifier = Modifier.size(18.dp)) },
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White,
                            unfocusedBorderColor = Color(0xFFDFE3E8)
                        )
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    OutlinedButton(
                        onClick = { /* TODO */ },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(48.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF637381))
                    ) {
                        Text("Todas las habitacione", fontSize = 12.sp)
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
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
            if (state.schedules.isEmpty()) {
                item {
                    Text("No hay programaciones configuradas", color = Color.Gray)
                }
            } else {
                items(state.schedules) { schedule ->
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
                    text = "Estadisticas",
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
                        StatRow("Dispositivos programados", "2/3")
                        StatRow("Horarios activos", "24")
                        StatRow("Ahorro estimado", "15%", Color(0xFF4CAF50))
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
                        SmartRuleItem("Modo Nocturno", "Apaga luces automaticamente")
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
    }
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
                        Text(text = schedule.deviceName, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212B36))
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(color = Color(0xFFE8F5E9), shape = RoundedCornerShape(4.dp)) {
                            Text("Activo", color = Color(0xFF4CAF50), fontSize = 10.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), fontWeight = FontWeight.Bold)
                        }
                    }
                    Text(text = schedule.roomName, fontSize = 14.sp, color = Color(0xFF919EAB))
                }
                Switch(
                    checked = schedule.enabled,
                    onCheckedChange = { onToggle() },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Color(0xFF81C784))
                )
            }
            
            Divider(modifier = Modifier.padding(vertical = 16.dp), color = Color(0xFFF4F6F8))
            
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
        Text(text = "$time- $days", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF454F5B), modifier = Modifier.padding(start = 16.dp))
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
        shape = RoundedCornerShape(8.dp)
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
                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Color(0xFF81C784))
            )
        }
    }
}

@Composable
fun ScheduleFilterChip(label: String, selected: Boolean) {
    Surface(
        color = if (selected) Color.White else Color.Transparent,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.height(32.dp),
        shadowElevation = if (selected) 2.dp else 0.dp
    ) {
        Box(modifier = Modifier.padding(horizontal = 12.dp), contentAlignment = Alignment.Center) {
            Text(text = label, fontSize = 12.sp, color = if (selected) Color(0xFF212B36) else Color(0xFF919EAB))
        }
    }
}
