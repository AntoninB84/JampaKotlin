package com.example.jampa.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "type_table")
data class Type(
    @PrimaryKey(autoGenerate = true)
    var typeId: Long = 0L,

    @ColumnInfo(name = "type_name")
    var name: String = "type"
)