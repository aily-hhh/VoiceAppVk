package com.hhh.voiceappvk.di

import android.content.Context
import androidx.room.Room
import com.hhh.voiceappvk.data.home.HomeDao
import com.hhh.voiceappvk.data.home.HomeRepository
import com.hhh.voiceappvk.data.room.dao.RoomDao
import com.hhh.voiceappvk.data.room.database.RoomDatabaseApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideStudentDao(appDataBase: RoomDatabaseApp): RoomDao {
        return appDataBase.getRoomDao()
    }

//    @Provides
//    fun baseURL() = BASE_URL

    @Provides
    fun logging() = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    fun okHttpClient() = OkHttpClient.Builder()
        .addInterceptor(logging())
        .build()

//    @Provides
//    @Singleton
//    fun provideRetrofit(baseUrl: String): GifServise =
//        Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
//            .client(okHttpClient()).build().create(GifServise::class.java)
}