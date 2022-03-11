package com.rodrigmatrix.weatheryou.data.model.openweather


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenWeatherCurrent(
    @SerialName("clouds")
    val clouds: Int? = null,
    @SerialName("dew_point")
    val dewPoint: Double? = null,
    @SerialName("dt")
    val datetime: Long? = null,
    @SerialName("feels_like")
    val feelsLike: Double? = null,
    @SerialName("humidity")
    val humidity: Double? = null,
    @SerialName("pressure")
    val pressure: Int? = null,
    @SerialName("sunrise")
    val sunrise: Long? = null,
    @SerialName("sunset")
    val sunset: Long? = null,
    @SerialName("temp")
    val temp: Double? = null,
    @SerialName("uvi")
    val uvi: Double? = null,
    @SerialName("visibility")
    val visibility: Double? = null,
    @SerialName("weather")
    val weather: List<OpenWeatherWeather>? = null,
    @SerialName("wind_deg")
    val windDeg: Double? = null,
    @SerialName("wind_gust")
    val windGust: Double? = null,
    @SerialName("wind_speed")
    val windSpeed: Double? = null
)