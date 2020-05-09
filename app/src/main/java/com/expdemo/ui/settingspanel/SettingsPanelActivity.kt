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

package com.expdemo.ui.settingspanel

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.invoke
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.expdemo.databinding.ActivitySettingsPanelBinding
import com.expdemo.utils.constants.Constants.ANY_WEBSITE_URL
import com.expdemo.utils.extensions.isAtLeastAndroid9
import com.expdemo.utils.extensions.isNetworkAvailable
import com.google.android.material.snackbar.Snackbar
//StartActivityOnResult()
//https://pluu.github.io/blog/android/2020/03/24/activity-result/
//https://wajahatkarim.com/2020/05/activity-results-api-onactivityresult/
//https://developer.android.com/training/basics/intents/result
/**
 * Created by Manish Patel on 5/9/2020.
 */
class SettingsPanelActivity : AppCompatActivity() {

    lateinit var binding: ActivitySettingsPanelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsPanelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /** Set action bar */
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        loadWebPage()

        binding.button.setOnClickListener {
            pickImages("image/*")     // We want images, so we set the mimeType as "image/*"
           // askForPermission(android.Manifest.permission.CAMERA)
        }
    }

    private fun loadWebPage() {
        if (isNetworkAvailable(applicationContext)) {
            binding.webView.loadUrl(ANY_WEBSITE_URL)
        } else {
            displayNoConnectivityMessage()
        }
    }

    private fun displayNoConnectivityMessage() {
        Snackbar.make(binding.webView, "No Internet", Snackbar.LENGTH_INDEFINITE)
            .setAction("Retry") { displaySettingsPanel() }
            .show()
    }

    val askForPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted  ->
            if (isGranted) {
                //start camera
                var imageUri: Uri? = null
                takePicture(imageUri)
            }
        }

    val takePicture =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { bitmap: Bitmap? ->
            // Do something with the Bitmap, if present
            bitmap?.let {
                binding.imgView.setImageBitmap(it)
            }
        }

    // Pick Images Contract - Normally this is for all kind of files.
    private val pickImages = registerForActivityResult(ActivityResultContracts.GetContent()) {uri ->
        uri?.let {uri ->
            binding.imgView.setImageURI(uri)
        }
    }

    val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                //val intent = result.data
                // Handle the Intent
                loadWebPage()
            }
        }

    private fun displaySettingsPanel() {
        try {
            var intent = if (isAtLeastAndroid9()) {
                Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY)
            } else {
                Intent(Settings.ACTION_WIFI_SETTINGS)
            }
            startForResult(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            intent = Intent(Settings.ACTION_WIFI_SETTINGS)
            startForResult(intent)
        }

        // Older way to handle startActivityForResult()
        /*try {
            var intent = if (isAtLeastAndroid9()) {
                Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY)
            } else {
                Intent(Settings.ACTION_WIFI_SETTINGS)
            }
            startActivityForResult(intent, REQUEST_CODE)
        } catch (e: Exception) {
            e.printStackTrace()
            intent = Intent(Settings.ACTION_WIFI_SETTINGS)
            startActivityForResult(intent, REQUEST_CODE)
        }*/
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            loadWebPage()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        private const val REQUEST_CODE = 1000
        val TAG = SettingsPanelActivity::class.java.simpleName
    }
}