package com.example.bolkar

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bolkar.ui.theme.BOLKARTheme
import com.faruk_tutkun.bolkar.ui.theme.Button_v2
import com.faruk_tutkun.bolkar.ui.theme.RadioGroup

class ManualActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BOLKARTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.primaryContainer
                )
                {
                    val kinds = listOf("Game Mode", "Other Modes")
                    val (selected, setSelected) = remember { mutableStateOf("") }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(all = 4.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    )
                    {
                        Text(
                            text = "Chose an Operation Mode",
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )

                        Column {
                            RadioGroup(
                                mItems = kinds,
                                selected, setSelected
                            )
                        }
                        Button_v2(text = "Mode Settings", func = {
                            val intent = Intent(
                                this@ManualActivity,
                                OperationModeActivity::class.java
                            )
                            Log.i("TAG", selected)
                            intent.putExtra("operation" ,  selected)
                            startActivity(intent)
                        }, height = 90, width = 180, fontSize = 20)

                        Button_v2(text = "START", func = {
                            if( selected.equals("Game Mode"))
                            {
                                BluetoothArduino.mThread?.write( byteArrayOf(GameModeMsgId, 2, ballConfArray[30], ballConfArray[31]))
                            }
                            else
                            {
                                BluetoothArduino.mThread?.write( byteArrayOf(OtherModesMsgId, ballConfArray.size.toByte()) + ballConfArray)
                            }
                        }, height = 90, width = 180, fontSize = 20)

                        Button_v2(text = "STOP", func = {
                            val byteArray = ByteArray(3)
                            byteArray[0]  = StopMsgId
                            byteArray[1]  = 1
                            byteArray[2]  = 0
                            BluetoothArduino.mThread?.write(byteArray)
                        }, height = 90, width = 180, fontSize = 20)
                    }

                }
            }
        }
    }
}
