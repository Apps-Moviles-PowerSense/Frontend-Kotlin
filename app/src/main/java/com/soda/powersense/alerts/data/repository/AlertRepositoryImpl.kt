package com.soda.powersense.alerts.data.repository

import com.soda.powersense.alerts.data.local.AlertDao
import com.soda.powersense.alerts.data.mapper.toDomain
import com.soda.powersense.alerts.data.mapper.toEntity
import com.soda.powersense.alerts.data.remote.AlertService
import com.soda.powersense.alerts.domain.model.Alert
import com.soda.powersense.alerts.domain.repository.AlertRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
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

    override suspend fun acknowledgeAlert(id: String) {
        alertDao.acknowledgeAlert(id)
    }

    override suspend fun clearLocalAlerts() {
        alertDao.clearAlerts()
    }
}