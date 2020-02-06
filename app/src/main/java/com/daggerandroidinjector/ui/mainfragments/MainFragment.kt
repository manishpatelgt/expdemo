package com.daggerandroidinjector.ui.mainfragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.daggerandroidinjector.R
import com.daggerandroidinjector.utils.viewModelProvider
import dagger.android.support.AndroidSupportInjection
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

    /*override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
    }*/

}