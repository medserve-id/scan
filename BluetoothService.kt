package com.example.qrcetak

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.SharedPreferences
import java.io.OutputStream
import java.util.*

class BluetoothService(private val context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("printer_prefs", Context.MODE_PRIVATE)
    private val adapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

    fun printText(text: String) {
        val savedMac = prefs.getString("printer_mac", null)
        val device: BluetoothDevice? = if (savedMac != null) {
            adapter?.getRemoteDevice(savedMac)
        } else {
            val paired = adapter?.bondedDevices?.firstOrNull()
            paired?.also {
                prefs.edit().putString("printer_mac", it.address).apply()
            }
        }

        val socket = device?.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"))
        socket?.connect()
        val output: OutputStream? = socket?.outputStream

        output?.write(text.toByteArray(Charsets.UTF_8))
        output?.write(byteArrayOf(0x0A, 0x0A, 0x0A)) // 3 baris kosong
        output?.flush()
        socket?.close()
    }
}
