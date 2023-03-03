package com.hhh.voiceappvk.ui.homeFragment

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hhh.voiceappvk.R
import com.hhh.voiceappvk.util.audiorecorder.playback.AudioPlayer
import com.vk.sdk.api.docs.dto.DocsDoc

class HomeAdapter: RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    inner class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleItemVoice: TextView = itemView.findViewById(R.id.titleItemVoice)
        val datetimeItemVoice: TextView = itemView.findViewById(R.id.datetimeItemVoice)
        val durationItemVoice: TextView = itemView.findViewById(R.id.durationItemVoice)
        val tickingItemVoice: TextView = itemView.findViewById(R.id.tickingItemVoice)
        val playItemVoice: ImageView = itemView.findViewById(R.id.playItemVoice)
        val stopItemVoice: ImageView = itemView.findViewById(R.id.stopItemVoice)
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
        holder.datetimeItemVoice.text = java.time.format.DateTimeFormatter.ISO_INSTANT
            .format(java.time.Instant.ofEpochSecond(currentItem.date.toLong()))

        holder.itemView.apply {
            setOnLongClickListener {
                clickListener!!.onItemLongClickListener(currentItem, holder.itemView)
                false
            }
        }
    }

    override fun onViewAttachedToWindow(holder: HomeViewHolder) {
        super.onViewAttachedToWindow(holder)

        holder.itemView.apply {
            holder.playItemVoice.setOnClickListener {
                holder.playItemVoice.visibility = View.GONE
                holder.stopItemVoice.visibility = View.VISIBLE

                differ.currentList[holder.adapterPosition].url?.let { file ->
                    audioPlayer.playFile(file) {
                        holder.stopItemVoice.visibility = View.GONE
                        holder.playItemVoice.visibility = View.VISIBLE
                    }
                }
            }
            holder.stopItemVoice.setOnClickListener {
                holder.stopItemVoice.visibility = View.GONE
                holder.playItemVoice.visibility = View.VISIBLE
                audioPlayer.stop()
            }
        }
    }

    override fun onViewDetachedFromWindow(holder: HomeViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.playItemVoice.setOnClickListener(null)
        holder.stopItemVoice.setOnClickListener(null)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var clickListener: ItemClickListener? = null
    fun setClickListener(clickListener: ItemClickListener) {
        this.clickListener = clickListener
    }

    private val audioPlayer = AudioPlayer()
}