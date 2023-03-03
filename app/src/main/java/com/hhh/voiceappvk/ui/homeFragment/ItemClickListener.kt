package com.hhh.voiceappvk.ui.homeFragment

import android.view.View
import com.vk.sdk.api.docs.dto.DocsDoc

interface ItemClickListener {
    fun onItemClickListener(doc: DocsDoc)
    fun onItemLongClickListener(doc: DocsDoc, view: View)
}