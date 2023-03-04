package com.hhh.voiceappvk.data.room.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hhh.voiceappvk.data.room.model.AudioNote
import com.hhh.voiceappvk.data.room.repository.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomViewModel @Inject constructor(private val repository: RoomRepository): ViewModel() {

    val allAudioNotes: LiveData<List<AudioNote>> = repository.allAudioNotes

    fun insertAudioNote(note: AudioNote) {
        viewModelScope.launch {
            repository.insertAudioNote(note)
        }
    }

    fun updateAudioNote(note: AudioNote) {
        viewModelScope.launch {
            repository.updateAudioNote(note)
        }
    }

    fun deleteAudioNote(note: AudioNote) {
        viewModelScope.launch {
            repository.deleteAudioNote(note)
        }
    }
}