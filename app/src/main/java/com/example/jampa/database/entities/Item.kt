package com.example.jampa.database

import androidx.room.*
import com.example.jampa.database.entities.Category
import com.example.jampa.database.entities.Reminder
import com.example.jampa.database.entities.Type
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.util.*

@Entity(tableName = "item_table",
    foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = ["categoryId"],
        childColumns = ["categoryId"],
        onUpdate = ForeignKey.CASCADE
    ), ForeignKey(
        entity = Type::class,
        parentColumns = ["typeId"],
        childColumns = ["typeId"],
        onUpdate = ForeignKey.CASCADE
    )])
data class Item(
    @PrimaryKey(autoGenerate = true)
    var itemId: Long = 0L,
    var title: String = "Title",
    var description: String = "description",
    var priority: Int = -1,
    var categoryId: Long = 1,
    var typeId: Long = 1,

    @Embedded
    var state: State,
    @Embedded
    var taskLife: TaskLife,
    @Embedded
    var recurrency: Recurrency
)

data class State(
    @ColumnInfo(name = "current_state")
    var currentState: Int = 0,
    @ColumnInfo(name = "previous_state")
    var previousState: Int? = -1,
    var fails: Int? = 0,
    var successes: Int? = 0
)

data class TaskLife(
    @ColumnInfo(name = "creation_date")
    var creationDate: Long = 0L,
    @ColumnInfo(name = "starting_date")
    var startingDate: Long = 0L,
    @ColumnInfo(name = "term_date")
    var termDate: Long = 0L
)

data class Recurrency(
    @ColumnInfo(name = "type")
    var recTyp: Int = 0,
    var customRec: Int = 0,
    var delayType: Int = 0,
    var day: Long = 0L,
    var hour: String = ""
)