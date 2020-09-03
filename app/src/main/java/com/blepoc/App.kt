package com.blepoc

import android.app.Application
import android.content.Context
import android.util.Log
import com.blepoc.ble.AdvertiserService
import com.blepoc.ble.BLESScanService
import com.blepoc.database.BLEDatabase
import com.blepoc.repository.BLERepository
import com.blepoc.utility.Utils
import com.blepoc.utility.isAtLeastAndroid8
import com.facebook.stetho.Stetho
import com.polidea.rxandroidble2.LogConstants
import com.polidea.rxandroidble2.LogOptions
import com.polidea.rxandroidble2.RxBleClient

/**
 * Created by Manish Patel on 8/4/2020.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
        sInstance = this

        /** STETHO**/
        Stetho.initializeWithDefaults(this)

        /** Manual DI for now**/
        database = BLEDatabase.getInstance(applicationContext)
        bleRepository = BLERepository.getInstance(applicationContext)

        rxBleClient = RxBleClient.create(this)
        RxBleClient.updateLogOptions(
            LogOptions.Builder()
                .setLogLevel(LogConstants.INFO)
                .setMacAddressLogSetting(LogConstants.MAC_ADDRESS_FULL)
                .setUuidsLogSetting(LogConstants.UUIDS_FULL)
                .setShouldLogAttributeValues(true)
                .build()
        )
    }

    fun startAdvertiserService() {
        Log.e(TAG, "Inside AdvertiserService")
        if (Utils.serviceIsRunning(context, AdvertiserService::class.java.name)) {
            Log.e(TAG, "AdvertiserService is already running")
        } else {
            Log.e(TAG, "Restarting AdvertiserService")
            val intent = AdvertiserService.getIntent()
            if (isAtLeastAndroid8()) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
        }
    }

    fun stopAdvertiserService() {
        stopService(AdvertiserService.getIntent())
    }

    fun stopBLEService() {
        stopService(BLESScanService.getIntent())
    }

    fun startBLEService() {
        Log.e(TAG, "Inside startBLEService")
        if (Utils.serviceIsRunning(context, BLESScanService::class.java.name)) {
            Log.e(TAG, "BLE service is already running")
        } else {
            Log.e(TAG, "Restarting BLE service")
            val intent = BLESScanService.getIntent()
            if (isAtLeastAndroid8()) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
        }

    }

    companion object {
        lateinit var context: Context
        val TAG = App::class.java.simpleName

        @Volatile
        private lateinit var sInstance: App

        lateinit var database: BLEDatabase
            private set

        lateinit var bleRepository: BLERepository
            private set

        fun getInstance(): App {
            if (sInstance == null) {
                synchronized(App::class.java) {
                    if (sInstance == null) {
                        sInstance = App()
                    }
                }
            }
            return sInstance
        }

        lateinit var rxBleClient: RxBleClient
            private set

    }

}