package com.rodrigmatrix.weatheryou.data.mapper

import com.rodrigmatrix.weatheryou.data.model.openweather.OpenWeatherDaily
import com.rodrigmatrix.weatheryou.data.model.openweather.OpenWeatherHourly
import com.rodrigmatrix.weatheryou.data.model.openweather.OpenWeatherLocationResponse
import com.rodrigmatrix.weatheryou.data.model.openweather.OpenWeatherMinutely
import com.rodrigmatrix.weatheryou.domain.model.WeatherDay
import com.rodrigmatrix.weatheryou.domain.model.WeatherHour
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation
import com.rodrigmatrix.weatheryou.presentation.extensions.getLocalTime

class OpenWeatherRemoteMapper(
    private val weatherIconMapper: OpenWeatherIconMapper
) {

    fun map(source: OpenWeatherLocationResponse): WeatherLocation {
        return WeatherLocation(
            name = source.name.orEmpty().split(",").dropLast(1).joinToString(),
            currentWeather = source.current?.temp ?: 0.0,
            currentWeatherDescription = source.current?.weather?.first()?.description.orEmpty(),
            maxTemperature = source.daily?.firstOrNull()?.temp?.max ?: 0.0,
            lowestTemperature = source.daily?.firstOrNull()?.temp?.min ?: 0.0,
            feelsLike = source.current?.feelsLike ?: 0.0,
            weatherIcons = weatherIconMapper.map(source.current?.weather?.firstOrNull()),
            currentTime = source.current?.datetime?.toTimeString().orEmpty(),
            timeZone = source.timezone.orEmpty(),
            precipitationProbability = source.minutely.toChanceOfPrecipitation(source.current?.datetime ?: 0L),
            precipitationType = source.current?.weather?.firstOrNull()?.main.orEmpty(),
            humidity = source.current?.humidity?.toDouble() ?: 0.0,
            dewPoint = source.current?.dewPoint ?: 0.0,
            windDirection = source.current?.windDeg ?: 0.0,
            windSpeed = source.current?.windSpeed ?: 0.0,
            uvIndex = source.current?.uvi?.toDouble() ?: 0.0,
            sunrise = source.current?.sunrise?.toTimeString().orEmpty(),
            sunset = source.current?.sunset?.toTimeString().orEmpty(),
            visibility = source.current?.visibility?.toDouble() ?: 0.0,
            pressure = source.current?.pressure?.toDouble() ?: 0.0,
            days = source.daily?.mapDaysList().orEmpty(),
            hours = source.getTodayHoursList()
        )
    }

    private fun List<OpenWeatherDaily>.mapDaysList(): List<WeatherDay> {
        return this.map {
            WeatherDay(
                dateTime = it.datetime?.toTimeString().orEmpty(),
                weatherCondition = it.weather?.firstOrNull()?.main.orEmpty(),
                temperature = it.temp?.day ?: 0.0,
                maxTemperature = it.temp?.max ?: 0.0,
                minTemperature = it.temp?.min ?: 0.0,
                weatherIcons = weatherIconMapper.map(it.weather?.first()),
                precipitationProbability = it.pop ?: 0.0,
                precipitationType = it.weather?.firstOrNull()?.main.orEmpty(),
                windSpeed = it.windSpeed ?: 0.0,
                humidity = it.humidity?.toDouble() ?: 0.0,
                sunrise = it.sunrise?.toTimeString().orEmpty(),
                sunset = it.sunset?.toTimeString().orEmpty(),
                hours = emptyList()
            )
        }
    }

    private fun List<OpenWeatherHourly>.mapHoursList(): List<WeatherHour> {
        return this.map {
            WeatherHour(
                dateTime = it.datetime?.toTimeString().orEmpty(),
                weatherCondition = it.weather?.first()?.description.orEmpty(),
                temperature = it.temp ?: 0.0,
                weatherIcons = weatherIconMapper.map(it.weather?.firstOrNull()),
                precipitationProbability = it.pop ?: 0.0,
                precipitationType = it.weather?.firstOrNull()?.main.orEmpty()
            )
        }
    }

    private fun OpenWeatherLocationResponse.getTodayHoursList(): List<WeatherHour> {
        return hourly?.mapHoursList().orEmpty()
    }

    private fun Long.toTimeString(): String {
        return this.getLocalTime().toString("hh::mm::ss")
    }

    private fun List<OpenWeatherMinutely>?.toChanceOfPrecipitation(currentTime: Long): Double {
        return this?.find { it.datetime == currentTime }?.precipitation ?: 0.0
    }
}