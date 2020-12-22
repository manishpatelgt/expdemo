package com.kotlinflowdemo.ui.fragments.weatherforecast.datastream

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kotlinflowdemo.R
import com.kotlinflowdemo.databinding.FragmentWeatherForecastDataStreamBinding
import com.kotlinflowdemo.other.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherForecastDataStreamFragment : Fragment(R.layout.fragment_weather_forecast_data_stream) {


    // Scoped to the lifecycle of the fragment's view (between onCreateView and onDestroyView)
    private var fragmentWeatherForecastDataStreamBinding: FragmentWeatherForecastDataStreamBinding? =
        null

    val viewModel: WeatherForecastDataStreamViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentWeatherForecastDataStreamBinding.bind(view)
        fragmentWeatherForecastDataStreamBinding = binding

        // Observe weather forecast data stream
        viewModel.weatherForecast.observe(viewLifecycleOwner, {
            when (it) {
                Result.Loading -> {
                    binding.tvDegree.text = "Loading"
                    //Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
                }
                is Result.Success -> {
                    // Update weather data
                    binding.tvDegree.text = it.data.toString()
                }
                Result.Error -> {
                    binding.tvDegree.text = "Error"
                    //Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

}