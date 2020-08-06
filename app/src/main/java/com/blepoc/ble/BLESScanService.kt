package com.blepoc.ble

import android.Manifest
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.os.IBinder
import android.util.Log
import com.blepoc.App.Companion.context
import com.blepoc.utility.Utils
import com.blepoc.utility.isAtLeastAndroid8
import com.blepoc.utility.notifications.NotificationHelper
import com.polidea.rxandroidble2.RxBleClient
import com.polidea.rxandroidble2.scan.ScanFilter
import com.polidea.rxandroidble2.scan.ScanResult
import com.polidea.rxandroidble2.scan.ScanSettings
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

/**
 * Created by Manish Patel on 8/4/2020.
 */
class BLESScanService : Service() {

    private lateinit var rxBleClient: RxBleClient
    private var scanDisposable: Disposable? = null
    private lateinit var notificationHelper: NotificationHelper
    private val scanHandler = Handler()
    private var is_bluetooth_on = true
    private lateinit var bluetoothAdapter: BluetoothAdapter

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.e(TAG, "BLEService onCreate")
        notificationHelper = NotificationHelper(this)
        createNotification()
        rxBleClient = RxBleClient.create(this)

        val filter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        this.registerReceiver(mBluetoothReceiver, filter)
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        //val filter2 = IntentFilter(BluetoothDevice.ACTION_FOUND)
        //registerReceiver(receiver, filter2)

        scanHandler.removeCallbacks(scanBLERunnable)
        scanHandler.post(scanBLERunnable)
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
        Log.e(TAG, "BLEService onStartCommand")
        /** check for permission **/
        if (!Utils.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            !Utils.checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            return START_NOT_STICKY
        }
        createNotification()
        scanBleDevices()
        return Service.START_STICKY
    }


    fun isScanning(): Boolean {
        return scanDisposable != null
    }

    fun stopScanning() {
        if (isScanning()) {
            scanDisposable?.dispose()
            scanDisposable = null
            bluetoothAdapter.cancelDiscovery()
        }
    }

    private val mBluetoothReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (BluetoothAdapter.ACTION_STATE_CHANGED == intent.action) {
                val bluetoothState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)
                Log.e(TAG, "BLE State: $bluetoothState")
                if (bluetoothState == BluetoothAdapter.STATE_ON) {
                    Log.e(TAG, "Bluetooth turned ON")
                    is_bluetooth_on = true
                    scanHandler.removeCallbacks(scanBLERunnable)
                    scanHandler.post(scanBLERunnable)
                } else if (bluetoothState == BluetoothAdapter.STATE_OFF) {
                    Log.e(TAG, "Bluetooth turned OFF")
                    is_bluetooth_on = false
                    notificationHelper.updateNotification(
                        Utils.NOTIFICATION_TITLE,
                        Utils.BLE_MESSAGE
                    )
                    //stop scanning
                    stopScanning()
                }
            }
        }
    }

    fun scanBleDevices() {
        notificationHelper.updateNotification(
            Utils.NOTIFICATION_TITLE,
            "Searching device near to you"
        )

        scanDisposable = rxBleClient.scanBleDevices(
            ScanSettings.Builder()
                .setShouldCheckLocationServicesState(true)
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                .build(),
            ScanFilter.Builder() //.setDeviceAddress("FF:FF:C3:15:47:0A") //Set my BLE device mac
                // add custom filters if needed
                .build()
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { bleScanResult: ScanResult ->
                    onScanResult(
                        bleScanResult
                    )
                }
            ) { throwable: Throwable ->
                onScanFailure(
                    throwable
                )
            }

        //startDiscovering()
    }

    private fun enableDiscoverable() {
        val discoverableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0)
        startActivity(discoverableIntent)
    }

    private fun startDiscovering() {
        val discoveryStarted = bluetoothAdapter.startDiscovery()
        if (discoveryStarted) {
            Log.i(this.javaClass.name, "Discovery started")
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (BluetoothDevice.ACTION_FOUND == action) {
                val device =
                    intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                val type = device.bluetoothClass
                if (type.deviceClass == BluetoothClass.Device.PHONE_SMART) {
                    val deviceName = device.name
                    val deviceHardwareAddress = device.address // MAC address
                    val rssi = intent.getShortExtra(
                        BluetoothDevice.EXTRA_RSSI,
                        Short.MIN_VALUE
                    ).toInt()
                    val timestamp = System.currentTimeMillis()
                    Log.e(TAG, "Device Name: $deviceName")
                    Log.e(TAG, "Mac: $deviceHardwareAddress")
                }
            }
        }
    }

    fun onScanResult(bleScanResult: ScanResult) {
        val rxBleDevice = bleScanResult.bleDevice
        val mac = rxBleDevice.macAddress
        Log.e(TAG, "Mac: $mac")

        val name = rxBleDevice.name
        Log.e(TAG, "Name: $name")

        /*val type = rxBleDevice.bluetoothDevice.type
        Log.e(TAG, "Type: $type")*/
    }

    private fun onScanFailure(throwable: Throwable) {
        Log.e(TAG, "BLE Scan error in background: $throwable")
    }

    var scanBLERunnable: Runnable = object : Runnable {
        override fun run() {
            //Check bluetooth on/off
            if (is_bluetooth_on) {
                //check for scanning is already running or not
                if (!isScanning()) {
                    scanBleDevices()
                }
            }
            scanHandler.postDelayed(this, Utils.SCAN_CHECK_INTERVAL_TIME)
        }
    }

    override fun onDestroy() {
        stopScanning()
        stopSelf()
        //stop scanner check runnable
        scanHandler.removeCallbacks(scanBLERunnable)
        unregisterReceiver(mBluetoothReceiver)

        //unregisterReceiver(receiver)
    }

    companion object {
        fun getIntent() = Intent(context, BLESScanService::class.java)
        val TAG = BLESScanService::class.java.simpleName
    }
}