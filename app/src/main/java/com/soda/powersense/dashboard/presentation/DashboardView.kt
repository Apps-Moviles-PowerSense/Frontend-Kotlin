package com.soda.powersense.dashboard.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soda.powersense.alerts.domain.model.Alert
import com.soda.powersense.devices.domain.model.Device
import com.soda.powersense.devices.presentation.getPowerSenseSwitchColors
import com.soda.powersense.reports.presentation.MonthlyComparisonChart

@Composable
fun DashboardView(
    viewModel: DashboardViewModel,
    onNavigateToAlerts: () -> Unit
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
                        text = "Dashboard de Consumo",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF454F5B)
                    )
                    Text(
                        text = "Monitoreo y consumo energético en tiempo real",
                        fontSize = 14.sp,
                        color = Color(0xFF919EAB)
                    )
                }
            }

            // Top KPIs
            state.kpis?.let { kpis ->
                item {
                    KPICard(
                        title = "Consumo Actual",
                        value = "${String.format("%.2f", kpis.totalConsumption)}kWh",
                        variation = kpis.consumptionVariation,
                        icon = Icons.Default.Bolt,
                        color = Color(0xFF81C784)
                    )
                }
                item {
                    KPICard(
                        title = "Costo Estimado",
                        value = "S/${String.format("%.2f", kpis.estimatedCost)}",
                        variation = kpis.costVariation,
                        icon = Icons.Default.Payments,
                        color = Color(0xFF64B5F6)
                    )
                }
                item {
                    KPICard(
                        title = "Ahorro Potencial",
                        value = "${kpis.efficiency}%",
                        variation = kpis.efficiencyVariation,
                        icon = Icons.Default.TrendingDown,
                        color = Color(0xFFFFB300)
                    )
                }
                item {
                    KPICard(
                        title = "Dispositivos Activos",
                        value = "${kpis.activeDevices}",
                        variation = null,
                        icon = Icons.Default.ShowChart,
                        color = Color(0xFFB39DDB)
                    )
                }
            }

            // Energy Chart
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(text = "Consumo Energético", fontWeight = FontWeight.Bold, color = Color(0xFF454F5B))
                        Row(modifier = Modifier.padding(vertical = 12.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            listOf("Diario", "Semanal", "Mensual").forEach { period ->
                                val isSelected = state.selectedPeriod == period
                                Text(
                                    text = period,
                                    color = if (isSelected) Color(0xFF81C784) else Color(0xFF919EAB),
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 13.sp,
                                    modifier = Modifier.clickable { viewModel.onPeriodChange(period) }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        MonthlyComparisonChart(state.monthlyComparison)
                    }
                }
            }

            // Quick Device Control
            item {
                Text(text = "Control de Dispositivos", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF454F5B))
                Text(text = "Accesos rápidos (Top consumo)", fontSize = 14.sp, color = Color(0xFF919EAB))
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        state.quickDevices.forEach { device ->
                            QuickDeviceItem(
                                device = device,
                                onToggle = { viewModel.toggleDevice(device) }
                            )
                        }
                    }
                }
            }

            // Saving Tips
            item {
                Text(text = "Recomendaciones de Ahorro", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF454F5B))
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    SavingTipItem("Optimizar horarios", "Programa tus dispositivos para ahorrar automáticamente", Color(0xFFE8F5E9), Color(0xFF4CAF50), Icons.Default.Lightbulb)
                    SavingTipItem("Horario valle", "Usa el consumo pesado 22:00-06:00. Ahorra S/5/mes", Color(0xFFE3F2FD), Color(0xFF2196F3), Icons.Default.Bolt)
                }
            }

            // Recent Alerts
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Alertas Recientes", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF454F5B))
                    TextButton(onClick = onNavigateToAlerts) {
                        Text("Ver todas", color = Color(0xFF81C784))
                    }
                }
            }

            items(state.recentAlerts) { alert ->
                DashboardAlertItem(
                    alert = alert,
                    onDelete = { viewModel.acknowledgeAlert(it) }
                )
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }
}

@Composable
fun KPICard(title: String, value: String, variation: Int?, icon: ImageVector, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(color = color.copy(alpha = 0.1f), shape = RoundedCornerShape(12.dp)) {
                    Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.padding(10.dp).size(24.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = title, fontSize = 12.sp, color = Color(0xFF919EAB))
                    Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212B36))
                }
            }
            
            variation?.let {
                val varColor = if (it <= 0) Color(0xFF4CAF50) else Color(0xFFF44336)
                Surface(color = varColor.copy(alpha = 0.1f), shape = RoundedCornerShape(10.dp)) {
                    Text(
                        text = "${if (it > 0) "+" else ""}$it%",
                        color = varColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun QuickDeviceItem(device: Device, onToggle: () -> Unit) {
    val isActive = device.status.lowercase() == "active"
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(8.dp).background(if(isActive) Color(0xFF4CAF50) else Color.Gray, CircleShape))
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = device.name, fontWeight = FontWeight.Bold, color = Color(0xFF212B36))
            Text(text = "${device.roomName} • ${device.watts}W", fontSize = 12.sp, color = Color(0xFF919EAB))
        }
        Switch(
            checked = isActive,
            onCheckedChange = { onToggle() },
            colors = getPowerSenseSwitchColors()
        )
    }
}

@Composable
fun SavingTipItem(title: String, message: String, bgColor: Color, iconColor: Color, icon: ImageVector) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = bgColor,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, fontWeight = FontWeight.Bold, color = iconColor, fontSize = 14.sp)
                Text(text = message, color = iconColor.copy(alpha = 0.7f), fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun DashboardAlertItem(alert: Alert, onDelete: (String) -> Unit) {
    val color = when(alert.severity.uppercase()) {
        "CRITICAL", "ERROR" -> Color(0xFFF44336)
        "WARNING" -> Color(0xFFFF9800)
        else -> Color(0xFF2196F3)
    }
    Surface(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        color = color.copy(alpha = 0.05f),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.1f))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = alert.message, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF454F5B))
                Text(text = "Hace poco", fontSize = 12.sp, color = Color(0xFF919EAB))
            }
            IconButton(onClick = { onDelete(alert.id) }) {
                Icon(imageVector = Icons.Outlined.Close, contentDescription = "Cerrar", tint = Color.Gray, modifier = Modifier.size(16.dp))
            }
        }
    }
}
