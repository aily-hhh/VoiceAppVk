package com.hhh.voiceappvk.util.audiorecorder.record

import java.io.File

interface AudioRecord {
    fun start(outputFile: File)
    fun stop()
}