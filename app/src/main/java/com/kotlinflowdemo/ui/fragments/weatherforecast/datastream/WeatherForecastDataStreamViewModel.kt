package com.kotlinflowdemo.ui.fragments.weatherforecast.datastream

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kotlinflowdemo.data.weatherforecast.WeatherForecastRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import com.kotlinflowdemo.other.Result

class WeatherForecastDataStreamViewModel @ViewModelInject constructor(
    val weatherForecastRepository: WeatherForecastRepository
): ViewModel() {

    private val _weatherForecast = weatherForecastRepository
        .fetchWeatherForecastRealTime()
        .map {
            // Do some heavy operation. This operation will be done in the
            // scope of this flow collected. In our case it is the scope
            // passed to asLiveData extension function
            // This operation will not block the UI
            delay(1000)
            it
        }
        .asLiveData(
            // Use Default dispatcher for CPU intensive work and
            // viewModel scope for auto cancellation
            Dispatchers.Default + viewModelScope.coroutineContext
        )

    val weatherForecast: LiveData<Result<Int>>
        get() = _weatherForecast

}