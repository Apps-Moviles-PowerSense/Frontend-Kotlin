package com.soda.powersense.shared.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.soda.powersense.alerts.data.local.AlertDao
import com.soda.powersense.alerts.data.local.AlertEntity
import com.soda.powersense.auth.data.local.UserDao
import com.soda.powersense.auth.data.local.UserEntity

@Database(entities = [AlertEntity::class, UserEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun alertDao(): AlertDao
    abstract fun userDao(): UserDao
}