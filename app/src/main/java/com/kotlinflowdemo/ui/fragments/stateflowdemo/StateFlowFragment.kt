package com.kotlinflowdemo.ui.fragments.stateflowdemo

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.kotlinflowdemo.R
import com.kotlinflowdemo.databinding.FragmentStateFlowBinding
import kotlinx.coroutines.flow.collect

class StateFlowFragment : Fragment(R.layout.fragment_state_flow) {

    // Scoped to the lifecycle of the fragment's view (between onCreateView and onDestroyView)
    private var fragmentStateFlowBinding: FragmentStateFlowBinding? = null

    val viewModel: StateFlowViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentStateFlowBinding.bind(view)
        fragmentStateFlowBinding = binding

        /** button listeners **/
        binding.loginBtn.setOnClickListener {
            viewModel.loginUser(
                binding.txtInputEditUsername.text.toString(),
                binding.txtInputEditPassword.text.toString()
            )
        }

        lifecycleScope.launchWhenStarted {

            viewModel.loginUiState.collect {
                when (it) {
                    is StateFlowViewModel.LoginUiState.Success -> {
                        Snackbar.make(
                            binding.root,
                            "Successfully logged in", Snackbar.LENGTH_LONG
                        ).show()
                        binding.progressBar.isVisible = false
                    }
                    is StateFlowViewModel.LoginUiState.Error -> {
                        Snackbar.make(
                            binding.root,
                            it.message, Snackbar.LENGTH_LONG
                        ).show()
                        binding.progressBar.isVisible = false
                    }
                    is StateFlowViewModel.LoginUiState.Loading -> {
                        binding.progressBar.isVisible = true
                    }
                    else -> Unit
                }
            }
        }
    }

}