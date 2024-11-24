package com.example.tugas12.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class Bookmark(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "mal_id")
    val malId: Int,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "url")
    val url: String,

    @ColumnInfo(name = "status")
    val status: String,

    @ColumnInfo(name = "score")
    val score: Float,

    @ColumnInfo(name = "image")
    val image: String
)
