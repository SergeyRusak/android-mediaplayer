package com.sergeyrusak.mediaplayer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager


class MainActivity : AppCompatActivity() {
    var isPlaying = false
    val m = MediaPlayer()
    private lateinit var playButton: Button
    private var isPlay = false
    var firstTime = true

    private lateinit var statusObserver: Observer<Boolean>
    private val model: MyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playButton = findViewById<Button>(R.id.play_btn)
        val goToButton = findViewById<Button>(R.id.stats_btn)


        val afd = assets.openFd("track.mp3")
        m.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
        afd.close()
        m.prepare()

        MyViewModel.currentLength.value = m.duration

        playButton.setOnClickListener{
            MyViewModel.currentStatus.value = !isPlaying
        }

        goToButton.setOnClickListener{
            startActivity(Intent(this, Stats::class.java))
        }

        Thread {
            while (true) {
                MyViewModel.currentPosition.postValue(m.currentPosition)
                if (MyViewModel.currentStatus.value!! != isPlaying){
                    isPlaying = MyViewModel.currentStatus.value!!
                    if (isPlaying){
                        m.start()
                    }
                    else{
                        m.pause()
                    }
                }

                Thread.sleep(1000)
            }
        }.start()
        statusObserver = Observer<Boolean>{
                newStatus ->
            isPlaying = newStatus
            if (firstTime){
                firstTime = false
                Log.d("mylog", "OBSERVER created")
                return@Observer
            }
            if (isPlaying) {
                playButton.text = "Pause"
                m.start()
            }else{
                playButton.text = "Play"
                m.pause()
            }
            Log.d("mylog", "OBSERVER: ${isPlaying}")
        }

        MyViewModel.getStatusSingleton().observe(this, statusObserver)
    }
}
