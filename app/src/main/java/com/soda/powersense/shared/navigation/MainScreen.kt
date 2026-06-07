package com.soda.powersense.shared.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onLogout: () -> Unit
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "PowerSense",
                    modifier = Modifier.padding(horizontal = 28.dp, vertical = 16.dp),
                    style = MaterialTheme.typography.headlineSmall
                )
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                
                MainTab.entries.forEach { tab ->
                    val isSelected = currentDestination?.hasRoute(tab.route::class) == true
                    
                    NavigationDrawerItem(
                        label = { Text(tab.label) },
                        selected = isSelected,
                        onClick = {
                            navController.navigate(tab.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                            scope.launch { drawerState.close() }
                        },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) tab.iconFilled else tab.icon,
                                contentDescription = tab.label
                            )
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        val currentTab = MainTab.entries.find { currentDestination?.hasRoute(it.route::class) == true }
                        Text(text = currentTab?.label ?: "PowerSense")
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
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
