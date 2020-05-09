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

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.expdemo.databinding.FragmentSecondBinding
import com.expdemo.utils.constants.Constants

/**
 * Created by Manish Patel on 5/9/2020.
 */
class MyDialogFragment : DialogFragment() {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.button.setOnClickListener {

            /** older way **/
            val intent = Intent()
            intent.putExtra(Constants.DATA_FROM, "Manish")
            targetFragment!!.onActivityResult(
                Constants.REQ_CODE_SECOND_FRAGMENT,
                Activity.RESULT_OK, intent
            )

            /**new way **/
            parentFragmentManager.setFragmentResult(
                Constants.REQUEST_KEY,
                bundleOf(Constants.DATA_FROM to "Manish 2")
            )

            dismissAllowingStateLoss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}