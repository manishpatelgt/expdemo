package com.kotlinflowdemo.ui.fragments.weatherforecast.datastreamflow

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.kotlinflowdemo.data.weatherforecast.WeatherForecastRepository
import kotlinx.coroutines.flow.Flow
import com.kotlinflowdemo.other.Result

class WeatherForecastDataStreamFlowViewModel @ViewModelInject constructor(
    val weatherForecastRepository: WeatherForecastRepository
) : ViewModel() {

    private val _weatherForecast = weatherForecastRepository
        .fetchWeatherForecastRealTime()

    val weatherForecast: Flow<Result<Int>>
        get() = _weatherForecast
}