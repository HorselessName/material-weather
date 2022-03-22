package com.rodrigmatrix.weatheryou.domain.repository

import com.rodrigmatrix.weatheryou.domain.model.TemperaturePreference
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    fun getTemperaturePreference(): Flow<TemperaturePreference>

    fun setTemperaturePreference(newPreference: TemperaturePreference): Flow<Unit>
}