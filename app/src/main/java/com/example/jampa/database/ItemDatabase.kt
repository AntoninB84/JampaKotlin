package com.example.jampa.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.jampa.database.daos.CategoryDao
import com.example.jampa.database.daos.ReminderDao
import com.example.jampa.database.daos.TaskDao
import com.example.jampa.database.daos.TypeDao
import com.example.jampa.database.entities.Category
import com.example.jampa.database.entities.Reminder
import com.example.jampa.database.entities.Type

@Database(entities = [Item::class, Category::class, Type::class, Reminder::class], version = 8, exportSchema = false)
abstract class ItemDatabase: RoomDatabase() {

    abstract val taskDao: TaskDao
    abstract val categoryDao: CategoryDao
    abstract val typeDao: TypeDao
    abstract val reminderDao: ReminderDao

    companion object{
        @Volatile
        private var INSTANCE: ItemDatabase? = null

        fun getInstance(context: Context): ItemDatabase {
            synchronized(this){

                var instance = INSTANCE

                if(instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ItemDatabase::class.java,
                        "JampaDB"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

}