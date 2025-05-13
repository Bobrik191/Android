package com.example.lab4

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.View
import android.widget.Button
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.IOException
import android.widget.EditText
import android.widget.RadioButton
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var videoView: VideoView
    private var isAudio = true
    private var mediaUri: Uri? = null

    private val filePicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            mediaUri = it
            playMedia(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        videoView = findViewById(R.id.videoView)
        val playBtn = findViewById<Button>(R.id.playBtn)
        val pauseBtn = findViewById<Button>(R.id.pauseBtn)
        val stopBtn = findViewById<Button>(R.id.stopBtn)
        val chooseFileBtn = findViewById<Button>(R.id.chooseFileBtn)
        val loadFromUrlBtn = findViewById<Button>(R.id.loadFromUrlBtn)
        val urlInput = findViewById<EditText>(R.id.urlInput)

        val audioRadio = findViewById<RadioButton>(R.id.audioRadio)
        val videoRadio = findViewById<RadioButton>(R.id.videoRadio)

        audioRadio.setOnClickListener { isAudio = true; videoView.visibility = View.GONE }
        videoRadio.setOnClickListener { isAudio = false; videoView.visibility = View.VISIBLE }

        chooseFileBtn.setOnClickListener {
            val type = if (isAudio) "audio/*" else "video/*"
            filePicker.launch(type)
        }

        loadFromUrlBtn.setOnClickListener {
            val url = urlInput.text.toString()
            if (url.isNotBlank()) {
                val uri = Uri.parse(url)
                mediaUri = uri
                playMedia(uri)
            }
        }

        playBtn.setOnClickListener {
            if (isAudio && ::mediaPlayer.isInitialized) {
                mediaPlayer.start()
            } else if (!isAudio && mediaUri != null) {
                videoView.start()
            }
        }

        pauseBtn.setOnClickListener {
            if (isAudio && ::mediaPlayer.isInitialized) {
                mediaPlayer.pause()
            } else if (!isAudio && videoView.isPlaying) {
                videoView.pause()
            }
        }

        stopBtn.setOnClickListener {
            if (isAudio && ::mediaPlayer.isInitialized) {
                mediaPlayer.stop()
                mediaPlayer.release()
            } else if (!isAudio && videoView.isPlaying) {
                videoView.stopPlayback()
            }
        }
    }

    private fun playMedia(uri: Uri) {
        if (isAudio) {
            videoView.visibility = View.GONE
            if (::mediaPlayer.isInitialized) mediaPlayer.release()
            mediaPlayer = MediaPlayer().apply {
                setDataSource(this@MainActivity, uri)
                prepare()
                start()
            }
        } else {
            videoView.visibility = View.VISIBLE
            videoView.setVideoURI(uri)
            videoView.start()
        }
    }
}
