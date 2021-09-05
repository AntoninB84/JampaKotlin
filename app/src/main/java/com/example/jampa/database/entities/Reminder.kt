package com.example.jampa.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.jampa.database.Item

@Entity(tableName = "reminder_table",
        foreignKeys = [ForeignKey(
            entity = Item::class,
            parentColumns = ["itemId"],
            childColumns = ["itemId"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )])
data class Reminder(
    @PrimaryKey(autoGenerate = true)
    var reminderId: Long = 0L,
    @ColumnInfo(name = "reminder_type")
    var reminderType: Int = 0,
    @ColumnInfo(name = "reminder_date")
    var reminderDate: Long = 0L,
    @ColumnInfo(name = "reminder_custom_number")
    var reminderCustomNumber: Int = 0,
    @ColumnInfo(name = "reminder_custom_type")
    var reminderCustomType: Int = 0,

    var itemId: Long = 0L
)