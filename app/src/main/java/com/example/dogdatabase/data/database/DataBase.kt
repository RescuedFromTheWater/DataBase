package com.example.dogdatabase.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Dogs::class], version = 1, exportSchema = false)
abstract class Database : RoomDatabase() {
    abstract val dogsDao: DogsDao
}