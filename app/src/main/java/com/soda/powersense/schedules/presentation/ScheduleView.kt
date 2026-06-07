package com.soda.powersense.schedules.presentation

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
import com.soda.powersense.schedules.domain.model.Schedule

@Composable
fun ScheduleView(
    viewModel: ScheduleViewModel
) {
    val state by viewModel.state.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        if (state.isLoading && state.schedules.isEmpty()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (state.error != null && state.schedules.isEmpty()) {
            Text(
                text = state.error ?: "Error",
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.error
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Text(
                        text = "Horarios",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
                items(state.schedules) { schedule ->
                    ScheduleItem(
                        schedule = schedule,
                        onToggle = { viewModel.toggleSchedule(schedule) }
                    )
                }
            }
        }
    }
}

@Composable
fun ScheduleItem(
    schedule: Schedule,
    onToggle: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = schedule.deviceName, style = MaterialTheme.typography.titleLarge)
                Text(
                    text = "${schedule.startTime} - ${schedule.endTime}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = schedule.days.joinToString(", "),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Switch(
                checked = schedule.enabled,
                onCheckedChange = onToggle
            )
        }
    }
}
