package com.rodrigmatrix.weatheryou.domain.repository

import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    fun addLocation(name: String, latitude: Double, longitude: Double): Flow<Unit>

    fun fetchLocationsList(): Flow<List<WeatherLocation>>

    fun deleteLocation(weatherLocation: WeatherLocation): Flow<Unit>
}