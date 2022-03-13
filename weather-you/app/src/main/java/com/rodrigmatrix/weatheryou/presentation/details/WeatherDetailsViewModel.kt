package com.rodrigmatrix.weatheryou.presentation.details

import com.rodrigmatrix.weatheryou.core.viewmodel.ViewModel
import com.rodrigmatrix.weatheryou.domain.model.WeatherDay
import com.rodrigmatrix.weatheryou.domain.model.WeatherLocation

private const val EXPANDED_LIST_SIZE = 15
private const val COLLAPSED_LIST_SIZE = 7

class WeatherDetailsViewModel
    : com.rodrigmatrix.weatheryou.core.viewmodel.ViewModel<WeatherDetailsViewState, WeatherDetailsViewEffect>(WeatherDetailsViewState()) {

    fun setWeatherLocation(weatherLocation: com.rodrigmatrix.weatheryou.domain.model.WeatherLocation?) {
        if (weatherLocation != null) {
            setState {
                it.copy(
                    weatherLocation = weatherLocation,
                    todayWeatherHoursList = weatherLocation.hours,
                    futureDaysList = weatherLocation
                        .days
                        .take(if (it.isFutureWeatherExpanded) 15 else 7)
                )
            }
        }
    }

    fun onFutureWeatherButtonClick(isExpanded: Boolean) {
        setState {
            it.copy(
                futureDaysList = it.futureDaysList.getFutureDaysList(isExpanded),
                isFutureWeatherExpanded = isExpanded
            )
        }
    }

    private fun List<com.rodrigmatrix.weatheryou.domain.model.WeatherDay>.getFutureDaysList(isExpanded: Boolean): List<com.rodrigmatrix.weatheryou.domain.model.WeatherDay> {
        return this.take(if (isExpanded) EXPANDED_LIST_SIZE else COLLAPSED_LIST_SIZE)
    }
}