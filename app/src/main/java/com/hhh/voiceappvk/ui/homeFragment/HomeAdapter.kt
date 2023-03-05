package com.hhh.voiceappvk.ui.homeFragment

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hhh.voiceappvk.R
import com.hhh.voiceappvk.data.room.model.AudioNote
import com.hhh.voiceappvk.util.audiorecorder.playback.AudioPlayer
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import java.text.SimpleDateFormat
import java.util.logging.Handler
import kotlin.time.Duration.Companion.milliseconds

class HomeAdapter: RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    inner class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleItemVoice: TextView = itemView.findViewById(R.id.titleItemVoice)
        val datetimeItemVoice: TextView = itemView.findViewById(R.id.datetimeItemVoice)
        val durationItemVoice: TextView = itemView.findViewById(R.id.durationItemVoice)
        val tickingItemVoice: TextView = itemView.findViewById(R.id.tickingItemVoice)
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
        val sdf = java.text.SimpleDateFormat("dd.MM.yyyy, HH:mm")
        val date = java.util.Date(currentItem.dateTime * 1000)
        holder.datetimeItemVoice.text = sdf.format(date)
        holder.durationItemVoice.text = currentItem.duration

        holder.itemView.apply {
            setOnLongClickListener {
                clickListener!!.onItemLongClickListener(currentItem, holder.itemView)
                false
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewAttachedToWindow(holder: HomeViewHolder) {
        super.onViewAttachedToWindow(holder)

        holder.itemView.apply {
            holder.playItemVoice.setOnClickListener {
                if (audioPlayer.isPlaying() == true) {
                    Toast.makeText(context, context.getString(R.string.warning), Toast.LENGTH_SHORT).show()
                } else {
                    holder.playItemVoice.visibility = View.GONE
                    holder.stopItemVoice.visibility = View.VISIBLE
                    holder.tickingItemVoice.visibility = View.VISIBLE

                    flag = true
                    var ms: Long = 1000
                    val template = SimpleDateFormat("mm:ss/")

                    fun plusMs(): Observable<Long> {
                        return Observable.create {
                            while(flag) {
                                Thread.sleep(1)
                                ms++
                                it.onNext(ms)
                            }
                            it.onComplete()
                        }
                    }

                    plusMs().subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            val time = java.util.Date(ms)
                            holder.tickingItemVoice.text = template.format(time)
                        }

                    differ.currentList[holder.adapterPosition].filePath.let { file ->
                        audioPlayer.playFile(file) {
                            holder.stopItemVoice.visibility = View.GONE
                            holder.playItemVoice.visibility = View.VISIBLE
                            holder.tickingItemVoice.visibility = View.GONE
                            flag = false
                        }
                    }
                }
            }
            holder.stopItemVoice.setOnClickListener {
                holder.stopItemVoice.visibility = View.GONE
                holder.playItemVoice.visibility = View.VISIBLE
                holder.tickingItemVoice.visibility = View.GONE
                flag = false
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
    private var flag = true
}