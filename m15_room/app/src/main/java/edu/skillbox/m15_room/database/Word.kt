package edu.skillbox.m15_room.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dictionary")
data class Word(
    @PrimaryKey
    @ColumnInfo(name = "word")
    val word: String,
    @ColumnInfo(name = "counter")
    val counter: Int
) {
    override fun toString(): String {
        return "$word - $counter"
    }
}
