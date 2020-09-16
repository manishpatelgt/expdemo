package com.blepoc.activities.poc2

/**
 * Created by Manish Patel on 9/3/2020.
 */

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blepoc.App
import com.blepoc.R
import com.blepoc.ble.AdvertiserService
import com.blepoc.ble.BLESScanService
import com.blepoc.database.BLEEntry
import com.blepoc.repository.BLERepository
import com.blepoc.utility.Utils
import com.polidea.rxandroidble2.scan.ScanResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class ScanResultsAdapter(
    private val onClickListener: (ScanResult) -> Unit
) : RecyclerView.Adapter<ScanResultsAdapter.ViewHolder>() {

    private val ioScope = CoroutineScope(Dispatchers.IO)

    private var bleRepository = App.bleRepository

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text_name: TextView = itemView.findViewById(R.id.text_name)
        val text_rssi: TextView = itemView.findViewById(R.id.text_rssi)
    }

    val data = mutableListOf<ScanResult>()

    fun removeAt(index: Int) {
        data.removeAt(index)
        notifyItemRemoved(index)
    }

    fun addScanResult(bleScanResult: ScanResult) {
        // Not the best way to ensure distinct devices, just for the sake of the demo.
        data.withIndex()
            .firstOrNull { it.value.bleDevice == bleScanResult.bleDevice }
            ?.let {
                // device already in data list => update
                data[it.index] = bleScanResult
                val result = bleScanResult.scanRecord.getServiceData(AdvertiserService.Service_UUID)
                val from = result?.toString(Charsets.UTF_8).toString()
                val deviceId = from.replace("_EF", "")

                ioScope.launch {
                    val bleEntry = bleRepository.getDevice(deviceId)
                    if (bleEntry != null) {
                        bleRepository.updateLastVisibleTimestamp(
                            Utils.getCurrentTimeStamp(),
                            deviceId
                        )
                    }
                }

                notifyItemChanged(it.index)
            }
            ?: run {

                val result = bleScanResult.scanRecord.getServiceData(AdvertiserService.Service_UUID)
                val from = result?.toString(Charsets.UTF_8).toString()
                Log.e(TAG, "From: $from")

                val rxBleDevice = bleScanResult.bleDevice
                val mac = rxBleDevice.macAddress
                val name = rxBleDevice.name

                // new device => add to data list
                with(data) {
                    val isValid = from.contains("EF")
                    if (isValid) {
                        val deviceId = from.replace("_EF", "")
                        Log.e(TAG, "mac: $mac")
                        Log.e(TAG, "name: $name")
                        Log.e(TAG, "deviceId: $deviceId")
                        add(bleScanResult)
                        val idt = Utils.getCurrentDateTimeString()
                        val timeStamp = Utils.getCurrentTimeStamp()
                        val bleEntry = BLEEntry(
                            mac = mac.replace(":", ""),
                            deviceId = deviceId,
                            name = name,
                            insertDateTime = idt,
                            timeStamp = timeStamp,
                            lastVisibleTimeStamp = timeStamp
                        )
                        ioScope.launch { bleRepository.insertDevice(bleEntry) }
                        if (data.size == 0) {
                            notifyDataSetChanged()
                        } else {
                            notifyItemInserted(data.size - 1)
                        }
                    }
                }
            }

    }

    fun clearScanResults() {
        data.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(data[position]) {

            val result = scanRecord.getServiceData(AdvertiserService.Service_UUID)
            val from = result?.toString(Charsets.UTF_8).toString()
            val deviceId = from.replace("_EF", "")
            Log.e(TAG, "deviceId: $deviceId")

            val mac = bleDevice.macAddress
            val name = bleDevice.name

            holder.text_name.text = String.format("%s (%s)", mac, deviceId)
            holder.itemView.setOnClickListener { onClickListener(this) }

            ioScope.launch {
                val bleEntry = bleRepository.getDevice(deviceId)
                if (bleEntry != null) {
                    Log.e(TAG, "mac: $mac")
                    Log.e(TAG, "name: $name")
                    Log.e(TAG, "Device found")
                    val olderTimeStamp = bleEntry.timeStamp
                    val currentTimeStamp = Utils.getCurrentTimeStamp()

                    val totalTime =
                        Utils.getHumanTimeDifference(olderTimeStamp, currentTimeStamp)
                    Log.e(TAG, "Visible since last: $totalTime")

                    withContext(Dispatchers.Main) {
                        holder.text_rssi.text =
                            String.format("RSSI: %d, visible since last: %s", rssi, totalTime)
                    }

                } else {
                    withContext(Dispatchers.Main) {
                        holder.text_rssi.text = String.format("RSSI: %d", rssi)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.ble_list_item, parent, false)
            .let { ViewHolder(it) }

    companion object {
        val TAG = ScanResultsAdapter::class.java.simpleName
    }
}