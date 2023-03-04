package com.hhh.voiceappvk.ui.homeFragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hhh.voiceappvk.R
import com.hhh.voiceappvk.data.room.model.AudioNote
import com.hhh.voiceappvk.util.audiorecorder.playback.AudioPlayer

class HomeAdapter: RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    inner class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleItemVoice: TextView = itemView.findViewById(R.id.titleItemVoice)
        val datetimeItemVoice: TextView = itemView.findViewById(R.id.datetimeItemVoice)
        val durationItemVoice: TextView = itemView.findViewById(R.id.durationItemVoice)
        val tickingItemVoice: Chronometer = itemView.findViewById(R.id.tickingItemVoice)
        val playItemVoice: ImageView = itemView.findViewById(R.id.playItemVoice)
        val stopItemVoice: ImageView = itemView.findViewById(R.id.stopItemVoice)
    }

    private val callback = object: DiffUtil.ItemCallback<AudioNote>() {
        override fun areItemsTheSame(oldItem: AudioNote, newItem: AudioNote): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: AudioNote, newItem: AudioNote): Boolean {
            return if (oldItem != newItem) {
                false
            } else if (oldItem.id == newItem.id) {
                false
            } else if (oldItem.dateTime == newItem.dateTime) {
                false
            } else if (oldItem.duration == newItem.duration) {
                false
            } else if (oldItem.title == newItem.title) {
                false
            } else {
                return oldItem.filePath == newItem.filePath
            }
        }
    }

    private val differ = AsyncListDiffer(this, callback)

    fun setDiffer(list: List<AudioNote>) {
        this.differ.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        )
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val currentItem = differ.currentList[position]

        holder.titleItemVoice.text = currentItem.title
        val sdf = java.text.SimpleDateFormat("dd.MM.yyyy HH:mm")
        val date = java.util.Date(currentItem.dateTime * 1000)
        holder.datetimeItemVoice.text = sdf.format(date)
        val templateDuration = java.text.SimpleDateFormat("HH:mm:ss")
        val currentDuration = java.util.Date(currentItem.duration * 1000)
        holder.durationItemVoice.text = templateDuration.format(currentDuration)

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

                differ.currentList[holder.adapterPosition].filePath.let { file ->
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