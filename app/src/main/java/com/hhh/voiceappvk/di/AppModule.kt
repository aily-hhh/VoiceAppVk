package com.hhh.voiceappvk.di

import android.content.Context
import androidx.room.Room
import com.hhh.voiceappvk.data.room.dao.RoomDao
import com.hhh.voiceappvk.data.room.database.RoomDatabaseApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): RoomDatabaseApp {
        return Room.databaseBuilder(
            context,
            RoomDatabaseApp::class.java,
            "app_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideRoomDao(appDataBase: RoomDatabaseApp): RoomDao {
        return appDataBase.getRoomDao()
    }
}