package com.hhh.voiceappvk.di

import com.hhh.voiceappvk.data.HomeDao
import com.hhh.voiceappvk.data.HomeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideHomeRepository(): HomeDao {
        return HomeRepository()
    }
}