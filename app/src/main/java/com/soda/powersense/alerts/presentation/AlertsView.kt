package com.soda.powersense.alerts.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextOverflow
import com.soda.powersense.alerts.domain.model.Alert
import com.soda.powersense.devices.presentation.getPowerSenseSwitchColors

@Composable
fun AlertsView(
    viewModel: AlertViewModel,
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showFilterDialog by remember { mutableStateOf(false) }

    val errorCount = state.alerts.count { it.severity.uppercase() in listOf("CRITICAL", "ERROR") && !it.acknowledged }
    val warningCount = state.alerts.count { it.severity.uppercase() == "WARNING" && !it.acknowledged }
    val infoCount = state.alerts.count { it.severity.uppercase() == "INFO" && !it.acknowledged }
    val completedCount = state.alerts.count { it.acknowledged }

    val alertTypes = listOf(
        "HIGH_CONSUMPTION",
        "LOW_EFFICIENCY",
        "DEVICE_OFFLINE",
        "SCHEDULE_CONFLICT",
        "THRESHOLD_EXCEEDED",
        "CUSTOM"
    )

    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

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
                        text = "Alertas",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF454F5B)
                    )
                    Text(
                        text = "Centro de notificaciones y alertas del sistema",
                        fontSize = 14.sp,
                        color = Color(0xFF919EAB)
                    )
                }
            }

            // Filter Bar
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = { showFilterDialog = true },
                        modifier = Modifier.height(40.dp),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF637381))
                    ) {
                        Icon(Icons.Default.FilterList, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (state.selectedType != null) state.selectedType!!.replace("_", " ").lowercase().capitalize() else "Filtrar",
                            fontSize = 12.sp
                        )
                    }
                    
                    if (state.selectedType != null) {
                        IconButton(
                            onClick = { viewModel.onTypeFilterSelected(null) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Limpiar filtro", tint = Color.Red, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }

            // Summary Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        SummaryItem(label = "Errores", count = errorCount, icon = Icons.Default.Error, color = Color(0xFFF44336))
                        SummaryItem(label = "Advertencias", count = warningCount, icon = Icons.Default.Warning, color = Color(0xFFFF9800))
                        SummaryItem(label = "Información", count = infoCount, icon = Icons.Default.Notifications, color = Color(0xFF2196F3))
                        SummaryItem(label = "Leídas", count = completedCount, icon = Icons.Default.CheckCircle, color = Color(0xFF4CAF50))
                    }
                }
            }

            // Action Row: "Todas" chip + "Marcar todas" button
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val unacknowledgedCount = state.alerts.count { !it.acknowledged }
                    AlertFilterChip("Todas ($unacknowledgedCount)", state.selectedType == null) {
                        viewModel.onTypeFilterSelected(null)
                    }
                    
                    TextButton(
                        onClick = { viewModel.acknowledgeAll() },
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        Text(
                            text = "Marcar todas como leídas",
                            color = Color(0xFF81C784),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Alert List
            if (state.isLoading && state.alerts.isEmpty()) {
                item {
                    Box(Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            } else if (state.filteredAlerts.isEmpty()) {
                item {
                    Text("No se encontraron alertas nuevas", color = Color.Gray, modifier = Modifier.padding(vertical = 32.dp))
                }
            } else {
                items(state.filteredAlerts) { alert ->
                    AlertItem(
                        alert = alert,
                        onDelete = { id -> viewModel.acknowledgeAlert(id) }
                    )
                }
            }

            // Settings Section
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(
                            text = "Configuración de Alertas",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF454F5B)
                        )
                        Text(
                            text = "Personaliza que notificaciones deseas recibir",
                            fontSize = 14.sp,
                            color = Color(0xFF919EAB)
                        )
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        SettingsToggleItem("Consumo excesivo", "Notificar cuando el consumo supere los límites", true)
                        SettingsToggleItem("Dispositivos desconectados", "Alerta cuando un dispositivo pierda conexión", true)
                        SettingsToggleItem("Programaciones completadas", "Confirmar cuando se ejecuten programaciones", false)
                        SettingsToggleItem("Actualizaciones del sistema", "Notificar sobre nuevas versiones disponibles", true)
                    }
                }
            }
            
            item { Spacer(modifier = Modifier.height(20.dp)) }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        if (showFilterDialog) {
            AlertDialog(
                onDismissRequest = { showFilterDialog = false },
                title = { Text("Filtrar por tipo de alerta") },
                text = {
                    Column {
                        alertTypes.forEach { type ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(if(state.selectedType == type) Color(0xFFE8F5E9) else Color.Transparent)
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = state.selectedType == type,
                                    onClick = { 
                                        viewModel.onTypeFilterSelected(type)
                                        showFilterDialog = false 
                                    },
                                    colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF81C784))
                                )
                                Text(
                                    text = type.replace("_", " ").lowercase().capitalize(),
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { 
                        viewModel.onTypeFilterSelected(null)
                        showFilterDialog = false 
                    }) {
                        Text("Limpiar filtros", color = Color.Gray)
                    }
                }
            )
        }
    }
}

@Composable
fun SummaryItem(label: String, count: Int, icon: ImageVector, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            color = color.copy(alpha = 0.1f),
            shape = CircleShape,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.padding(12.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = count.toString(), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF454F5B))
        Text(text = label, fontSize = 11.sp, color = Color(0xFF919EAB))
    }
}

@Composable
fun AlertFilterChip(label: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        color = if (selected) Color(0xFFDFE3E8) else Color.White,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.height(32.dp),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                color = if (selected) Color(0xFF212B36) else Color(0xFF637381)
            )
        }
    }
}

@Composable
fun SettingsToggleItem(title: String, subtitle: String, initialValue: Boolean) {
    var checked by remember { mutableStateOf(initialValue) }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 15.sp, fontWeight = FontWeight.Medium, color = Color(0xFF454F5B))
            Text(text = subtitle, fontSize = 12.sp, color = Color(0xFF919EAB))
        }
        Switch(
            checked = checked,
            onCheckedChange = { checked = it },
            colors = getPowerSenseSwitchColors()
        )
    }
}

private fun String.capitalize() = this.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
