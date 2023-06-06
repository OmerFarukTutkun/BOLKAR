package com.example.bolkar

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bolkar.ui.theme.BOLKARTheme
import com.faruk_tutkun.bolkar.ui.theme.toolbar_v1
import java.lang.Integer.min
import java.util.ArrayList
import java.util.Locale

val REQUEST_CODE_SPEECH_INPUT = 1;
val VOICE_RECOGNITION_MSG_ID: Byte = 1;

var operationMode = 0;
var numTopVoice = 0;
var toplarVoice = IntArray(33)

var frekansVoice = 20
var toplamTopSayisi = 30;

var speedVoice = 0;
var spinVoice = 0;
var angleVoice = 0;

var ballConfArrayVoice = ByteArray(33)

fun writeBallInfo(index: Int): Int {
    var indexSpeed = (toplarVoice[3*index] + 1) + (toplarVoice[3*index + 1] +2)*5;
    if(toplarVoice[3*index] == 5)
    {
        ballConfArrayVoice[3*index ] = 0
        ballConfArrayVoice[3*index + 1] = 0
        ballConfArrayVoice[3*index + 2] = 0
        return 0;
    }
    when (indexSpeed) {
        0 -> {
            ballConfArrayVoice[3*index] = 4;
            ballConfArrayVoice[3*index + 1] = 6;
        }
        1 -> {
            ballConfArrayVoice[3*index] = 4;
            ballConfArrayVoice[3*index + 1] = 8;
        }
        2 -> {
            ballConfArrayVoice[3*index] = 4;
            ballConfArrayVoice[3*index + 1] = 10;
        }
        3 -> {
            ballConfArrayVoice[3*index] = 5;
            ballConfArrayVoice[3*index + 1] = 6;
        }
        4 -> {
            ballConfArrayVoice[3*index] = 5;
            ballConfArrayVoice[3*index + 1] = 8;
        }
        5 -> {
            ballConfArrayVoice[3*index] = 5;
            ballConfArrayVoice[3*index + 1] = 10;
        }
        6 -> {
            ballConfArrayVoice[3*index] = 4;
            ballConfArrayVoice[3*index + 1] = 4;
        }
        7 -> {
            ballConfArrayVoice[3*index] = 6;
            ballConfArrayVoice[3*index + 1] = 6;
        }
        8 -> {
            ballConfArrayVoice[3*index] = 8;
            ballConfArrayVoice[3*index + 1] = 8;
        }
        9 -> {
            ballConfArrayVoice[3*index] = 6;
            ballConfArrayVoice[3*index + 1] = 3;
        }
        10 -> {
            ballConfArrayVoice[3*index] = 7;
            ballConfArrayVoice[3*index + 1] = 3;
        }
        11 -> {
            ballConfArrayVoice[3*index] = 9;
            ballConfArrayVoice[3*index + 1] = 3;
        }
        12 -> {
            ballConfArrayVoice[3*index] = 7;
            ballConfArrayVoice[3*index + 1] = 3;
        }
        13 -> {
            ballConfArrayVoice[3*index] = 9;
            ballConfArrayVoice[3*index + 1] = 43;
        }
        14 -> {
            ballConfArrayVoice[3*index] = 10;
            ballConfArrayVoice[3*index + 1] = 4;
        }
    }
    var angleindex = toplarVoice[3*index + 2] + 1;
    when(angleindex)
    {
        0 -> {
            ballConfArrayVoice[3*index + 2] = 40;
        }
        1 -> {
            ballConfArrayVoice[3*index + 2] = 60;
        }
        2 -> {
            ballConfArrayVoice[3*index + 2] = 85;
        }
    }
    return 0;
}
fun returnTextforoperationMode(): String {
    when (operationMode) {
        4 -> {
            return "Game Mode";
        }
        3-> {
            return "Random Sequence Practicing";
        }
        2 -> {
            return "Sequence Practicing";
        }
        1 -> {
            return "Random Repetition";
        }
        0 -> {
            return "Repetition";
        }
    }
    return ""
}
fun returnTextforBall(speed: Int, spin: Int, angle: Int): Array<String> {
    val result = arrayOf<String>("", "", "")
    when (speed) {
        5 -> {
            result[0] = "random";
        }
        1 -> {
            result[0] = "high";
        }
        0-> {
            result[0] = "medium";
        }
        -1 -> {
            result[0] = "low";
        }
    }
    when (spin) {
        5 -> {
            result[0] = "random";
        }
        2 -> {
            result[1] = "heavy +";
        }
        1-> {
            result[1] = "light +";
        }
        0 -> {
            result[1] = "no ";
        }
        -2 -> {
            result[1] = "heavy -";
        }
        -1-> {
            result[1] = "light -";
        }
    }
    when (angle) {
        5-> {
            result[2] = "random";
        }
        1-> {
            result[2] = "right";
        }
        0 -> {
            result[2] = "middle";
        }
        -1-> {
            result[2] = "left";
        }
    }
    return result

}
@Composable
fun MyVoiceTable() {

    val column1Weight = .25f // 25%
    val column2Weight = .25f // 25%
    val column3Weight = .25f // 25%
    val column4Weight = .25f // 25%

    // The LazyColumn will be our table. Notice the use of the weights below
    LazyColumn(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Here is the header
        item {
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                TableCell(text = "Ball", weight = column1Weight)
                TableCell(text = "Speed", weight = column2Weight)
                TableCell(text = "Spin", weight = column3Weight)
                TableCell(text = "Angle", weight = column4Weight)
            }
            val numberOfBall = toplarVoice[32].toInt()
            for (i in 0 until numberOfBall) {   // i in 1 until 10, excluding 10
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    val result = returnTextforBall( toplarVoice[3 * i], toplarVoice[3 * i + 1], toplarVoice[3 * i + 2])
                    TableCell(text = (i + 1).toString(), weight = column1Weight)
                    TableCell(text = result[0], weight = column2Weight)
                    TableCell(text = result[1], weight = column3Weight)
                    TableCell(text = result[2], weight = column4Weight)
                }
            }
        }
    }
}

class VoiceActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BOLKARTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.primaryContainer
                )
                {
                    toolbar_v1(func = this::finishActivity, hasBackArrow = true)
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Operation Mode: " + returnTextforoperationMode(),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontSize = 20.sp
                        )
                        MyVoiceTable();
                        Text(
                            text = "Click the button to speak",
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.width(width = 50.dp))
                        icon()
                    }

                }
            }
        }
    }

    fun finishActivity() {
        finish()
    }
    @Composable
    fun icon()
    {
        IconButton(onClick = this::voiceRecognize) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_keyboard_voice_24),
                contentDescription = "Menu",
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(128.dp),
            )
        }
    }
    fun voiceRecognize() {
        if (!SpeechRecognizer.isRecognitionAvailable(this)) {
            Toast.makeText(this, "Speech recognition is not available", Toast.LENGTH_SHORT)
                .show()
        } else {
            val i = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            i.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH)
            i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something")
            try {
                startActivityForResult(i, REQUEST_CODE_SPEECH_INPUT)
            } catch (e: Exception) {
                Toast
                    .makeText(
                        this@VoiceActivity, " " + e.message,
                        Toast.LENGTH_SHORT
                    )
                    .show()
            }
        }
    }
    // on below line we are calling on activity result method.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // in this method we are checking request
        // code with our result code.
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            // on below line we are checking if result code is ok
            if (resultCode == RESULT_OK && data != null) {

                // in that case we are extracting the
                // data from our array list
                //TODO: Arduino'ya gönderilmeyecek mesaj, kotlin içinde halledilecek
                val res: ArrayList<String> =
                    data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>
                val msg: String = res[0]

                if(msg.contains("rastgele tekrar" , ignoreCase = true))
                {
                    operationMode = 1;
                    numTopVoice   = 0;
                }
                else if(msg.contains("tekrar" , ignoreCase = true))
                {
                    operationMode = 0;
                    numTopVoice   = 0;
                }
                else if(msg.contains("rastgele sıra" , ignoreCase = true))
                {
                    operationMode = 3;
                    numTopVoice   = 0;
                }
                else if(msg.contains("sıra" , ignoreCase = true))
                {
                    operationMode = 2;
                    numTopVoice   = 0;
                }
                else if(msg.contains("oyun" , ignoreCase = true))
                {
                    operationMode = 4;
                    numTopVoice   = 0;
                }
                if(msg.contains("ağır ileri" , ignoreCase = true))
                {
                    spinVoice = 2;
                }
                else if(msg.contains("hafif ileri" , ignoreCase = true))
                {
                    spinVoice = 1;
                }
                else if(msg.contains("ağır geri" , ignoreCase = true))
                {
                    spinVoice = -1;
                }
                else if(msg.contains("hafif geri" , ignoreCase = true))
                {
                    spinVoice = -2;
                }
                else if(msg.contains("düz" , ignoreCase = true))
                {
                    spinVoice = 0;
                }

                if(msg.contains("hızlı" , ignoreCase = true))
                {
                    speedVoice = 1;
                }
                else if(msg.contains("orta" , ignoreCase = true))
                {
                    speedVoice = 0;
                }
                else if(msg.contains("yavaş" , ignoreCase = true))
                {
                    speedVoice = -1;
                }

                if(msg.contains("yüksek" , ignoreCase = true))
                {
                    frekansVoice = 2;
                }
                else if(msg.contains("normal" , ignoreCase = true))
                {
                    frekansVoice = 1;
                }
                else if(msg.contains("yavaş" , ignoreCase = true))
                {
                    frekansVoice = 0;
                }
                if(msg.contains("sağ" , ignoreCase = true))
                {
                    angleVoice = 1;
                }
                else if(msg.contains("sol" , ignoreCase = true))
                {
                    angleVoice = -1;
                }
                else if(msg.contains("merkez" , ignoreCase = true))
                {
                    angleVoice = 0;
                }

                if(msg.contains("sil" , ignoreCase = true))
                {
                    if( numTopVoice > 0)
                    {
                        numTopVoice = numTopVoice - 1 ;
                    }
                }
                if(msg.contains("kaydet" , ignoreCase = true))
                {
                    if(operationMode == 1 || operationMode == 3)
                    {
                        toplarVoice[numTopVoice*3 ] = 5;
                        toplarVoice[numTopVoice*3 + 1] = 5;
                        toplarVoice[numTopVoice*3 + 2] = 5;
                    }
                    else
                    {
                        toplarVoice[numTopVoice*3 ] = speedVoice;
                        toplarVoice[numTopVoice*3 + 1] = spinVoice;
                        toplarVoice[numTopVoice*3 + 2] = angleVoice;
                    }


                    speedVoice = 0;
                    spinVoice = 0;
                    angleVoice = 0;

                    toplarVoice[30 ] = operationMode;
                    toplarVoice[31]  = frekansVoice;
                    numTopVoice = min(9, numTopVoice + 1)
                    toplarVoice[32]  = numTopVoice;
                }
                Toast
                    .makeText(
                        this@VoiceActivity, " " + msg,
                        Toast.LENGTH_LONG
                    )
                    .show()
                if(msg.contains("başlat" , ignoreCase = true))
                {
                    if(operationMode == 4)
                    {
                        BluetoothArduino.mThread?.write( byteArrayOf(GameModeMsgId, 2, 20, 30))
                    }
                    else {
                        for (i in 0 until numTopVoice) {
                            writeBallInfo(i);
                        }

                        ballConfArrayVoice[30] = 20
                        ballConfArrayVoice[31] = 30;
                        ballConfArrayVoice[32] = numTopVoice.toByte();
                        BluetoothArduino.mThread?.write( byteArrayOf(OtherModesMsgId, ballConfArrayVoice.size.toByte()) + ballConfArrayVoice)
                    }
                }
                if(msg.contains("dur" , ignoreCase = true))
                {
                    val byteArray = ByteArray(3)
                    byteArray[0]  = StopMsgId
                    byteArray[1]  = 1
                    byteArray[2]  = 0
                    BluetoothArduino.mThread?.write(byteArray)
                }
            }

        }
        toplarVoice[32]  = numTopVoice;
        this@VoiceActivity.refreshActivity()
    }
    private fun refreshActivity() {
        val intent = this@VoiceActivity.intent;
        finish();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
}
