package com.blepoc.repository

import android.content.Context
import com.blepoc.App
import com.blepoc.database.BLEEntry
import com.blepoc.database.BLEStore
import com.blepoc.utility.SingletonHolder

/**
 * Created by Manish Patel on 8/6/2020.
 */
class BLERepository private constructor(context: Context) {

    companion object : SingletonHolder<BLERepository, Context>(::BLERepository)

    private var bleStore: BLEStore

    init {
        val database = App.database
        bleStore = database.bleDao()
    }

    suspend fun getDevice(mac: String) = bleStore.getDevice(mac)

    suspend fun updateAlertFlag(mac: String) = bleStore.updateAlertFlag(mac)

    suspend fun clearLogs() = bleStore.clearLogs()

    suspend fun insertDevice(bleEntry: BLEEntry) = bleStore.insert(bleEntry)
}