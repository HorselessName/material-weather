package com.rodrigmatrix.weatheryou.presentation.di

import com.rodrigmatrix.weatheryou.presentation.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { HomeViewModel(weatherRepository = get()) }
}