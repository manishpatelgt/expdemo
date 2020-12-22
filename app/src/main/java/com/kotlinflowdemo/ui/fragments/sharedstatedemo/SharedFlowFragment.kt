package com.kotlinflowdemo.ui.fragments.sharedstatedemo

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.kotlinflowdemo.R
import com.kotlinflowdemo.databinding.FragmentSharedFlowBinding
import kotlinx.coroutines.flow.collect

class SharedFlowFragment : Fragment(R.layout.fragment_shared_flow) {

    // Scoped to the lifecycle of the fragment's view (between onCreateView and onDestroyView)
    private var fragmentSharedFlowBinding: FragmentSharedFlowBinding? = null

    val viewModel: SharedFlowViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSharedFlowBinding.bind(view)
        fragmentSharedFlowBinding = binding

        lifecycleScope.launchWhenStarted {
            viewModel.timerSharedValue.collect {
                println("value: $it")
                binding.tvDegree.text = it.toString()
            }
        }
    }
}