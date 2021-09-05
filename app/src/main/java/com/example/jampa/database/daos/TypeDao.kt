package com.example.jampa.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.jampa.database.Item
import com.example.jampa.database.entities.Category
import com.example.jampa.database.entities.Type

@Dao
interface TypeDao{

    @Insert
    suspend fun insert(type: Type)

    @Update
    suspend fun update(type: Type)

    @Delete
    suspend fun delete(type: Type)

    // No suspend with LiveData !!!
    @Query("SELECT * FROM type_table")
    fun getAllTypes(): LiveData<List<Type>>

    @Query("DELETE FROM type_table")
    suspend fun clearTypeDB()
}