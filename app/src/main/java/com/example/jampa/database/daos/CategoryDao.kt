package com.example.jampa.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.jampa.database.Item
import com.example.jampa.database.entities.Category

@Dao
interface CategoryDao{

    @Insert
    suspend fun insert(category: Category)

    @Update
    suspend fun update(category: Category)

    @Delete
    suspend fun delete(category: Category)

    // No suspend with LiveData !!!
    @Query("SELECT * FROM category_table ORDER BY category_name ASC")
    fun getAllCategories(): LiveData<List<Category>>

    @Query("DELETE FROM category_table")
    suspend fun clearCategoryDB()
}