package com.example.voice_recorder_legacy

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var btnRecord: Button
    private lateinit var btnStop: Button
    private lateinit var btnPlay: Button
    private lateinit var statusText: TextView

    private var mediaRecorder: MediaRecorder? = null
    private var mediaPlayer: MediaPlayer? = null

    private lateinit var outputFile: String

    private val REQUEST_PERMISSION_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout) // Your XML layout file

        // Bind views
        btnRecord = findViewById(R.id.btn_record)
        btnStop = findViewById(R.id.btn_stop)
        btnPlay = findViewById(R.id.btn_play)
        statusText = findViewById(R.id.display_text)

        btnStop.isEnabled = false
        btnPlay.isEnabled = false

        // Use app-specific external storage
        val outputDir = getExternalFilesDir(null)
        outputFile = File(outputDir, "audio.mp4").absolutePath

        // Set click listeners
        btnRecord.setOnClickListener { checkPermissionsAndRecord() }
        btnStop.setOnClickListener { stopRecording() }
        btnPlay.setOnClickListener { playRecording() }
    }

    private fun checkPermissionsAndRecord() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                REQUEST_PERMISSION_CODE
            )
        } else {
            startRecording()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startRecording()
            } else {
                statusText.text = "Permission denied. Cannot record audio."
            }
        }
    }

    private fun startRecording() {
        try {
            mediaRecorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(outputFile)
                prepare()
                start()
            }

            statusText.text = "Recording..."
            btnRecord.isEnabled = false
            btnStop.isEnabled = true
            btnPlay.isEnabled = false

        } catch (e: IOException) {
            e.printStackTrace()
            statusText.text = "Recording failed: ${e.message}"
        }
    }

    private fun stopRecording() {
        try {
            mediaRecorder?.apply {
                stop()
                release()
            }
        } catch (e: RuntimeException) {
            e.printStackTrace()
            statusText.text = "Stop failed: ${e.message}"
        }
        mediaRecorder = null

        statusText.text = "Recording stopped. Saved to $outputFile"
        btnRecord.isEnabled = true
        btnStop.isEnabled = false
        btnPlay.isEnabled = true
    }

    private fun playRecording() {
        try {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(outputFile)
                prepare()
                start()
                statusText.text = "Playing recording..."
                setOnCompletionListener {
                    statusText.text = "Playback finished."
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            statusText.text = "Playback failed: ${e.message}"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaRecorder?.release()
        mediaRecorder = null
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
