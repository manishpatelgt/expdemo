package com.blepoc.activities

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.blepoc.App
import com.blepoc.App.Companion.context
import com.blepoc.R
import com.blepoc.ble.AdvertiserService
import com.blepoc.ble.BLESScanService
import com.blepoc.databinding.ActivityMainBinding
import com.blepoc.utility.Utils
import com.blepoc.utility.isAtLeastAndroid6
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import com.livinglifetechway.quickpermissions_kotlin.util.QuickPermissionsOptions

class MainActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mBluetoothAdapter: BluetoothAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI()
    }

    fun setupUI() {
        binding.scanServiceBtn.setOnClickListener(this)
        binding.advertiseServiceBtn.setOnClickListener(this)
        updateUI()
        askForPermissions()
    }

    fun updateUI() {
        if (Utils.serviceIsRunning(context, BLESScanService::class.java.name)) {
            binding.scanServiceBtn.text = getString(R.string.stop_service)
        } else {
            binding.scanServiceBtn.text = getString(R.string.start_service)
        }

        if (Utils.serviceIsRunning(context, AdvertiserService::class.java.name)) {
            binding.advertiseServiceBtn.text = getString(R.string.stop_advertise_service)
        } else {
            binding.advertiseServiceBtn.text = getString(R.string.start_advertise_service)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.advertise_service_btn -> {
                //Check for service already running or not
                if (Utils.serviceIsRunning(context, AdvertiserService::class.java.name)) {
                    //Stop service
                    App.getInstance().stopAdvertiserService()
                } else {
                    App.getInstance().startAdvertiserService()
                }
                updateUI()
            }
            R.id.scan_service_btn -> {
                //Check for service already running or not
                if (Utils.serviceIsRunning(context, BLESScanService::class.java.name)) {
                    //Stop service
                    App.getInstance().stopBLEService()
                } else {
                    App.getInstance().startBLEService()
                }
                updateUI()
            }
        }
    }

    fun askForPermissions() {
        if (isAtLeastAndroid6()) {
            val quickPermissionsOption = QuickPermissionsOptions(
                handleRationale = true,
                rationaleMessage = Utils.getRationaleMessage(Manifest.permission.ACCESS_FINE_LOCATION),
                permanentlyDeniedMessage = getString(R.string.permission_message2),
                rationaleMethod = { req ->
                    rationaleCallback(
                        req,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                },
                permanentDeniedMethod = { req -> permissionsPermanentlyDenied(req) }
            )

            runWithPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                options = quickPermissionsOption
            ) {
                checkForBluetooth()
            }
        } else {
            checkForBluetooth()
        }
    }

    fun handleService() {
        //Check for service already running or not
        if (Utils.serviceIsRunning(context, BLESScanService::class.java.name)) {
            //Stop service
            App.getInstance().stopBLEService()
        } else {
            //Stop service
            App.getInstance().startBLEService()
        }
        updateUI()
    }

    fun checkForBluetooth() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (mBluetoothAdapter != null) {
            if (!mBluetoothAdapter.isEnabled) {
                //Call intent for Bluetooth
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, REQ_BT_ENABLE)
            }
        } else {
            Utils.showToast(getString(R.string.bluetooth_error_message))
        }

        //Check for service already running or not
        /*if (Utils.serviceIsRunning(context, BLEService::class.java.name)) {
            //Stop service
            App.getInstance().stopBLEService()
        } else {
            val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            if (mBluetoothAdapter != null) {
                if (!mBluetoothAdapter.isEnabled) {
                    //Call intent for Bluetooth
                    val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    startActivityForResult(enableBtIntent, REQ_BT_ENABLE)
                } else {
                    handleService()
                }
            } else {
                Utils.showToast(getString(R.string.bluetooth_error_message))
            }
        }*/
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_BT_ENABLE) {
            if (resultCode == RESULT_OK) {
                //handleService()
            }
            if (resultCode == RESULT_CANCELED) {
                Utils.showToast("Something went wrong. Please turn on Bluetooth")
            }
        }
    }

    companion object {
        private const val REQ_BT_ENABLE = 100
    }
}