package com.rodrigmatrix.weatheryou.data.remote

import com.rodrigmatrix.weatheryou.domain.model.TemperaturePreference
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import kotlinx.coroutines.flow.Flow

interface WeatherYouRemoteDataSource {

    fun getWeather(
        latitude: Double,
        longitude: Double,
        unit: TemperaturePreference
    ): Flow<WeatherLocation>
}