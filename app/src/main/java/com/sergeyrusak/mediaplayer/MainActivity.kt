package com.sergeyrusak.mediaplayer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager


class MainActivity : AppCompatActivity() {
    var isPlaying = false
    val m = MediaPlayer()
    private lateinit var playButton: Button
    private var isPlay = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playButton = findViewById<Button>(R.id.play_btn)
        val goToButton = findViewById<Button>(R.id.stats_btn)


        val afd = assets.openFd("track.mp3")
        m.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
        afd.close()
        m.prepare()

        playButton.setOnClickListener{
           switch()
        }

        goToButton.setOnClickListener{
            startActivity(Intent(this, Stats::class.java))
        }

        Thread {
            while (true) {
                val intent = Intent("player_condition")
                intent.putExtra("isPlay", isPlaying)
                intent.putExtra("played", m.currentPosition)
                intent.putExtra("length", m.duration)
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
                Log.d("mylog", "Sending broadcast: ${m.currentPosition} / ${m.duration}")
                Thread.sleep(1000)
            }
        }.start()

        val lbm = LocalBroadcastManager.getInstance(this)
        lbm.registerReceiver(receiver, IntentFilter("switch_player"))
    }

    var receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            switch()
        }
    }

    fun switch(){
        isPlaying = !isPlaying
        if (isPlaying){
            playButton.text = "Pause"
            m.start()
        }
        else{
            playButton.text = "Play"
            m.pause()
        }
    }
}