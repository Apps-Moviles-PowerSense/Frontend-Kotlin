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
import com.soda.powersense.alerts.domain.model.Alert

@Composable
fun AlertsView(
    viewModel: AlertViewModel,
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val errorCount = state.alerts.count { it.severity.uppercase() in listOf("CRITICAL", "ERROR") }
    val warningCount = state.alerts.count { it.severity.uppercase() == "WARNING" }
    val infoCount = state.alerts.count { it.severity.uppercase() == "INFO" }
    val completedCount = state.alerts.count { it.acknowledged }

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

            // Quick Action Buttons
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(
                        onClick = { /* TODO */ },
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF637381))
                    ) {
                        Icon(Icons.Default.FilterList, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Filtrar", fontSize = 14.sp)
                    }
                    
                    OutlinedButton(
                        onClick = { /* TODO */ },
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF637381))
                    ) {
                        Text("Marcar todas como leídas", fontSize = 14.sp)
                    }
                }
            }

            // Summary Card (Consolidated)
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
                        SummaryItem(label = "Completadas", count = completedCount, icon = Icons.Default.CheckCircle, color = Color(0xFF4CAF50))
                    }
                }
            }

            // Filter Chips
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AlertFilterChip("Todas (${state.alerts.size})", true)
                    AlertFilterChip("No leídas", false)
                    AlertFilterChip("Errores", false)
                    AlertFilterChip("Advertencias", false)
                }
            }

            // Alert List
            if (state.isLoading && state.alerts.isEmpty()) {
                item {
                    Box(Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            } else if (state.alerts.isEmpty()) {
                item {
                    Text("No se encontraron alertas", color = Color.Gray, modifier = Modifier.padding(vertical = 32.dp))
                }
            } else {
                items(state.alerts) { alert ->
                    AlertItem(alert)
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
fun AlertFilterChip(label: String, selected: Boolean) {
    Surface(
        color = if (selected) Color(0xFFDFE3E8) else Color.White,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.height(32.dp)
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
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF81C784),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFFDFE3E8),
                uncheckedBorderColor = Color.Transparent
            )
        )
    }
}
