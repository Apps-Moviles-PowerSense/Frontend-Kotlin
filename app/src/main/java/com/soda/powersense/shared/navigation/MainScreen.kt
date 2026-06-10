package com.soda.powersense.shared.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.soda.powersense.alerts.presentation.AlertViewModel
import com.soda.powersense.alerts.presentation.AlertsView
import com.soda.powersense.auth.presentation.profile.ProfileView
import com.soda.powersense.auth.presentation.profile.ProfileViewModel
import com.soda.powersense.dashboard.presentation.DashboardView
import com.soda.powersense.dashboard.presentation.DashboardViewModel
import com.soda.powersense.devices.presentation.DeviceView
import com.soda.powersense.devices.presentation.DeviceViewModel
import com.soda.powersense.reports.presentation.ReportView
import com.soda.powersense.reports.presentation.ReportViewModel
import com.soda.powersense.schedules.presentation.ScheduleView
import com.soda.powersense.schedules.presentation.ScheduleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onLogout: () -> Unit
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            Surface(
                tonalElevation = 3.dp,
                shadowElevation = 8.dp,
                color = Color.White
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    MainTab.entries.forEach { tab ->
                        val isSelected = currentDestination?.hasRoute(tab.route::class) == true
                        
                        Box(
                            modifier = Modifier
                                .width(100.dp) // Fixed width for each item to allow scrolling
                                .fillMaxHeight()
                        ) {
                            NavigationItem(
                                label = tab.label,
                                icon = if (isSelected) tab.iconFilled else tab.icon,
                                selected = isSelected,
                                onClick = {
                                    navController.navigate(tab.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = DashboardRoute,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable<DashboardRoute> {
                val viewModel: DashboardViewModel = hiltViewModel()
                DashboardView(viewModel = viewModel)
            }
            composable<DevicesRoute> {
                val viewModel: DeviceViewModel = hiltViewModel()
                DeviceView(viewModel = viewModel)
            }
            composable<AlertsRoute> {
                val viewModel: AlertViewModel = hiltViewModel()
                AlertsView(viewModel = viewModel)
            }
            composable<ReportsRoute> {
                val viewModel: ReportViewModel = hiltViewModel()
                ReportView(viewModel = viewModel)
            }
            composable<SchedulesRoute> {
                val viewModel: ScheduleViewModel = hiltViewModel()
                ScheduleView(viewModel = viewModel)
            }
            composable<ProfileRoute> {
                val viewModel: ProfileViewModel = hiltViewModel()
                ProfileView(
                    viewModel = viewModel,
                    onLogout = onLogout
                )
            }
        }
    }
}

@Composable
fun NavigationItem(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    val contentColor = if (selected) Color(0xFF81C784) else Color(0xFF919EAB)
    
    IconButton(
        onClick = onClick,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = contentColor,
                modifier = Modifier.size(26.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = contentColor,
                maxLines = 1
            )
        }
    }
}

@Composable
fun PlaceholderScreen(name: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "$name Screen (Coming Soon)")
    }
}
