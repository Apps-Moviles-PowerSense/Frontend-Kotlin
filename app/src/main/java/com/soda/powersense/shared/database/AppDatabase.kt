package com.soda.powersense.shared.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.soda.powersense.alerts.data.local.AlertDao
import com.soda.powersense.alerts.data.local.AlertEntity
import com.soda.powersense.auth.data.local.UserDao
import com.soda.powersense.auth.data.local.UserEntity
import com.soda.powersense.dashboard.data.local.DashboardDao
import com.soda.powersense.dashboard.data.local.DashboardKPIsEntity
import com.soda.powersense.devices.data.local.DeviceDao
import com.soda.powersense.devices.data.local.DeviceEntity
import com.soda.powersense.reports.data.local.DepartmentMetricLocalEntity
import com.soda.powersense.reports.data.local.MonthlyComparisonLocalEntity
import com.soda.powersense.reports.data.local.ReportDao
import com.soda.powersense.reports.data.local.ReportHistoryLocalEntity
import com.soda.powersense.reports.data.local.ReportKPIsLocalEntity
import com.soda.powersense.schedules.data.local.ScheduleDao
import com.soda.powersense.schedules.data.local.ScheduleLocalEntity

@Database(
    entities = [
        AlertEntity::class,
        UserEntity::class,
        DeviceEntity::class,
        DashboardKPIsEntity::class,
        ScheduleLocalEntity::class,
        ReportKPIsLocalEntity::class,
        MonthlyComparisonLocalEntity::class,
        DepartmentMetricLocalEntity::class,
        ReportHistoryLocalEntity::class
    ],
    version = 6,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun alertDao(): AlertDao
    abstract fun userDao(): UserDao
    abstract fun deviceDao(): DeviceDao
    abstract fun dashboardDao(): DashboardDao
    abstract fun scheduleDao(): ScheduleDao
    abstract fun reportDao(): ReportDao
}
