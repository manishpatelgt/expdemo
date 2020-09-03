package com.blepoc.ble

import android.Manifest
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.bluetooth.le.BluetoothLeAdvertiser
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.os.IBinder
import android.os.ParcelUuid
import android.util.Log
import com.blepoc.App.Companion.context
import com.blepoc.utility.Utils
import com.blepoc.utility.bluetoothManager
import com.blepoc.utility.isAtLeastAndroid8
import com.blepoc.utility.notifications.NotificationHelper

/**
 * Created by Manish Patel on 8/6/2020.
 */
//https://code.tutsplus.com/tutorials/how-to-advertise-android-as-a-bluetooth-le-peripheral--cms-25426

class AdvertiserService : Service() {

    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var notificationHelper: NotificationHelper
    var running = false
    private var is_bluetooth_on = true
    private var mBluetoothLeAdvertiser: BluetoothLeAdvertiser? = null
    private var mAdvertiseCallback: AdvertiseCallback? = null
    private val mHandler = Handler()

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        Log.e(TAG, "AdvertiserService onCreate")
        notificationHelper = NotificationHelper(this)
        createNotification()
        running = true;
        initialize()
        startAdvertising()
        //setTimeout()

        /*val filter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        this.registerReceiver(mBluetoothReceiver, filter)
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()*/

        super.onCreate()
    }

    /**
     * Get references to system Bluetooth objects if we don't have them already.
     */
    private fun initialize() {
        if (mBluetoothLeAdvertiser == null) {
            val mBluetoothManager = context.bluetoothManager
            if (mBluetoothManager != null) {
                bluetoothAdapter = mBluetoothManager.adapter
                if (bluetoothAdapter != null) {
                    mBluetoothLeAdvertiser = bluetoothAdapter.bluetoothLeAdvertiser
                } else {
                    Log.e(TAG, "Bluetooth null")
                }
            } else {
                Log.e(TAG, "Bluetooth null")
            }
        }
    }

    private val mBluetoothReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (BluetoothAdapter.ACTION_STATE_CHANGED == intent.action) {
                val bluetoothState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)
                Log.e(BLESScanService.TAG, "BLE State: $bluetoothState")
                if (bluetoothState == BluetoothAdapter.STATE_ON) {
                    Log.e(BLESScanService.TAG, "Bluetooth turned ON")
                    is_bluetooth_on = true
                    stopAdvertising()
                    Handler().postDelayed({
                        startAdvertising()
                    }, 1000)

                } else if (bluetoothState == BluetoothAdapter.STATE_OFF) {
                    Log.e(BLESScanService.TAG, "Bluetooth turned OFF")
                    is_bluetooth_on = false
                    notificationHelper.updateNotification(
                        Utils.NOTIFICATION_TITLE,
                        Utils.BLE_MESSAGE
                    )
                    stopAdvertising()
                }
            }
        }
    }

    private fun setTimeout() {
        mHandler.postDelayed(timeoutRunnable, TIMEOUT)
    }

    /**
     * Starts BLE Advertising.
     */
    private fun startAdvertising() {
        Log.e(TAG, "Service: Starting Advertising")
        if (mAdvertiseCallback == null) {
            val settings = buildAdvertiseSettings()
            val data = buildAdvertiseData()
            mAdvertiseCallback = SampleAdvertiseCallback()
            if (mBluetoothLeAdvertiser != null) {
                mBluetoothLeAdvertiser!!.startAdvertising(
                    settings, data,
                    mAdvertiseCallback
                )
            }
        }
    }

    /**
     * Stops BLE Advertising.
     */
    private fun stopAdvertising() {
        Log.e(TAG, "Service: Stopping Advertising")
        if (mBluetoothLeAdvertiser != null) {
            mBluetoothLeAdvertiser!!.stopAdvertising(mAdvertiseCallback)
            mAdvertiseCallback = null
        }
    }

    /**
     * Returns an AdvertiseData object which includes the Service UUID and Device Name.
     */
    private fun buildAdvertiseData(): AdvertiseData {
        /**
         * Note: There is a strict limit of 31 Bytes on packets sent over BLE Advertisements.
         * This includes everything put into AdvertiseData including UUIDs, device info, &
         * arbitrary service or manufacturer data.
         * Attempting to send packets over this limit will result in a failure with error code
         * AdvertiseCallback.ADVERTISE_FAILED_DATA_TOO_LARGE. Catch this error in the
         * onStartFailure() method of an AdvertiseCallback implementation.
         */
        val dataBuilder = AdvertiseData.Builder()
        dataBuilder.addServiceUuid(Service_UUID)
        dataBuilder.setIncludeDeviceName(true)

        /* For example - this will cause advertising to fail (exceeds size limit) */
        //String failureData = "asdghkajsghalkxcjhfa;sghtalksjcfhalskfjhasldkjfhdskf";
        //dataBuilder.addServiceData(Constants.Service_UUID, failureData.getBytes());
        return dataBuilder.build()
    }

    /**
     * Returns an AdvertiseSettings object set to use low power (to help preserve battery life)
     * and disable the built-in timeout since this code uses its own timeout runnable.
     */
    private fun buildAdvertiseSettings(): AdvertiseSettings {
        val settingsBuilder = AdvertiseSettings.Builder()
        settingsBuilder.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_POWER)
        settingsBuilder.setTimeout(0)
        return settingsBuilder.build()
    }

    /**
     * Custom callback after Advertising succeeds or fails to start. Broadcasts the error code
     * in an Intent to be picked up by AdvertiserFragment and stops this Service.
     */
    private class SampleAdvertiseCallback : AdvertiseCallback() {
        override fun onStartFailure(errorCode: Int) {
            super.onStartFailure(errorCode)
            Log.e(TAG, "Advertising failed")
        }

        override fun onStartSuccess(settingsInEffect: AdvertiseSettings) {
            super.onStartSuccess(settingsInEffect)
            Log.e(TAG, "Advertising successfully started")
        }
    }

    fun createNotification() {
        if (isAtLeastAndroid8()) {
            val foregroundNotification = notificationHelper.getForegroundServiceNotification(
                Utils.NOTIFICATION_TITLE,
                Utils.NOTIFICATION_MESSAGE
            )
            startForeground(NotificationHelper.SERVICE_RUNNING_NOTIFICATION, foregroundNotification)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Log.e(TAG, "AdvertiserService onStartCommand")
        /** check for permission **/
        if (!Utils.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            !Utils.checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            return START_NOT_STICKY
        }
        createNotification()
        return Service.START_STICKY
    }

    private val timeoutRunnable = object : Runnable {
        override fun run() {
            Log.d(
                TAG,
                "AdvertiserService has reached timeout of $TIMEOUT milliseconds, stopping advertising."
            );
            mHandler.postDelayed(this, TIMEOUT)
        }
    }

    override fun onDestroy() {
        stopSelf()
        running = false;
        stopAdvertising();
        //unregisterReceiver(mBluetoothReceiver)
        mHandler.removeCallbacks(timeoutRunnable)
        stopForeground(true)
        super.onDestroy()
    }

    companion object {
        fun getIntent() = Intent(context, AdvertiserService::class.java)

        //val SERVICE_UUID = java.util.UUID.randomUUID()
        val Service_UUID = ParcelUuid.fromString("0000b81d-0000-1000-8000-00805f9b34fb")
        val TAG = AdvertiserService::class.java.simpleName
        val TIMEOUT = (20 * 1000).toLong() // 20 sec
    }
}