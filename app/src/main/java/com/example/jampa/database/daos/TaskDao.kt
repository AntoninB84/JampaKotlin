package com.example.jampa.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.jampa.database.Item
import com.example.jampa.database.entities.Task
import kotlinx.coroutines.CompletableJob

@Dao
interface TaskDao{


    @Transaction
    @Query("SELECT * FROM item_table")
    fun getTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM item_table WHERE itemId = :id")
    suspend fun getTask(id: Long): Item

    @Insert
    suspend fun insert(item: Item): Long

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)

    // No suspend with LiveData !!!
    @Query("SELECT * FROM item_table")
    fun getAllItems(): LiveData<List<Item>>

    @Query("DELETE FROM item_table")
    suspend fun clearItemDB()
}