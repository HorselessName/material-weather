package com.rodrigmatrix.weatheryou.domain.repository

import com.rodrigmatrix.weatheryou.domain.model.City
import com.rodrigmatrix.weatheryou.domain.model.SearchAutocompleteLocation
import com.rodrigmatrix.weatheryou.domain.model.SearchLocation
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    fun searchLocation(locationName: String): Flow<List<SearchAutocompleteLocation>>

    fun getLocation(placeId: String): Flow<SearchLocation>

    fun getFamousCities(): Flow<List<City>>
}