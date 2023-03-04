package com.hhh.voiceappvk.ui.homeFragment

import android.view.View
import com.hhh.voiceappvk.data.room.model.AudioNote
import com.vk.sdk.api.docs.dto.DocsDoc

interface ItemClickListener {
    fun onItemLongClickListener(note: AudioNote, view: View)
}