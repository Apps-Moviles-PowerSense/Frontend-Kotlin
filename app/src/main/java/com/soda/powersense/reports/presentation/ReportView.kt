package com.soda.powersense.reports.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soda.powersense.reports.domain.model.*

@Composable
fun ReportView(
    viewModel: ReportViewModel
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
                        text = "Reportes de Consumo",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF454F5B)
                    )
                    Text(
                        text = "Analiza y compara el consumo energetico con reportes detallados",
                        fontSize = 14.sp,
                        color = Color(0xFF919EAB)
                    )
                }
            }

            // Filters Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        ReportFilterField(label = "Tipo de Reporte", value = "Diario")
                        Spacer(modifier = Modifier.height(12.dp))
                        ReportFilterField(label = "Fecha Inicio", value = "01/01/2025")
                        Spacer(modifier = Modifier.height(12.dp))
                        ReportFilterField(label = "Fecha Fin", value = "09/18/2025")
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick = { viewModel.loadData() },
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7CB342)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Aplicar Filtros", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // KPI Cards
            state.kpis?.let { kpis ->
                item {
                    ReportKPICard(
                        title = "Consumo Total",
                        value = "${String.format("%,.0f", kpis.totalConsumption)} kWh",
                        variation = kpis.consumptionVariation,
                        icon = Icons.Default.Bolt,
                        color = Color(0xFF2196F3)
                    )
                }
                item {
                    ReportKPICard(
                        title = "Costo Total",
                        value = "S/${String.format("%,.0f", kpis.totalCost)}",
                        variation = kpis.costVariation,
                        icon = Icons.Default.Payments,
                        color = Color(0xFFFFB300)
                    )
                }
                item {
                    ReportKPICard(
                        title = "Eficiencia",
                        value = "${kpis.efficiency}%",
                        variation = kpis.efficiencyVariation,
                        icon = Icons.Default.TrendingUp,
                        color = Color(0xFF4CAF50)
                    )
                }
            }

            // Monthly Comparison Chart
            item {
                ChartCard(title = "Consumo Mensual", subtitle = "Comparativa anual") {
                    MonthlyComparisonChart(state.monthlyComparison)
                }
            }

            // Department Comparison Chart
            item {
                ChartCard(title = "Comparativa por Departamentos", subtitle = "Consumo mensual") {
                    DepartmentComparisonChart(state.departmentMetrics)
                }
            }

            // History Table
            item {
                HistorySection(state.reportHistory)
            }
            
            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }
}

@Composable
fun ReportFilterField(label: String, value: String) {
    Column {
        Text(text = label, fontSize = 12.sp, color = Color(0xFF919EAB), modifier = Modifier.padding(bottom = 4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) },
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFDFE3E8),
                focusedBorderColor = Color(0xFFDFE3E8)
            )
        )
    }
}

@Composable
fun ReportKPICard(title: String, value: String, variation: Int, icon: ImageVector, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            Box(modifier = Modifier.width(4.dp).fillMaxHeight().background(color))
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(color = color.copy(alpha = 0.1f), shape = RoundedCornerShape(8.dp)) {
                        Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.padding(8.dp).size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = title, fontSize = 14.sp, color = Color(0xFF919EAB))
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = value, fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212B36))
                
                val variationColor = if (variation >= 0) Color(0xFF4CAF50) else Color(0xFFF44336)
                Surface(
                    color = variationColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text(
                        text = "${if (variation >= 0) "+" else ""}$variation% vs. mes anterior",
                        color = variationColor,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun ChartCard(title: String, subtitle: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212B36))
            Text(text = subtitle, fontSize = 14.sp, color = Color(0xFF919EAB))
            Spacer(modifier = Modifier.height(24.dp))
            content()
        }
    }
}

@Composable
fun MonthlyComparisonChart(data: List<MonthlyComparison>) {
    // Simple bar chart implementation
    Row(
        modifier = Modifier.fillMaxWidth().height(150.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        data.forEach { item ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Box(modifier = Modifier.width(12.dp).height((item.value1 / 5).dp).background(Color(0xFF81C784), RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp)))
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(modifier = Modifier.width(12.dp).height((item.value2 / 5).dp).background(Color(0xFF64B5F6), RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp)))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = item.month, fontSize = 10.sp, color = Color(0xFF919EAB))
            }
        }
    }
}

@Composable
fun DepartmentComparisonChart(data: List<DepartmentMetric>) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        data.forEach { item ->
            Column {
                Text(text = item.department, fontSize = 12.sp, color = Color(0xFF212B36), modifier = Modifier.padding(bottom = 4.dp))
                Row(modifier = Modifier.fillMaxWidth().height(24.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.weight(item.current.toFloat()).fillMaxHeight().background(Color(0xFF81C784), RoundedCornerShape(4.dp)))
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(modifier = Modifier.weight(item.previous.toFloat()).fillMaxHeight().background(Color(0xFFB39DDB), RoundedCornerShape(4.dp)))
                }
            }
        }
    }
}

@Composable
fun HistorySection(history: List<ReportHistory>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text(text = "Historial de Reportes", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212B36))
                    Text(text = "Reportes generados previamente", fontSize = 14.sp, color = Color(0xFF919EAB))
                }
                Row {
                    IconButton(onClick = {}) { Icon(Icons.Outlined.FileDownload, contentDescription = "Export PDF") }
                    IconButton(onClick = {}) { Icon(Icons.Outlined.FileDownload, contentDescription = "Export CSV", tint = Color(0xFF4CAF50)) }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Header Row
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Periodo", modifier = Modifier.weight(1.5f), fontSize = 12.sp, color = Color(0xFF919EAB))
                Text("Dept.", modifier = Modifier.weight(1.5f), fontSize = 12.sp, color = Color(0xFF919EAB))
                Text("Cons.", modifier = Modifier.weight(1f), fontSize = 12.sp, color = Color(0xFF919EAB))
                Text("Var.", modifier = Modifier.weight(1f), fontSize = 12.sp, color = Color(0xFF919EAB))
                Text("Acc.", modifier = Modifier.weight(0.8f), fontSize = 12.sp, color = Color(0xFF919EAB))
            }
            
            Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFF4F6F8))
            
            history.forEach { item ->
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(item.period, modifier = Modifier.weight(1.5f), fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color(0xFF212B36))
                    Text(item.department, modifier = Modifier.weight(1.5f), fontSize = 13.sp, color = Color(0xFF637381))
                    Text("${item.consumption}kWh", modifier = Modifier.weight(1f), fontSize = 13.sp, color = Color(0xFF637381))
                    
                    val varColor = if (item.variation >= 0) Color(0xFFF44336) else Color(0xFF4CAF50)
                    Text(
                        "${if (item.variation >= 0) "+" else ""}${item.variation}%",
                        modifier = Modifier.weight(1f),
                        fontSize = 11.sp,
                        color = varColor,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Row(modifier = Modifier.weight(0.8f), horizontalArrangement = Arrangement.SpaceBetween) {
                        Icon(Icons.Outlined.Visibility, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFF637381))
                        Icon(Icons.Default.FileDownload, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFF637381))
                    }
                }
                Divider(color = Color(0xFFF4F6F8))
            }
        }
    }
}
