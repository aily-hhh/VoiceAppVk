package com.hhh.voiceappvk.data

import com.hhh.voiceappvk.util.UiState
import com.vk.sdk.api.docs.dto.DocsDoc

interface HomeDao {
    fun getFiles(result: (UiState<List<DocsDoc>>) -> Unit)
}