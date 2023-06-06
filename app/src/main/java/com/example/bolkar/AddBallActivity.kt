package com.example.bolkar

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.bolkar.ui.theme.BOLKARTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.faruk_tutkun.bolkar.ui.theme.Button_v2
import com.faruk_tutkun.bolkar.ui.theme.toolbar_v1


val BallActivityReturnCode = 59;

class AddBallActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BOLKARTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.primaryContainer
                )
                {
                    toolbar_v1(hasBackArrow = true, hasMenu = false, func = { finish() })
                    var speed by remember { mutableStateOf(0f) }
                    var spin by remember { mutableStateOf(0f) }
                    var angle by remember { mutableStateOf(40f) }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(all = 4.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    )
                    {
                        Text(
                            text = "Motor 1: ",
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.width(width = 20.dp))

                        Text(text = speed.toInt().toString())
                        Slider(
                            value = speed,
                            onValueChange = { speed = it },
                            valueRange = 0f..10f,
                            steps = 9
                        )

                        Spacer(modifier = Modifier.width(width = 20.dp))
                        Text(
                            text = "Motor 2: ",
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.width(width = 20.dp))

                        Text(text = spin.toInt().toString())
                        Slider(
                            value = spin,
                            onValueChange = { spin = it },
                            valueRange = 0f..10f,
                            steps = 9
                        )

                        Spacer(modifier = Modifier.width(width = 20.dp))
                        Text(
                            text = "Angle: ",
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.width(width = 20.dp))

                        Text(text = angle.toInt().toString())
                        Slider(
                            value = angle,
                            onValueChange = { angle = it },
                            valueRange = 0f..90f,
                            steps = 89
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 4.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Button_v2(text = "Randomize", func = {
                            speed = 0f;
                            spin  = 0f;
                            angle = 0f;
                        }, height = 90, width = 180, fontSize = 20)
                        Button_v2(text = "Save and Exit", func = {
                            val intent = Intent()
                            intent.putExtra("SpeedResult", speed.toInt())
                            intent.putExtra("SpinResult", spin.toInt())
                            intent.putExtra("AngleResult", angle.toInt())
                            setResult(RESULT_OK, intent)
                            finish()
                        }, height = 90, width = 180, fontSize = 20)
                    }
                }
            }
        }
    }
}