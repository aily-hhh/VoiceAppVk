package com.hhh.voiceappvk.data.room.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "audioNote")
data class AudioNote(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo("dateTime")
    val dateTime: Long,
    @ColumnInfo("duration")
    val duration: Long,
    @ColumnInfo("filePath")
    val filePath: String,
    @ColumnInfo("title")
    var title: String
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeLong(dateTime)
        parcel.writeLong(duration)
        parcel.writeString(filePath)
        parcel.writeString(title)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AudioNote> {
        override fun createFromParcel(parcel: Parcel): AudioNote {
            return AudioNote(parcel)
        }

        override fun newArray(size: Int): Array<AudioNote?> {
            return arrayOfNulls(size)
        }
    }
}
