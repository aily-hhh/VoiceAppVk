package com.hhh.voiceappvk.util.audiorecorder.playback

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.core.net.toUri
import java.io.File
import java.io.FileInputStream

class AudioPlayer(): AudioPlay {

    private var player: MediaPlayer? = null

    override fun playFile(fileURL: String, function: () -> Unit) {
        MediaPlayer().apply {
            player = this
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setDataSource(fileURL)
            prepareAsync()
            setOnPreparedListener {
                start()
                setOnCompletionListener {
                    stop()
                    function()
                }
            }
        }
    }

    override fun stop() {
        if (player?.isPlaying == true) {
            player?.stop()
        }
        player?.release()
        player = null
    }

    fun isPlaying(): Boolean? {
        return player?.isPlaying
    }
}