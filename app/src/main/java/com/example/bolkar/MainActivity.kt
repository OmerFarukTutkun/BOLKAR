package com.example.bolkar

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.example.bolkar.ui.theme.BOLKARTheme
import com.faruk_tutkun.bolkar.ui.theme.Button_v1
import com.faruk_tutkun.bolkar.ui.theme.menuItem
import com.faruk_tutkun.bolkar.ui.theme.toolbar_v1

var connectionMade = false;

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BOLKARTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    var expanded by remember { mutableStateOf(false) }

                    Button_v1(
                        text = "Voice Control",
                        func = this::gotoVoiceActivity,
                        height = 90,
                        width = 180,
                        x = 0,
                        y = 300,
                        fontSize = 20
                    )
                    Button_v1(
                        text = "Manual Control",
                        func = this::gotoManualControlActivity,
                        height = 90,
                        width = 180,
                        x = 0,
                        y = 400,
                        fontSize = 20
                    )
                    Button_v1(
                        text = "Live Result",
                        func = this::gotoLiveResultActivity,
                        height = 90,
                        width = 180,
                        x = 0,
                        y = 500,
                        fontSize = 20
                    )

                    var item1 = menuItem(text = "About", onClick = this::gotoAboutActivity)
                    var item2 = menuItem(text = "Connect to Robot", onClick = this::connectToRobot)
                    var item3 = menuItem(
                        text = "Bluetooth Settings",
                        onClick = { startActivity(Intent(Settings.ACTION_BLUETOOTH_SETTINGS)) })
                    toolbar_v1(hasBackArrow = false, items = listOf(item1, item2, item3))
                }
            }
        }

        handlepermissions()
    }

    fun handlepermissions() {
        val permissionsResultCallback = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            when (it) {
                true -> {
                    println("Permission has been granted by user")
                }

                false -> {
                    Toast.makeText(this@MainActivity, "Permission denied", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        //TODO: bunu Manifest dosyasından çekerek her permission için generic olarak yap
        val permissions = listOf(
            android.Manifest.permission.BLUETOOTH_CONNECT,
            android.Manifest.permission.BLUETOOTH
        )

        for (p in permissions) {
            val permission = ContextCompat.checkSelfPermission(
                this@MainActivity, p
            )
            if (permission != PackageManager.PERMISSION_GRANTED) {
                permissionsResultCallback.launch(p)
            } else {
                println("Permission is Granted")
            }
        }
    }

    fun gotoVoiceActivity() {
        val intent = Intent(this, VoiceActivity::class.java)
        if ( true ) { //connectionMade) {
            startActivity(intent)
        } else {
            Toast.makeText(this@MainActivity, "Please First connect a robot", Toast.LENGTH_SHORT)
                .show()
        }
    }
    fun gotoAboutActivity() {
        val intent = Intent(this, AboutActivity::class.java)
        startActivity(intent)
    }
    fun gotoManualControlActivity() {
        val intent = Intent(this, ManualActivity::class.java)
        if (connectionMade) {
            startActivity(intent)
        }
        else
        {
            Toast.makeText(this@MainActivity, "Please First connect a robot", Toast.LENGTH_SHORT)
                .show()
        }
    }
    fun gotoLiveResultActivity() {
        val intent = Intent(this, LiveResultActivity::class.java)
        startActivity(intent)
    }
    fun connectToRobot() {

        val result = BluetoothArduino.connect(this@MainActivity)
        if (result == CONNNECTION_OK) {
            connectionMade = true
            Toast.makeText(this@MainActivity, "Connected to Robot", Toast.LENGTH_LONG)
                .show()
        } else if (result == ENABLE_ERROR) {
            Toast.makeText(this@MainActivity, "Bluetooth is off", Toast.LENGTH_LONG)
                .show()
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, 3)
        } else if (result == HC_05_NOT_BOUNDED) {

            Toast.makeText(
                this@MainActivity,
                "Please pair your device with HC-05",
                Toast.LENGTH_LONG
            )
                .show()
        } else if (result == SOCKET_PROBLEM) {
            Toast.makeText(
                this@MainActivity,
                "Please open your robot",
                Toast.LENGTH_LONG
            )
                .show()
        } else {
            Toast.makeText(
                this@MainActivity,
                "Could not connect to robot",
                Toast.LENGTH_LONG
            )
                .show()
        }
    }
}
