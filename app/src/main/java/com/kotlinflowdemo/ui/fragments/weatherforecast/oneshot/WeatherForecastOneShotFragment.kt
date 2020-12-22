package com.kotlinflowdemo.ui.fragments.weatherforecast.oneshot

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kotlinflowdemo.R
import com.kotlinflowdemo.other.Result
import com.kotlinflowdemo.databinding.FragmentWeatherForecastOneShotBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherForecastOneShotFragment : Fragment(R.layout.fragment_weather_forecast_one_shot) {

    // Scoped to the lifecycle of the fragment's view (between onCreateView and onDestroyView)
    private var fragmentWeatherForecastOneShotBinding: FragmentWeatherForecastOneShotBinding? = null

    val viewModel: WeatherForecastOneShotViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentWeatherForecastOneShotBinding.bind(view)
        fragmentWeatherForecastOneShotBinding = binding

        // It is always a good practice to use viewLifecycleOwner since
        // lifecycle of fragment might be longer than its view in some cases
        viewModel.weatherForecast.observe(viewLifecycleOwner,  {
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