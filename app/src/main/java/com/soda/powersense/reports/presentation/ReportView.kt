package com.soda.powersense.reports.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.Visibility
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
import com.soda.powersense.reports.domain.model.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportView(
    viewModel: ReportViewModel
) {
    val state by viewModel.state.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
    var previewReport by remember { mutableStateOf<ReportHistory?>(null) }

    val startDatePickerState = rememberDatePickerState()
    val endDatePickerState = rememberDatePickerState()

    val dateFormatter = remember { 
        SimpleDateFormat("dd/MM/yy", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
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
                        text = "Reportes de Consumo",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF454F5B)
                    )
                    Text(
                        text = "Analiza y compara el consumo energético con reportes detallados",
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
                        var expanded by remember { mutableStateOf(false) }
                        val reportTypes = listOf("Diario", "Semanal", "Mensual")
                        
                        Box {
                            ReportFilterField(
                                label = "Tipo de Reporte", 
                                value = state.reportType,
                                onClick = { expanded = true }
                            )
                            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                                reportTypes.forEach { type ->
                                    DropdownMenuItem(
                                        text = { Text(type) },
                                        onClick = {
                                            viewModel.onReportTypeChange(type)
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        ReportFilterField(
                            label = "Fecha Inicio", 
                            value = state.startDate,
                            onValueChange = viewModel::onStartDateChange,
                            readOnly = true,
                            onClick = { showStartDatePicker = true }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        ReportFilterField(
                            label = "Fecha Fin", 
                            value = state.endDate,
                            onValueChange = viewModel::onEndDateChange,
                            readOnly = true,
                            onClick = { showEndDatePicker = true }
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick = { viewModel.loadData() },
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7CB342)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            if (state.isLoading) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                            } else {
                                Text("Aplicar Filtros", fontWeight = FontWeight.Bold)
                            }
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
                ChartCard(
                    title = "Consumo Mensual", 
                    subtitle = "Comparativa anual",
                    legend = listOf("2024" to Color(0xFF81C784), "2023" to Color(0xFF64B5F6))
                ) {
                    MonthlyComparisonChart(state.monthlyComparison)
                }
            }

            // Department Comparison Chart
            item {
                ChartCard(
                    title = "Comparativa por Departamentos", 
                    subtitle = "Consumo mensual",
                    legend = listOf("Enero 2025" to Color(0xFF81C784), "Diciembre 2024" to Color(0xFFB39DDB))
                ) {
                    DepartmentComparisonChart(state.departmentMetrics)
                }
            }

            // History Table
            item {
                HistorySection(
                    history = state.reportHistory,
                    onPreview = { previewReport = it },
                    onDownload = { report, format ->
                        scope.launch {
                            snackbarHostState.showSnackbar("Descargando reporte de ${report.department} en formato $format...")
                        }
                    }
                )
            }
            
            item { Spacer(modifier = Modifier.height(20.dp)) }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }

    if (showStartDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showStartDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    startDatePickerState.selectedDateMillis?.let {
                        viewModel.onStartDateChange(dateFormatter.format(Date(it)))
                    }
                    showStartDatePicker = false
                }) { Text("Confirmar") }
            },
            dismissButton = {
                TextButton(onClick = { showStartDatePicker = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = startDatePickerState)
        }
    }

    if (showEndDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showEndDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    endDatePickerState.selectedDateMillis?.let {
                        viewModel.onEndDateChange(dateFormatter.format(Date(it)))
                    }
                    showEndDatePicker = false
                }) { Text("Confirmar") }
            },
            dismissButton = {
                TextButton(onClick = { showEndDatePicker = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = endDatePickerState)
        }
    }

    previewReport?.let { report ->
        ReportPreviewDialog(
            report = report,
            onDismiss = { previewReport = null }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportFilterField(
    label: String, 
    value: String, 
    onValueChange: (String) -> Unit = {}, 
    readOnly: Boolean = true,
    onClick: () -> Unit = {}
) {
    Column {
        Text(text = label, fontSize = 12.sp, color = Color(0xFF919EAB), modifier = Modifier.padding(bottom = 4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            readOnly = true, // Force read only to capture clicks
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = { 
                IconButton(onClick = onClick) {
                    Icon(if (label.contains("Fecha")) Icons.Default.CalendarToday else Icons.Default.ArrowDropDown, contentDescription = null) 
                }
            },
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFDFE3E8),
                focusedBorderColor = Color(0xFFDFE3E8)
            ),
            interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
                .also { interactionSource ->
                    LaunchedEffect(interactionSource) {
                        interactionSource.interactions.collect {
                            if (it is androidx.compose.foundation.interaction.PressInteraction.Release) {
                                onClick()
                            }
                        }
                    }
                }
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
                
                val variationColor = if (variation <= 0) Color(0xFF4CAF50) else Color(0xFFF44336)
                Surface(
                    color = variationColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text(
                        text = "${if (variation > 0) "+" else ""}$variation% vs. mes anterior",
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
fun ChartCard(title: String, subtitle: String, legend: List<Pair<String, Color>>, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212B36))
                    Text(text = subtitle, fontSize = 14.sp, color = Color(0xFF919EAB))
                }
                
                // Dynamic Legend
                Column(horizontalAlignment = Alignment.End) {
                    legend.forEach { (label, color) ->
                        LegendItem(label, color)
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            content()
        }
    }
}

@Composable
fun LegendItem(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)) {
        Box(modifier = Modifier.size(8.dp).background(color, CircleShape))
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = label, fontSize = 11.sp, color = Color(0xFF919EAB))
    }
}

