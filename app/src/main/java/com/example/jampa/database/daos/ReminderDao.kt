package com.example.jampa.database.daos

import androidx.room.*
import com.example.jampa.database.entities.Reminder

@Dao
interface ReminderDao {

    @Insert
    suspend fun insert(reminder: Reminder): Long

    @Update
    suspend fun update(reminder: Reminder)

    @Delete
    suspend fun delete(reminder: Reminder)

}