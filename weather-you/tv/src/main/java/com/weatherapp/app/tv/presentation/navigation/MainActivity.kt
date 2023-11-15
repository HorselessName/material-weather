package com.weatherapp.app.tv.presentation.navigation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.weatherapp.app.tv.presentation.home.HomeScreen
import com.weatherapp.app.tv.presentation.theme.WeatherYouTheme

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherYouTheme {
                HomeScreen()
            }
        }
    }
}