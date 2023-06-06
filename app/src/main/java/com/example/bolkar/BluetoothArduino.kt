package com.example.bolkar

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.UUID


val TAG = "MY_APP_DEBUG_TAG"
val SERVICE_ID = "00001101-0000-1000-8000-00805F9B34FB" //SPP UUID
const val CONNNECTION_OK = 1
const val NO_CONNNECTION = 0
const val BTDevice = "HC-05"
const val PERMISSION_ERROR = 17
const val HC_05_NOT_BOUNDED = 18
const val ENABLE_ERROR = 19
const val SOCKET_PROBLEM = 20;

val TestModeMsgID: Byte = 5;

val StopMsgId: Byte = 60;
val OtherModesMsgId: Byte = 61;
val GameModeMsgId: Byte = 62;
val ArduinoMsgId: Byte = 63;

var tryingConnection =  false;

var basariliKarsilama = 0
var toplamAtilanTop = 0
object BluetoothArduino {
    private var btAdapter: BluetoothAdapter? = null;
    private var btDevice: BluetoothDevice? = null;
    var btSocket: BluetoothSocket? = null;
    var mThread: ConnectedThread? = null;

    fun is_enabled(): Boolean {
        btAdapter = BluetoothAdapter.getDefaultAdapter()
        if (btAdapter == null)
            return false

        return btAdapter!!.isEnabled()
    }

    fun connect(context: Context): Int {
        tryingConnection = true;
        //check Permissions
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return PERMISSION_ERROR
        }
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return PERMISSION_ERROR
        }
        if (is_enabled() == false) {
            return ENABLE_ERROR
        }
        //get adapter
        btAdapter = BluetoothAdapter.getDefaultAdapter()
        //get paired devices
        var m_pairedDevices = btAdapter!!.bondedDevices

        //chose HC-05 from paired Devices
        if (!m_pairedDevices.isEmpty()) {
            var deviceFound = false
            for (device: BluetoothDevice in m_pairedDevices) {
                if (device.name == BTDevice) {
                    btDevice = device;
                    deviceFound = true
                }
            }
            if (deviceFound == false)
                return HC_05_NOT_BOUNDED
        } else {
            return HC_05_NOT_BOUNDED
        }
        //close socket if already opened
        if(btSocket != null)
        {
            try {
                btSocket?.close()
            } catch (e: Exception) {
                return SOCKET_PROBLEM
            }
        }
        btSocket = btDevice?.createRfcommSocketToServiceRecord(UUID.fromString(SERVICE_ID))
        if (btSocket == null)
            return NO_CONNNECTION
        //connect socket
        try {
            btSocket?.connect()
        } catch (e: Exception) {
            return SOCKET_PROBLEM
        }

        //connect a thread
        mThread = ConnectedThread(btSocket as BluetoothSocket)
        mThread!!.start()
        tryingConnection = false
        return CONNNECTION_OK
    }
    class ConnectedThread(private val mmSocket: BluetoothSocket) : Thread() {

        private val mmInStream: InputStream = mmSocket.inputStream
        private val mmOutStream: OutputStream = mmSocket.outputStream
        private val mmBuffer: ByteArray = ByteArray(1024) // mmBuffer store for the stream

        override fun run() {
            var numBytes: Int //bytes returned from read()

            // Keep listening to the InputStream until an exception occurs.
            while (tryingConnection == false) {
                //Log.i("ARDUINO", "I am fucking running\n");
                // Read from the InputStream.
                numBytes = try {
                    mmInStream.read(mmBuffer)
                } catch (e: IOException) {
                    Log.d(TAG, "Input stream was disconnected", e)
                    connectionMade = false;
                    break
                }
                if (mmBuffer[0] == ArduinoMsgId && numBytes == 3)
                {
                    basariliKarsilama = mmBuffer[1].toInt();
                    toplamAtilanTop   = mmBuffer[2].toInt();
                }
            }
        }

        fun write(bytes: ByteArray) {
            try {
                mmOutStream.write(bytes)
            } catch (e: IOException) {
                Log.e(TAG, "Error occurred when sending data", e)
            }
        }

        // Call this method from the main activity to shut down the connection.
        fun cancel() {
            try {
                mmSocket.close()
            } catch (e: IOException) {
                Log.e(TAG, "Could not close the connect socket", e)
            }
        }
    }
}