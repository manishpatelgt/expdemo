package com.kotlinflowdemo.ui.fragments.weatherforecast.oneshot

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kotlinflowdemo.data.weatherforecast.WeatherForecastRepository
import com.kotlinflowdemo.other.Result

class WeatherForecastOneShotViewModel @ViewModelInject constructor(
    private val weatherForecastRepository: WeatherForecastRepository
) : ViewModel() {

    private val _weatherForecast = weatherForecastRepository
        .fetchWeatherForecast()
        .asLiveData(viewModelScope.coroutineContext) // Use viewModel scope for auto cancellation

    val weatherForecast: LiveData<Result<Int>>
        get() = _weatherForecast
}