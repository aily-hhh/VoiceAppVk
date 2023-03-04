package com.hhh.voiceappvk.data.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hhh.voiceappvk.data.room.dao.RoomDao
import com.hhh.voiceappvk.data.room.model.AudioNote

@Database(entities = [AudioNote::class], version = 1)
abstract class RoomDatabaseApp: RoomDatabase() {
    abstract fun getRoomDao(): RoomDao
}