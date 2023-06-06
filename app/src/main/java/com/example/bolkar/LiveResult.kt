package com.example.bolkar

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bolkar.ui.theme.BOLKARTheme
import com.faruk_tutkun.bolkar.ui.theme.toolbar_v1

class LiveResultActivity : ComponentActivity() {
    private val refreshInterval = 100L
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BOLKARTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.primaryContainer
                )
                {

                    toolbar_v1(func = { finish() }, hasBackArrow = true, hasMenu = false)
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(all = 4.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Box(
                            Modifier
                                .height(200.dp)
                                .width(200.dp)
                                .background(MaterialTheme.colorScheme.primary)
                                .padding(20.dp)
                        ) {
                            Text(
                                text = basariliKarsilama.toString(),
                                fontSize = 50.sp,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }

                        Box(
                            Modifier
                                .height(200.dp)
                                .width(200.dp)
                                .background(MaterialTheme.colorScheme.primary)
                                .padding(20.dp)
                        ) {
                            Text(
                                text = toplamAtilanTop.toString(),
                                fontSize = 50.sp,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
            }
        }

    }
    override fun onResume() {
        super.onResume()
        startRefreshing()
    }

    override fun onPause() {
        super.onPause()
        stopRefreshing()
    }

    private fun startRefreshing() {
        handler.postDelayed(refreshRunnable, refreshInterval)
    }

    private fun stopRefreshing() {
        handler.removeCallbacks(refreshRunnable)
    }

    private val refreshRunnable = object : Runnable {
        override fun run() {
            // Place your refresh logic here
            // This code will be executed every second

            // Call the method to refresh your activity or update UI components
            refreshActivity()

            // Schedule the next execution
            handler.postDelayed(this, refreshInterval)
        }
    }

    private fun refreshActivity() {
        val intent = this@LiveResultActivity.intent;
        finish();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
}