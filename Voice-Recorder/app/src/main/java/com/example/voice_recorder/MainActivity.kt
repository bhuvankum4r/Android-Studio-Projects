package com.example.voice_recorder

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import com.example.voice_recorder.playback.AndroidAudioPlayer
import com.example.voice_recorder.recorder.AndroidAudioRecorder
import com.example.voice_recorder.ui.theme.VoiceRecorderTheme
import java.io.File

class MainActivity : ComponentActivity() {

    private val recorder by lazy {
        AndroidAudioRecorder(applicationContext)
    }

    private val player by lazy {
        AndroidAudioPlayer(applicationContext)
    }

    private var audioFile : File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(
            this,arrayOf(Manifest.permission.RECORD_AUDIO),0
        )
        enableEdgeToEdge()
        setContent {
            VoiceRecorderTheme {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(onClick = {
                        File(cacheDir,"audio.ogg").also{
                            recorder.start(it)
                            audioFile = it
                        }
                    }) {
                        Text("Start Recording")
                    }

                    Button(onClick = { recorder.stop() }) {
                        Text("Stop Recording")
                    }

                    Button(onClick = { player.playFile(audioFile ?: return@Button) }) {
                        Text("Play File")
                    }

                    Button(onClick = { player.stop() }) {
                        Text("Stop playing")
                    }



                }
            }
        }
    }
}
