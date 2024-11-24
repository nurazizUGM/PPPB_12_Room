package com.example.tugas12

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugas12.database.Bookmark
import com.example.tugas12.database.BookmarkDao
import com.example.tugas12.database.BookmarkDatabase
import com.example.tugas12.databinding.ActivityBookmarksBinding
import com.example.tugas12.model.Anime
import com.example.tugas12.model.AnimeImage
import com.example.tugas12.model.AnimeImageJpg
import java.util.concurrent.Executors

class BookmarksActivity : AppCompatActivity() {
    private val binding by lazy { ActivityBookmarksBinding.inflate(layoutInflater) }
    private val executorService by lazy { Executors.newSingleThreadExecutor() }
    private lateinit var mBookmarkDao: BookmarkDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val db = BookmarkDatabase.getDatabase(this)
        mBookmarkDao = db!!.bookmarkDao()!!

        mBookmarkDao.bookmarks.observe(this) { bookmarks ->
            val animes = convertData(bookmarks)
            binding.rvBookmarks.adapter = RvAdapter(
                animes,
                { url -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url))) },
                { true },
                { anime -> delete(anime.malId) }
            )
            binding.rvBookmarks.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_items, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_home -> {
                finish()
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun convertData(bookmarks: List<Bookmark>): List<Anime> {
        return bookmarks.map {
            Anime(
                title = it.title,
                url = it.url,
                status = it.status,
                score = it.score,
                images = AnimeImage(AnimeImageJpg(it.image)),
                malId = it.malId,
            )
        }
    }

    private fun delete(malId: Int): Boolean {
        executorService.execute {
            val bookmark = mBookmarkDao.getOne(malId)
            if (bookmark != null) {
                mBookmarkDao.delete(bookmark)
            }
        }
        return false
    }
}