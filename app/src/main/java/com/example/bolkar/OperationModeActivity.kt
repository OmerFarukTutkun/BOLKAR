package com.example.bolkar


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bolkar.ui.theme.BOLKARTheme
import com.faruk_tutkun.bolkar.ui.theme.Button_v3
import com.faruk_tutkun.bolkar.ui.theme.toolbar_v1
import java.lang.Integer.min


@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float
) {
    Text(
        text = text,
        fontSize = 12.sp,
        color = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier
            .border(1.dp, Color.Black)
            .weight(weight)
            .padding(8.dp)
    )
}

@Composable
fun TableScreen(byteArray: ByteArray) {

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
            val numberOfBall = byteArray[32].toInt()
            for (i in 0 until numberOfBall) {       // i in 1 until 10, excluding 10
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    TableCell(text = (i + 1).toString(), weight = column1Weight)
                    TableCell(text = byteArray[3 * i].toUByte().toString(), weight = column2Weight)
                    TableCell(text = byteArray[3 * i + 1].toUByte().toString(), weight = column3Weight)
                    TableCell(text = byteArray[3 * i + 2].toUByte().toString(), weight = column4Weight)
                }
            }
        }
    }
}

var ballConfArray = ByteArray(33)

class OperationModeActivity : ComponentActivity() {
    private var numberOfAddedBall = 0;
    private var defaultFreq = 1f;
    private var defaultTotalBalls = 0f;
    override fun onCreate(savedInstanceState: Bundle?) {
        defaultFreq = ballConfArray[30].toFloat();
        defaultTotalBalls = ballConfArray[31].toFloat();
        numberOfAddedBall = ballConfArray[32].toInt();
        super.onCreate(savedInstanceState)
        setContent {
            BOLKARTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.primaryContainer
                )
                {
                    val operation =
                        this@OperationModeActivity.intent.getStringExtra("operation").toString()
                    var freq by remember { mutableStateOf(defaultFreq) }
                    var totalBalls by remember { mutableStateOf(defaultTotalBalls) }

                    toolbar_v1(func = {
                        finish()
                    }, hasBackArrow = true, hasMenu = false)

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(all = 4.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    )
                    {
                        Text(
                            text = "Balls per Minute: ",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.width(width = 12.dp))

                        Text(text = freq.toInt().toString())
                        Slider(
                            value = freq,
                            onValueChange = { freq = it; ballConfArray[30] = freq.toInt().toByte(); },
                            valueRange = 1f..60f,
                            steps = 58
                        )

                        Spacer(modifier = Modifier.width(width = 12.dp))
                        Text(
                            text = "Number of Balls: ",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.width(width = 12.dp))

                        Text(text = totalBalls.toInt().toString())
                        Slider(
                            value = totalBalls,
                            onValueChange = {
                                totalBalls = it; ballConfArray[31] = totalBalls.toInt().toByte();
                            },
                            valueRange = 0f..60f,
                            steps = 59
                        )
                        if (!operation.equals("Game Mode"))
                            TableScreen(byteArray = ballConfArray);
                    }
                    if (!operation.equals("Game Mode")) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = 4.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.Bottom
                        )
                        {
                            Button_v3(
                                text = "Clear",
                                func = this@OperationModeActivity::clearByteArray,
                                height = 60,
                                width = 120,
                                fontSize = 12,
                                padding = 0
                            )
                            Button_v3(
                                text = "Remove Last",
                                func = this@OperationModeActivity::removeLast,
                                height = 60,
                                width = 120,
                                fontSize = 12,
                                padding = 0
                            )

                            Button_v3(
                                text = "Add Ball",
                                func = this@OperationModeActivity::startAddBallActivity,
                                height = 60,
                                width = 120,
                                fontSize = 12,
                                padding = 0
                            )
                        }
                    }
                }
            }
        }
    }

    fun reloadActivity() {
        val intent = this@OperationModeActivity.intent;
        finish();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
    fun removeLast() {
        if(numberOfAddedBall > 0) {
            numberOfAddedBall--;
            ballConfArray[32] = numberOfAddedBall.toByte();
            this@OperationModeActivity.reloadActivity()
        }
    }
    fun clearByteArray() {
        for (i in 0 until 33) {
            ballConfArray[i] = 0;
        }
        this@OperationModeActivity.reloadActivity()
    }

    fun startAddBallActivity() {
        val intent = Intent(this@OperationModeActivity, AddBallActivity::class.java)
        startActivityForResult(intent, BallActivityReturnCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == BallActivityReturnCode) {
            if (resultCode == Activity.RESULT_OK) {
                if (numberOfAddedBall == 10)
                    numberOfAddedBall = 9;
                ballConfArray[numberOfAddedBall * 3] = data?.getIntExtra("SpeedResult", 0)!!.toByte();
                ballConfArray[numberOfAddedBall * 3 + 1] =
                    data?.getIntExtra("SpinResult", 0)!!.toByte();
                ballConfArray[numberOfAddedBall * 3 + 2] =
                    data?.getIntExtra("AngleResult", 0)!!.toByte();
                numberOfAddedBall = min(10, numberOfAddedBall + 1);
            }
        }
        ballConfArray[32] = numberOfAddedBall.toByte();

        this@OperationModeActivity.reloadActivity()
    }

    fun addRandomBall() {
        if (numberOfAddedBall == 10)
            numberOfAddedBall = 9;

        ballConfArray[numberOfAddedBall * 3] = (0..10).random().toByte();
        ballConfArray[numberOfAddedBall * 3 + 1] = (0..10).random().toByte();
        ballConfArray[numberOfAddedBall * 3 + 2] = (0..10).random().toByte();
        numberOfAddedBall = min(10, numberOfAddedBall + 1);
        ballConfArray[32] = numberOfAddedBall.toByte();

        this@OperationModeActivity.reloadActivity()
    }
}