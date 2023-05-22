package com.sergeyrusak.mediaplayer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager


class Stats : AppCompatActivity() {
    var isPlaying = false
    var length = 0
    var played = 0
    private lateinit var progressBar: ProgressBar
    private lateinit var textView: TextView
    private lateinit var playBtn: Button
    private lateinit var backbtn: Button

    private val model: MyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)
        backbtn = findViewById<Button>(R.id.backbutton)
        playBtn = findViewById<Button>(R.id.button)
        progressBar = findViewById<ProgressBar>(R.id.progressBar)
        textView = findViewById<TextView>(R.id.textView)

        playBtn.setOnClickListener{
            MyViewModel.currentStatus.value= !MyViewModel.currentStatus.value!!
            Log.d("mylog", "${MyViewModel.currentStatus.value}")

        }
        backbtn.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }


        val statusObserver = Observer<Boolean>{
                newStatus ->
            isPlaying = newStatus
            if (isPlaying){
                playBtn.text = "Pause"
            }
            else{
                playBtn.text = "Play"
            }
        }
        MyViewModel.getStatusSingleton().observe(this, statusObserver)

        val lengthObserver = Observer<Int>{
                newLength ->
            length = newLength
            textView.text = "${played/60000}:${played/1000%60}/${length/60000}:${length/1000%60}"
            progressBar.max = length
        }
        MyViewModel.getLengthSingleton().observe(this, lengthObserver)

        val positionObserver = Observer<Int>{
                newPos ->
            played = newPos
            textView.text = "${played/60000}:${played/1000%60}/${length/60000}:${length/1000%60}"
            progressBar.progress = played
        }
        MyViewModel.getPositionSingleton().observe(this, positionObserver)

    }
}