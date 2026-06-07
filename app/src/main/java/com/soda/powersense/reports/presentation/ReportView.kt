package com.soda.powersense.reports.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ReportView(
    viewModel: ReportViewModel
) {
    val state by viewModel.state.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        if (state.isLoading && state.kpis == null) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(text = "Reportes Energéticos", style = MaterialTheme.typography.headlineMedium)
                }

                state.kpis?.let { kpis ->
                    item {
                        ReportKPISection(kpis)
                    }
                }

                item {
                    Text(text = "Consumo Diario (Hoy)", style = MaterialTheme.typography.titleLarge)
                }

                items(state.consumptionHistory) { consumption ->
                    ConsumptionItem(consumption.label, consumption.consumption)
                }
            }
        }
    }
}

@Composable
fun ReportKPISection(kpis: com.soda.powersense.reports.domain.model.ReportKPIs) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Resumen de Consumo", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Hoy:")
                Text(text = "${String.format("%.2f", kpis.dailyConsumption)} kWh", style = MaterialTheme.typography.bodyLarge)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Esta Semana:")
                Text(text = "${String.format("%.2f", kpis.weeklyConsumption)} kWh", style = MaterialTheme.typography.bodyLarge)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Ahorro Estimado:")
                Text(text = "$ ${String.format("%.2f", kpis.costSaved)}", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
fun ConsumptionItem(time: String, value: Double) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = time, style = MaterialTheme.typography.bodyMedium)
        LinearProgressIndicator(
            progress = { (value / 5.0).toFloat().coerceIn(0f, 1f) },
            modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
        )
        Text(text = "${String.format("%.2f", value)} kWh", style = MaterialTheme.typography.bodySmall)
    }
}