@Composable
fun MonthlyComparisonChart(data: List<MonthlyComparison>) {
    if (data.isEmpty()) {
        Box(Modifier.fillMaxWidth().height(150.dp), contentAlignment = Alignment.Center) {
            Text("No hay datos comparativos disponibles", color = Color.Gray, fontSize = 12.sp)
        }
        return
    }
    val scrollState = rememberScrollState()
    val maxVal = (data.maxOfOrNull { maxOf(it.value1, it.value2) } ?: 100.0).coerceAtLeast(1.0)
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        data.forEach { item ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.height(160.dp)) {
                    val h1 = (160 * (item.value1 / maxVal)).dp
                    val h2 = (160 * (item.value2 / maxVal)).dp
                    
                    Box(modifier = Modifier
                        .width(14.dp)
                        .height(h1)
                        .background(Color(0xFF81C784), RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)))
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(modifier = Modifier
                        .width(14.dp)
                        .height(h2)
                        .background(Color(0xFF64B5F6), RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = item.month, fontSize = 12.sp, color = Color(0xFF919EAB))
            }
        }
    }
}

@Composable
fun DepartmentComparisonChart(data: List<DepartmentMetric>) {
    if (data.isEmpty()) {
        Box(Modifier.fillMaxWidth().height(150.dp), contentAlignment = Alignment.Center) {
            Text("No hay métricas por departamento", color = Color.Gray, fontSize = 12.sp)
        }
        return
    }
    val scrollState = rememberScrollState()
    val maxVal = (data.maxOfOrNull { maxOf(it.current, it.previous) } ?: 100.0).coerceAtLeast(1.0)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        data.forEach { item ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.height(160.dp)) {
                    val h1 = (160 * (item.current / maxVal)).dp
                    val h2 = (160 * (item.previous / maxVal)).dp

                    Box(modifier = Modifier
                        .width(14.dp)
                        .height(h1)
                        .background(Color(0xFF81C784), RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)))
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(modifier = Modifier
                        .width(14.dp)
                        .height(h2)
                        .background(Color(0xFFB39DDB), RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = item.department, fontSize = 11.sp, color = Color(0xFF919EAB))
            }
        }
    }
}

