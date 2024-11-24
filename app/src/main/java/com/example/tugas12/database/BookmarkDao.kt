package com.example.tugas12.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BookmarkDao {
    @Insert
    fun insert(bookmark: Bookmark)

    @Delete
    fun delete(bookmark: Bookmark)

    @get:Query("select * from bookmarks")
    public val bookmarks: LiveData<List<Bookmark>>

    @Query("SELECT * FROM bookmarks WHERE mal_id = :malId LIMIT 1")
    fun getOne(malId: Int): Bookmark?
}