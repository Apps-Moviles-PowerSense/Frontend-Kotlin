package com.soda.powersense.shared.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@Composable
fun MainScreen(
    onLogout: () -> Unit
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar {
                MainTab.entries.forEach { tab ->
                    val isSelected = currentDestination?.hasRoute(tab.route::class) == true

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            navController.navigate(tab.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) tab.iconFilled else tab.icon,
                                contentDescription = tab.label
                            )
                        },
                        label = { Text(text = tab.label) }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = AlertsRoute,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable<DashboardRoute> {
                PlaceholderScreen("Dashboard")
            }
            composable<AlertsRoute> {
                val viewModel: AlertViewModel = hiltViewModel()
                AlertsView(viewModel = viewModel)
            }
            composable<ReportsRoute> {
                PlaceholderScreen("Reports")
            }
            composable<SchedulesRoute> {
                PlaceholderScreen("Schedules")
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
fun PlaceholderScreen(name: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "$name Screen (Coming Soon)")
    }
}
