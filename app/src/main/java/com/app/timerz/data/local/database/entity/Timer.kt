package com.app.timerz.data.local.database.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Timer(
    @PrimaryKey(autoGenerate = true)
    var id: Int,

    var title: String,

    var timerValue: String,

    var createdAt: String? = null,

    var updatedAt: String? = null,

    ) : Parcelable{

    @Ignore
    constructor(title: String, timerValue: String, createdAt: String) : this(0,
        title,
        timerValue,
        createdAt,
        null)
}