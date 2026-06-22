package com.soda.powersense.alerts.data.repository

import com.soda.powersense.alerts.data.local.AlertDao
import com.soda.powersense.alerts.data.mapper.toDomain
import com.soda.powersense.alerts.data.mapper.toEntity
import com.soda.powersense.alerts.data.remote.AlertService
import com.soda.powersense.alerts.domain.model.Alert
import com.soda.powersense.alerts.domain.repository.AlertRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class AlertRepositoryImpl @Inject constructor(
    private val alertService: AlertService,
    private val alertDao: AlertDao
) : AlertRepository {
    override fun getAlerts(): Flow<List<Alert>> {
        return alertDao.getAlerts().map { alertEntities ->
            alertEntities.map { alertEntity -> alertEntity.toDomain() }
        }
    }

    override suspend fun syncAlerts() {
        val response = alertService.getAlerts()

        if (response.isSuccessful) {
            response.body()?.let { alertDtos ->
                alertDtos.forEach { alertDto ->
                    alertDao.upsert(alertDto.toEntity())
                }
            }
        }
    }

    override suspend fun acknowledgeAlert(id: String): Result<Unit> {
        return try {
            val response = alertService.acknowledgeAlert(id)
            if (response.isSuccessful) {
                alertDao.acknowledgeAlert(id)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error acknowledging alert: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun acknowledgeAllAlerts(): Result<Unit> {
        return try {
            val unacknowledged = alertDao.getAlerts().first().filter { !it.acknowledged }
            unacknowledged.forEach { alert ->
                alertService.acknowledgeAlert(alert.id)
                alertDao.acknowledgeAlert(alert.id)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun clearLocalAlerts() {
        alertDao.clearAlerts()
    }
}
