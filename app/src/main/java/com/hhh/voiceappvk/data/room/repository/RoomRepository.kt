package com.hhh.voiceappvk.data.room.repository

import androidx.lifecycle.LiveData
import com.hhh.voiceappvk.data.room.dao.RoomDao
import com.hhh.voiceappvk.data.room.model.AudioNote
import javax.inject.Inject

class RoomRepository @Inject constructor(private val roomDao: RoomDao) {

    val allAudioNotes: LiveData<List<AudioNote>> = roomDao.getAll()

    suspend fun insertAudioNote(note: AudioNote) {
        roomDao.insertAudioNote(note)
    }

    suspend fun updateAudioNote(note: AudioNote) {
        roomDao.updateAudioNote(note)
    }

    suspend fun deleteAudioNote(note: AudioNote) {
        roomDao.deleteAudioNote(note)
    }
}