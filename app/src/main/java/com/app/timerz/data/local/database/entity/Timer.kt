package com.app.timerz.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Timer (
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    
    val title : String,
    
    val createdAt : String,
    
    val updatedAt : String,

    )