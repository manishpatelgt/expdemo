/*
 * Copyright 2020 Manish Patel. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.expdemo.ui.fragmentdatasharing

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Preconditions
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentResultListener
import com.expdemo.databinding.FragmentFirstBinding
import com.expdemo.utils.constants.Constants
import timber.log.Timber

/**
 * Created by Manish Patel on 5/9/2020.
 */
class FragmentOne : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpResultListener()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val bundle = arguments
        bundle?.let {
            val userId = it.getString(Constants.USER_ID)
            val userName = it.getString(Constants.USER_NAME)
            Timber.e("UserId: $userId")
            Timber.e("UserName: $userName")
        }

        binding.button.setOnClickListener {
            val myDialogFragment = MyDialogFragment()
            myDialogFragment.setTargetFragment(
                FragmentOne@ this,
                Constants.REQ_CODE_SECOND_FRAGMENT
            )
            myDialogFragment.show(parentFragmentManager, "Dialog")
        }
    }

    private fun setUpResultListener() {
        /** new way **/
        parentFragmentManager.setFragmentResultListener(
            Constants.REQUEST_KEY,
            this,
            FragmentResultListener { requestKey, result ->
                onFragmentResult(requestKey, result)
            })
    }

    @SuppressLint("RestrictedApi")
    private fun onFragmentResult(requestKey: String, result: Bundle) {
        Preconditions.checkState(Constants.REQUEST_KEY == requestKey)

        val dialogFragmentData = result.getString(Constants.DATA_FROM)
        Timber.e("dialogFragmentData: $dialogFragmentData")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.REQ_CODE_SECOND_FRAGMENT) {
                val dialogFragmentData = intent?.getStringExtra(Constants.DATA_FROM)
                Timber.e("dialogFragmentData: $dialogFragmentData")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}