package com.rodrigmatrix.weatheryou.domain.usecase

import com.rodrigmatrix.weatheryou.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow

class DeleteLocationUseCase(
    private val weatherRepository: WeatherRepository
) {

    operator fun invoke(id: Int): Flow<Unit> {
        return weatherRepository.deleteLocation(id)
    }
}