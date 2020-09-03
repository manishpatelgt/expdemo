package com.blepoc.ble

import android.Manifest
import android.app.PendingIntent
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
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
import com.blepoc.App
import com.blepoc.App.Companion.context
import com.blepoc.database.BLEEntry
import com.blepoc.receivers.NotificationDismissReceiver
import com.blepoc.repository.BLERepository
import com.blepoc.utility.Utils
import com.blepoc.utility.bluetoothManager
import com.blepoc.utility.isAtLeastAndroid8
import com.blepoc.utility.notifications.NotificationHelper
import com.polidea.rxandroidble2.RxBleClient
import com.polidea.rxandroidble2.scan.ScanFilter
import com.polidea.rxandroidble2.scan.ScanResult
import com.polidea.rxandroidble2.scan.ScanSettings
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by Manish Patel on 8/4/2020.
 */
class BLESScanService : Service() {

    private lateinit var rxBleClient: RxBleClient
    private var scanDisposable: Disposable? = null
    private lateinit var notificationHelper: NotificationHelper
    private val scanHandler = Handler()
    private val removeHandler = Handler()
    private var is_bluetooth_on = true
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var bleRepository: BLERepository

    var running = false
    private var mBluetoothLeAdvertiser: BluetoothLeAdvertiser? = null
    private var mAdvertiseCallback: AdvertiseCallback? = null
    private val mHandler = Handler()

    private val ioScope = CoroutineScope(Dispatchers.IO)

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.e(TAG, "BLEService onCreate")
        bleRepository = App.bleRepository
        notificationHelper = NotificationHelper(this)
        createNotification()
        rxBleClient = RxBleClient.create(this)

        running = true
        initialize()
        startAdvertising()

        val filter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        this.registerReceiver(mBluetoothReceiver, filter)
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        //val filter2 = IntentFilter(BluetoothDevice.ACTION_FOUND)
        //registerReceiver(receiver, filter2)

        scanHandler.removeCallbacks(scanBLERunnable)
        scanHandler.post(scanBLERunnable)
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

    fun createNotification() {
        if (isAtLeastAndroid8()) {
            val foregroundNotification = notificationHelper.getForegroundServiceNotification(
                Utils.NOTIFICATION_TITLE,
                Utils.NOTIFICATION_MESSAGE
            )
            startForeground(NotificationHelper.SERVICE_RUNNING_NOTIFICATION, foregroundNotification)
        }
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


    private val timeoutRunnable = object : Runnable {
        override fun run() {
            Log.d(
                TAG,
                "AdvertiserService has reached timeout of ${TIMEOUT} milliseconds, stopping advertising."
            );
            mHandler.postDelayed(this, TIMEOUT)
        }
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

                    /** restart scanning **/
                    scanHandler.removeCallbacks(scanBLERunnable)
                    scanHandler.post(scanBLERunnable)

                    /** restart advertising **/
                    stopAdvertising()
                    Handler().postDelayed({
                        startAdvertising()
                    }, 1000)


                } else if (bluetoothState == BluetoothAdapter.STATE_OFF) {
                    Log.e(TAG, "Bluetooth turned OFF")
                    is_bluetooth_on = false

                    notificationHelper.updateNotification(
                        Utils.NOTIFICATION_TITLE,
                        Utils.BLE_MESSAGE
                    )

                    /** stop scanning **/
                    stopScanning()

                    /** stop advertising **/
                    stopAdvertising()

                    /** clear all the records **/
                    ioScope.launch {
                        bleRepository.clearLogs()
                    }
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

        val Name = rxBleDevice.name
        Log.e(TAG, "Name: $Name")

        /** check for device is exist or not **/
        ioScope.launch {
            val bleEntry = bleRepository.getDevice(mac.replace(":", ""))
            if (bleEntry != null) {
                Log.e(TAG, "Device found")

                val olderTimeStamp = bleEntry.timeStamp
                val currentTimeStamp = Utils.getCurrentTimeStamp()

                val totalMinutes = Utils.getDifferenceInMinutes(currentTimeStamp, olderTimeStamp)
                Log.e(TAG, "Total Minutes: $totalMinutes")
                if (totalMinutes >= Utils.ALERT_MINUTES) {
                    if (!bleEntry.isAlert) {
                        val title = "Social Distancing Alert"
                        val message = "${bleEntry.name} is near to you since last 15 min"
                        notificationHelper.showAlertNotification(title, message)
                        bleRepository.updateAlertFlag(mac.replace(":", ""))
                    }
                }

            } else {
                Log.e(TAG, "Device not found")
                if (!Name.isNullOrEmpty()) {
                    val idt = Utils.getCurrentDateTimeString()
                    val timeStamp = Utils.getCurrentTimeStamp()
                    val bleEntry = BLEEntry(
                        mac = mac.replace(":", ""),
                        name = Name,
                        insertDateTime = idt,
                        timeStamp = timeStamp
                    )
                    bleRepository.insertDevice(bleEntry)
                }
            }
        }

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

        running = false
        stopAdvertising()
        mHandler.removeCallbacks(timeoutRunnable)

        //unregisterReceiver(receiver)
    }

    companion object {
        fun getIntent() = Intent(context, BLESScanService::class.java)
        val TAG = BLESScanService::class.java.simpleName
        val Service_UUID = ParcelUuid.fromString("0000b81d-0000-1000-8000-00805f9b34fb")
        val TIMEOUT = (20 * 1000).toLong() // 20 sec
    }
}