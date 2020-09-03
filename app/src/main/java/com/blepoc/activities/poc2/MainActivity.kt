package com.blepoc.activities.poc2

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.blepoc.App
import com.blepoc.App.Companion.context
import com.blepoc.R
import com.blepoc.activities.BaseActivity
import com.blepoc.databinding.ActivityMain2Binding
import com.blepoc.utility.Utils
import com.blepoc.utility.isAtLeastAndroid6
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import com.livinglifetechway.quickpermissions_kotlin.util.QuickPermissionsOptions
import com.polidea.rxandroidble2.exceptions.BleScanException
import com.polidea.rxandroidble2.scan.ScanFilter
import com.polidea.rxandroidble2.scan.ScanResult
import com.polidea.rxandroidble2.scan.ScanSettings
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main2.*

/**
 * Created by Manish Patel on 9/3/2020.
 */
class MainActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMain2Binding
    private val rxBleClient = App.rxBleClient
    private var scanDisposable: Disposable? = null
    private val resultsAdapter = ScanResultsAdapter { }
    private val isScanning: Boolean
        get() = scanDisposable != null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI()
    }

    fun setupUI() {
        configureResultList()
        binding.scanBtn.setOnClickListener(this)
    }

    private fun configureResultList() {
        with(scan_results) {
            setHasFixedSize(true)
            itemAnimator = null
            adapter = resultsAdapter
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

    fun checkForBluetooth() {
        var mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (mBluetoothAdapter != null) {
            if (!mBluetoothAdapter.isEnabled) {
                //Call intent for Bluetooth
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, REQ_BT_ENABLE)
            } else {
                startScanning()
            }
        } else {
            Utils.showToast(getString(R.string.bluetooth_error_message))
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_BT_ENABLE) {
            if (resultCode == RESULT_OK) {
                startScanning()
            }
            if (resultCode == RESULT_CANCELED) {
                Utils.showToast("Something went wrong. Please turn on Bluetooth")
            }
        }
    }

    private fun onScanToggleClick() {
        if (isScanning) {
            scanDisposable?.dispose()
        } else {
            askForPermissions()
        }
        updateButtonUIState()
    }

    private fun startScanning() {
        scanBleDevices()
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { dispose() }
            .subscribe({ resultsAdapter.addScanResult(it) }, { onScanFailure(it) })
            .let { scanDisposable = it }

        updateButtonUIState()
    }

    private fun scanBleDevices(): Observable<ScanResult> {
        val scanSettings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
            .build()

        val scanFilter = ScanFilter.Builder()
            .build()

        return rxBleClient.scanBleDevices(scanSettings, scanFilter)
    }

    private fun dispose() {
        scanDisposable = null
        resultsAdapter.clearScanResults()
        updateButtonUIState()
    }

    private fun onScanFailure(throwable: Throwable) {
        if (throwable is BleScanException) {
            Utils.showToast(throwable.message.toString())
        }
    }

    private fun stopScanning() {
        // Stop scanning in onPause callback.
        if (isScanning) scanDisposable?.dispose()
        updateButtonUIState()
    }

    private fun updateButtonUIState() {
        if (isScanning) {
            binding.scanBtn.setText(R.string.stop_scanning)
        } else {
            binding.scanBtn.setText(R.string.start_scanning)
        }
    }


    public override fun onPause() {
        super.onPause()
        stopScanning()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.scan_btn -> {
                onScanToggleClick()
            }
        }
    }

    companion object {
        private const val REQ_BT_ENABLE = 100
        fun getIntent() = Intent(context, MainActivity::class.java)
    }
}