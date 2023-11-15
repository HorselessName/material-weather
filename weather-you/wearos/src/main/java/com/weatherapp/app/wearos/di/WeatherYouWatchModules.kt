package com.weatherapp.app.wearos.di

import com.rodrigmatrix.weatheryou.wearos.presentation.home.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object WeatherYouWatchModules {

    fun loadModules() {
        loadKoinModules(com.weatherapp.app.wearos.di.WeatherYouWatchModules.domainModule + com.weatherapp.app.wearos.di.WeatherYouWatchModules.presentationModule)
    }

    private val domainModule = module {
        single {
            com.weatherapp.app.wearos.domain.usecase.GetCurrentLocationUseCase(
                weatherRepository = get()
            )
        }
        single {
            com.weatherapp.app.wearos.domain.usecase.GetLocationWeatherUseCase(
                weatherRepository = get()
            )
        }
    }

    private val presentationModule = module {
        viewModel {
            HomeViewModel(
                getCurrentLocationUseCase = get(),
                getLocationWeatherUseCase = get()
            )
        }
    }
}