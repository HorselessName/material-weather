package com.weatherapp.app.wearos.presentation.home.viewmodel

import androidx.lifecycle.viewModelScope
import com.rodrigmatrix.weatheryou.core.viewmodel.ViewEffect
import com.rodrigmatrix.weatheryou.core.viewmodel.ViewModel
import com.rodrigmatrix.weatheryou.data.exception.CurrentLocationNotFoundException
import com.rodrigmatrix.weatheryou.wearos.R
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.io.IOException

class HomeViewModel(
    private val getCurrentLocationUseCase: com.weatherapp.app.wearos.domain.usecase.GetCurrentLocationUseCase,
    private val getLocationWeatherUseCase: com.weatherapp.app.wearos.domain.usecase.GetLocationWeatherUseCase,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel<HomeViewState, ViewEffect>(HomeViewState()) {

    init {
        loadLocation()
    }

    fun loadLocation() {
        viewModelScope.launch {
            getCurrentLocationUseCase()
                .flowOn(coroutineDispatcher)
                .onStart { setState { it.copy(isLoading = true, error = null) } }
                .catch { exception ->
                    exception.handleError()
                }
                .collect { weatherLocation ->
                    setState {
                        it.copy(
                            isLoading = false,
                            error = null,
                            weatherLocation = weatherLocation
                        )
                    }
                }
        }
    }

    private fun Throwable.handleError() {
        val errorString = when (this) {
            is CurrentLocationNotFoundException -> R.string.current_location_not_found
            is IOException -> R.string.no_internet_connect
            else -> R.string.generic_error
        }
        setState { it.copy(error = errorString, isLoading = false) }
    }
}


