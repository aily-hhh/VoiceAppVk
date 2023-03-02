package com.hhh.voiceappvk.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hhh.voiceappvk.util.UiState
import com.vk.sdk.api.docs.dto.DocsDoc
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: HomeRepository): ViewModel() {

    private var _getFiles: MutableLiveData<UiState<List<DocsDoc>>> = MutableLiveData()
    val getFiles: LiveData<UiState<List<DocsDoc>>> get() = _getFiles
    fun getFiles() {
        _getFiles.value = UiState.Loading()
        repository.getFiles {
            _getFiles.value = it
        }
    }

}