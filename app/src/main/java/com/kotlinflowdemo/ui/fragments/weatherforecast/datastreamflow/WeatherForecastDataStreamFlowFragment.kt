package com.kotlinflowdemo.ui.fragments.weatherforecast.datastreamflow

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import kotlinx.coroutines.flow.collect
import androidx.lifecycle.lifecycleScope
import com.kotlinflowdemo.R
import com.kotlinflowdemo.other.Result
import com.kotlinflowdemo.databinding.FragmentWeatherForecastDataStreamFlowBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherForecastDataStreamFlowFragment :
    Fragment(R.layout.fragment_weather_forecast_data_stream_flow) {

    // Scoped to the lifecycle of the fragment's view (between onCreateView and onDestroyView)
    private var fragmentWeatherForecastDataStreamFlowBinding: FragmentWeatherForecastDataStreamFlowBinding? =
        null

    val viewModel: WeatherForecastDataStreamFlowViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentWeatherForecastDataStreamFlowBinding.bind(view)
        fragmentWeatherForecastDataStreamFlowBinding = binding

        // Consume data when fragment is started
        lifecycleScope.launchWhenStarted {
            // Since collect is a suspend function it needs to be called
            // from a coroutine scope
            viewModel.weatherForecast.collect {
                when (it) {
                    Result.Loading -> {
                        //Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
                        binding.tvDegree.text = "Loading"
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
            }
        }
    }

}