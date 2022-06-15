package com.example.insectopedia.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface InsectopediaDAO {

    @Insert
    fun insert(insect: Insect)

    @Query("SELECT * FROM insects_table ORDER BY id DESC")
    fun getAll(): LiveData<List<Insect>>

    @Query("DELETE FROM insects_table")
    fun deleteAll()

    @Query("DELETE FROM insects_table WHERE id = :id")
    fun delete(id: Long)

    @Query("SELECT * FROM insects_table WHERE id = :id")
    fun get(id: Long): Insect
}