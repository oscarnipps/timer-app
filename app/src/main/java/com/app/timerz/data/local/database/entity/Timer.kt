package com.app.timerz.data.local.database.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class Timer(
    @PrimaryKey(autoGenerate = true)
    var id: Int,

    var title: String,

    var timerValue: String,

    var createdAt: String? = null,

    var updatedAt: String? = null,

    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(timerValue)
        parcel.writeString(createdAt)
        parcel.writeString(updatedAt)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Timer> {
        override fun createFromParcel(parcel: Parcel): Timer {
            return Timer(parcel)
        }

        override fun newArray(size: Int): Array<Timer?> {
            return arrayOfNulls(size)
        }
    }

    @Ignore
    constructor(title: String, timerValue: String, createdAt: String) : this(0,
        title,
        timerValue,
        createdAt,
        null)
}