package com.weatherapp.app.wearos

import android.app.Application
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.rodrigmatrix.weatheryou.data.di.WeatherYouDataModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class WeatherYouWearOsApp: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@WeatherYouWearOsApp)
            WeatherYouDataModules.loadModules()
            com.weatherapp.app.wearos.di.WeatherYouWatchModules.loadModules()
        }
        initRemoteConfig()
    }

    private fun initRemoteConfig() {
        val remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = if (BuildConfig.DEBUG) 0 else 3600
            fetchTimeoutInSeconds = if (BuildConfig.DEBUG) 0 else 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.fetchAndActivate()
    }
}