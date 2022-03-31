package com.rodrigmatrix.weatheryou.domain.model

data class WeatherDay(
    val dateTime: Long,
    val weatherCondition: String,
    val temperature: Double,
    val maxTemperature: Double,
    val minTemperature: Double,
    val weatherIcons: WeatherIcons,
    val hours: List<WeatherHour>,
    val precipitationProbability: Double,
    val precipitationType: String,
    val windSpeed: Double,
    val humidity: Double,
    val sunrise: Long,
    val sunset: Long
)