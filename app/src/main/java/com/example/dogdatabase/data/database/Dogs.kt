package com.example.dogdatabase.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Dogs(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val breedOfDog: String,
    val nickName: String,
    val hostName: String,
    val address: String
)