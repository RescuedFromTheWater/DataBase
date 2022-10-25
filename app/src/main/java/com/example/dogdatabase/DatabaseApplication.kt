package com.example.dogdatabase

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.dogdatabase.data.database.Database

class DatabaseApplication : Application() {

    private var _database: Database? = null
    val Database get() = requireNotNull(_database)

    override fun onCreate() {
        super.onCreate()
        _database = Room
            .databaseBuilder(
                this,
                Database::class.java,
                "dogs-database"
            )
            .allowMainThreadQueries()
            .build()
    }
}

val Context.database: Database
    get() = when (this) {
        is DatabaseApplication -> Database
        else -> applicationContext.database
    }