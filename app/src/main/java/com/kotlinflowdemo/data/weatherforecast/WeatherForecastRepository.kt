package com.kotlinflowdemo.data.weatherforecast

import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import com.kotlinflowdemo.other.Result

class WeatherForecastRepository @Inject constructor() {

    /**
     * This methods is used to make one shot request to get
     * fake weather forecast data
     */
    fun fetchWeatherForecast() = flow {
        emit(Result.Loading)
        // Fake api call
        delay(1000)
        // Send a random fake weather forecast data
        emit(Result.Success((0..20).random()))
    }

    /**
     * This method is used to get data stream of fake weather
     * forecast data in real time
     */
    fun fetchWeatherForecastRealTime() = flow {
        emit(Result.Loading)
        // Fake data stream
        while (true) {
            delay(1000)
            // Send a random fake weather forecast data
            emit(Result.Success((0..20).random()))
        }
    }

}