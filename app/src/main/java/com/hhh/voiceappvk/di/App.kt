package com.hhh.voiceappvk.di

import android.app.Application
import android.content.Intent
import com.hhh.voiceappvk.MainActivity
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKTokenExpiredHandler
import com.vk.api.sdk.auth.VKScope
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class App: Application() {}