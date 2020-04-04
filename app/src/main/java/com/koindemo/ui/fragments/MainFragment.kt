package com.koindemo.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.koindemo.R
import org.koin.android.viewmodel.ext.android.viewModel
import com.koindemo.databinding.MainFragmentBinding

/**
 * Created by Manish Patel on 4/4/2020.
 */
class MainFragment : Fragment(R.layout.main_fragment) {

    // Scoped to the lifecycle of the fragment's view (between onCreateView and onDestroyView)
    private var mainFragmentBinding: MainFragmentBinding? = null

    val mainFragmentViewModel: MainFragmentViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = MainFragmentBinding.bind(view)
        mainFragmentBinding = binding
    }

    override fun onDestroyView() {
        // Consider not storing the binding instance in a field, if not needed.
        mainFragmentBinding = null
        super.onDestroyView()
    }
}