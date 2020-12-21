package com.kotlinflowdemo.ui.fragments.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kotlinflowdemo.R
import com.kotlinflowdemo.databinding.FragmentMainBinding
import com.kotlinflowdemo.ui.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

class MainFragment : Fragment(R.layout.fragment_main), View.OnClickListener {

    // Scoped to the lifecycle of the fragment's view (between onCreateView and onDestroyView)
    private var fragmentMainBinding: FragmentMainBinding? = null

    val viewModel: MainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentMainBinding.bind(view)
        fragmentMainBinding = binding

        binding.button1.setOnClickListener(this)
        binding.button2.setOnClickListener(this)
        binding.button3.setOnClickListener(this)
        binding.button4.setOnClickListener(this)
    }

    override fun onDestroyView() {
        // Consider not storing the binding instance in a field, if not needed.
        fragmentMainBinding = null
        super.onDestroyView()
    }

    override fun onClick(view: View?) {

        when (view?.id) {
            R.id.button_1 -> {
                findNavController().navigate(
                    R.id.action_mainFragment_to_weatherForecastOneShotFragment,
                    null
                )
            }

        }
    }

}