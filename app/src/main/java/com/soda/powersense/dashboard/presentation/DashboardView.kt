package com.soda.powersense.dashboard.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DashboardView(
    viewModel: DashboardViewModel
) {
    val state by viewModel.state.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        if (state.isLoading && state.kpis == null) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (state.error != null && state.kpis == null) {
            Text(
                text = state.error ?: "Error",
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.error
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "Dashboard", style = MaterialTheme.typography.headlineMedium)
                
                state.kpis?.let { kpis ->
                    KPICard(title = "Consumo Total", value = "${kpis.totalConsumption} kWh")
                    KPICard(title = "Costo Total", value = "$ ${kpis.totalCost}")
                    KPICard(title = "Dispositivos Activos", value = "${kpis.activeDevices} / ${kpis.totalDevices}")
                    KPICard(title = "Eficiencia", value = "${kpis.efficiency} %")
                }
            }
        }
    }
}

@Composable
fun KPICard(title: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleSmall)
            Text(text = value, style = MaterialTheme.typography.headlineSmall)
        }
    }
}
