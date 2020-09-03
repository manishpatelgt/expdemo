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

    private val data = mutableListOf<ScanResult>()

    fun addScanResult(bleScanResult: ScanResult) {
        // Not the best way to ensure distinct devices, just for the sake of the demo.
        data.withIndex()
            .firstOrNull { it.value.bleDevice == bleScanResult.bleDevice }
            ?.let {
                // device already in data list => update
                data[it.index] = bleScanResult
                notifyItemChanged(it.index)
            }
            ?: run {

                val rxBleDevice = bleScanResult.bleDevice
                val mac = rxBleDevice.macAddress
                val name = rxBleDevice.name

                // new device => add to data list
                with(data) {
                    if (!name.isNullOrEmpty()) {
                        Log.e(TAG, "mac: $mac")
                        Log.e(TAG, "name: $name")
                        add(bleScanResult)
                        val idt = Utils.getCurrentDateTimeString()
                        val timeStamp = Utils.getCurrentTimeStamp()
                        val bleEntry = BLEEntry(
                            mac = mac.replace(":", ""),
                            name = name,
                            insertDateTime = idt,
                            timeStamp = timeStamp
                        )
                        ioScope.launch { bleRepository.insertDevice(bleEntry) }
                        notifyDataSetChanged()
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

            val mac = bleDevice.macAddress
            val name = bleDevice.name

            holder.text_name.text = String.format("%s (%s)", mac, name)
            holder.itemView.setOnClickListener { onClickListener(this) }

            ioScope.launch {
                val bleEntry = bleRepository.getDevice(mac.replace(":", ""))
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