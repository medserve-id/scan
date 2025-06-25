class BluetoothService(private val context: Context) {

    fun printText(text: String) {
        val adapter = BluetoothAdapter.getDefaultAdapter()
        val pairedDevices = adapter?.bondedDevices ?: return

        val printer = pairedDevices.firstOrNull()  // Ambil printer pertama
        val socket = printer
            ?.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"))

        socket?.connect()
        val output = socket?.outputStream

        // ESC/POS format
        output?.write(text.toByteArray())
        output?.write(byteArrayOf(0x0A)) // Line feed

        output?.flush()
        socket?.close()
    }
}

