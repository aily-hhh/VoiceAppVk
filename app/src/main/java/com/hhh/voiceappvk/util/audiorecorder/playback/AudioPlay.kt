package com.hhh.voiceappvk.util.audiorecorder.playback

import java.io.File

interface AudioPlay {
    fun playFile(fileURL: String, function: () -> Unit)
    fun stop()
}