@Composable
fun HistorySection(
    history: List<ReportHistory>,
    onPreview: (ReportHistory) -> Unit,
    onDownload: (ReportHistory, String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Title and Description
            Text(text = "Historial de Reportes", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212B36))
            Text(text = "Reportes generados previamente", fontSize = 14.sp, color = Color(0xFF919EAB))
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Global Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { /* All PDF */ },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f).height(40.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF637381))
                ) {
                    Icon(Icons.Outlined.FileDownload, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Exportar PDF", fontSize = 11.sp)
                }
                Button(
                    onClick = { /* All CSV */ },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f).height(40.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF81C784))
                ) {
                    Icon(Icons.Outlined.FileDownload, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Exportar CSV", fontSize = 11.sp)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Table with horizontal scroll to prevent text wrapping
            val scrollState = rememberScrollState()
            Column(modifier = Modifier.horizontalScroll(scrollState)) {
                // Header Row
                Row(
                    modifier = Modifier.width(500.dp), // Fixed width for scrollable table
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Periodo", modifier = Modifier.width(100.dp), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF919EAB))
                    Text("Departamento", modifier = Modifier.width(120.dp), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF919EAB))
                    Text("Consumo", modifier = Modifier.width(80.dp), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF919EAB))
                    Text("Costo", modifier = Modifier.width(70.dp), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF919EAB))
                    Text("Var.", modifier = Modifier.width(60.dp), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF919EAB))
                    Text("Acc.", modifier = Modifier.width(70.dp), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF919EAB), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                }
                
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp).width(500.dp), color = Color(0xFFF4F6F8))
                
                history.forEach { item ->
                    Row(
                        modifier = Modifier.width(500.dp).padding(vertical = 8.dp), 
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(item.period, modifier = Modifier.width(100.dp), fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color(0xFF212B36))
                        Text(item.department, modifier = Modifier.width(120.dp), fontSize = 13.sp, color = Color(0xFF637381))
                        Text("${item.consumption.toInt()} kWh", modifier = Modifier.width(80.dp), fontSize = 13.sp, color = Color(0xFF637381))
                        Text("S/${item.cost.toInt()}", modifier = Modifier.width(70.dp), fontSize = 13.sp, color = Color(0xFF637381))
                        
                        val varColor = if (item.variation <= 0) Color(0xFF4CAF50) else Color(0xFFF44336)
                        Box(modifier = Modifier.width(60.dp)) {
                            Surface(
                                color = varColor.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "${if (item.variation > 0) "+" else ""}${item.variation}%",
                                    fontSize = 10.sp,
                                    color = varColor,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                        
                        Row(modifier = Modifier.width(70.dp), horizontalArrangement = Arrangement.Center) {
                            IconButton(onClick = { onPreview(item) }, modifier = Modifier.size(28.dp)) {
                                Icon(Icons.Outlined.Visibility, contentDescription = null, modifier = Modifier.size(18.dp), tint = Color(0xFF637381))
                            }
                            IconButton(onClick = { onDownload(item, "PDF") }, modifier = Modifier.size(28.dp)) {
                                Icon(Icons.Outlined.FileDownload, contentDescription = null, modifier = Modifier.size(18.dp), tint = Color(0xFF637381))
                            }
                        }
                    }
                    HorizontalDivider(modifier = Modifier.width(500.dp), color = Color(0xFFF4F6F8))
                }
            }
        }
    }
}

@Composable
fun ReportPreviewDialog(
    report: ReportHistory,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Previsualización de Reporte", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Se generará un documento con la siguiente información:", fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(16.dp))
                PreviewRow("Periodo:", report.period)
                PreviewRow("Departamento:", report.department)
                PreviewRow("Consumo Total:", "${report.consumption.toInt()} kWh")
                PreviewRow("Costo Total:", "S/${report.cost.toInt()}")
                PreviewRow("Variación:", "${report.variation}%")
                
                Spacer(modifier = Modifier.height(24.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(Color(0xFFF4F6F8), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(48.dp))
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF81C784))
            ) {
                Text("Cerrar")
            }
        }
    )
}

@Composable
fun PreviewRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = label, fontWeight = FontWeight.Medium, fontSize = 13.sp)
        Text(text = value, color = Color.DarkGray, fontSize = 13.sp)
    }
}
