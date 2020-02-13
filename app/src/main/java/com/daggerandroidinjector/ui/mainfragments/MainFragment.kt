package com.daggerandroidinjector.ui.mainfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.daggerandroidinjector.R
import com.daggerandroidinjector.utils.viewModelProvider
import dagger.android.support.DaggerFragment
import javax.inject.Inject

/**
 * Created by Manish Patel on 2/6/2020.
 */
class MainFragment : DaggerFragment() {

    @Inject
    lateinit var modelFactory: ViewModelProvider.Factory

    private val model by lazy { viewModelProvider(modelFactory) as MainFragmentViewModel }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

}