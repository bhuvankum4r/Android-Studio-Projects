package com.example.voice_recorder.recorder

import java.io.File

interface AudioRecorder {
    fun start(outputFile : File)
    fun stop()
}