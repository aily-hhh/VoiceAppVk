package com.hhh.voiceappvk.ui.homeFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageSwitcher
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hhh.voiceappvk.R
import com.vk.sdk.api.docs.dto.DocsDoc

class HomeAdapter: RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    inner class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleItemVoice: TextView = itemView.findViewById(R.id.titleItemVoice)
        val datetimeItemVoice: TextView = itemView.findViewById(R.id.datetimeItemVoice)
        val durationItemVoice: TextView = itemView.findViewById(R.id.durationItemVoice)
        val tickingItemVoice: TextView = itemView.findViewById(R.id.tickingItemVoice)
        val switcherPlayStop: ImageSwitcher = itemView.findViewById(R.id.switcherPlayStop)
    }

    private val callback = object: DiffUtil.ItemCallback<DocsDoc>() {
        override fun areItemsTheSame(oldItem: DocsDoc, newItem: DocsDoc): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: DocsDoc, newItem: DocsDoc): Boolean {
            return if (oldItem != newItem) {
                false
            } else if (oldItem.id == newItem.id) {
                false
            } else if (oldItem.date == newItem.date) {
                false
            } else if (oldItem.type == newItem.type) {
                false
            } else if (oldItem.ext == newItem.ext) {
                false
            } else if (oldItem.ownerId == newItem.ownerId) {
                false
            } else if (oldItem.size == newItem.size) {
                false
            } else if (oldItem.title == newItem.title) {
                false
            } else {
                return oldItem.url == newItem.url
            }
        }
    }

    private val differ = AsyncListDiffer(this, callback)

    fun setDiffer(list: List<DocsDoc>) {
        this.differ.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        )
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val currentItem = differ.currentList[position]

        holder.titleItemVoice.text = currentItem.title
        holder.datetimeItemVoice.text = currentItem.date.toString()

    }



    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}