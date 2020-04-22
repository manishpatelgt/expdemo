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

package com.expdemo.ui.vb

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.expdemo.R
import com.expdemo.databinding.FragmentThirdBinding

/**
 * Created by Manish Patel on 4/22/2020.
 */
class ThirdFragment : BaseFragment<FragmentViewModel, FragmentThirdBinding>() {

    override fun getFragmentView() = R.layout.fragment_third

    override val mViewModel: FragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding.txtView.text = "Best way just 2-3 lines of code and hell yes"
    }
}