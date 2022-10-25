package com.example.dogdatabase.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DogsDao {

    @Query("SELECT * FROM dogs")
    fun getAll(): Flow<List<Dogs>>

    @Query("SELECT * FROM dogs")
    fun getAllDog(): List<Dogs>

    @Query("SELECT * FROM dogs WHERE id = :id")
    fun loadAllById(id: Long): Flow<Dogs>

    @Insert
    fun insertAll(vararg dogs: Dogs)

    @Delete
    fun delete(dogs: Dogs)

    @Update
    suspend fun update(item: Dogs)
}