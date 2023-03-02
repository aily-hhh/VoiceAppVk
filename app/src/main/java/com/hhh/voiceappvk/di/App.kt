package com.hhh.voiceappvk.di

import android.app.Application
import android.content.Intent
import com.hhh.voiceappvk.MainActivity
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKTokenExpiredHandler
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class App: Application() {

    override fun onCreate() {
        super.onCreate()
        VK.addTokenExpiredHandler(tokenTracker)
    }

    private val tokenTracker = object: VKTokenExpiredHandler {
        override fun onTokenExpired() {
            val intent = Intent(this@App, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }
}