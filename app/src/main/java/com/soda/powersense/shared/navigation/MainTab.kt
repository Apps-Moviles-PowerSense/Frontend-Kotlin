package com.soda.powersense.shared.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.outlined.Assessment
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.ui.graphics.vector.ImageVector

enum class MainTab(
    val label: String,
    val icon: ImageVector,
    val iconFilled: ImageVector,
    val route: Any
) {
    Dashboard(
        label = "Dashboard",
        icon = Icons.Outlined.Dashboard,
        iconFilled = Icons.Filled.Dashboard,
        route = DashboardRoute
    ),
    Devices(
        label = "Dispositivos",
        icon = Icons.Outlined.Devices,
        iconFilled = Icons.Filled.Devices,
        route = DevicesRoute
    ),
    Schedules(
        label = "Programación",
        icon = Icons.Outlined.Schedule,
        iconFilled = Icons.Filled.Schedule,
        route = SchedulesRoute
    ),
    /*Reports(
        label = "Reportes",
        icon = Icons.Outlined.Assessment,
        iconFilled = Icons.Filled.Assessment,
        route = ReportsRoute
    ),*/
    Alerts(
        label = "Alertas",
        icon = Icons.Outlined.Notifications,
        iconFilled = Icons.Filled.Notifications,
        route = AlertsRoute
    ),
    Profile(
        label = "Perfil",
        icon = Icons.Outlined.Person,
        iconFilled = Icons.Filled.Person,
        route = ProfileRoute
    )
}
