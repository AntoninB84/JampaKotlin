package com.example.jampa.database.entities

import androidx.room.Embedded
import androidx.room.Relation
import com.example.jampa.database.Item

data class Task(
    @Embedded
    var item: Item,

    @Relation(
        parentColumn = "categoryId",
        entityColumn = "categoryId"
    )
    val category: Category,

    @Relation(
        parentColumn = "typeId",
        entityColumn = "typeId"
    )
    val type: Type
)