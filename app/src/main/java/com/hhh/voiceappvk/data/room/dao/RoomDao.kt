package com.hhh.voiceappvk.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hhh.voiceappvk.data.room.model.AudioNote
import com.hhh.voiceappvk.util.UiState

@Dao
interface RoomDao {

    @Query("SELECT * FROM audioNote")
    fun getAll(): LiveData<List<AudioNote>>

    @Insert
    suspend fun insertAudioNote(note: AudioNote)

    @Update
    suspend fun updateAudioNote(note: AudioNote)

    @Delete
    suspend fun deleteAudioNote(note: AudioNote)
}