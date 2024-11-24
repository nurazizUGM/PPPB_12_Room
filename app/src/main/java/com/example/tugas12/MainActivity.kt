package com.example.tugas12

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugas12.database.Bookmark
import com.example.tugas12.database.BookmarkDao
import com.example.tugas12.database.BookmarkDatabase
import com.example.tugas12.databinding.ActivityMainBinding
import com.example.tugas12.model.Anime
import com.example.tugas12.model.AnimeResponse
import com.example.tugas12.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors
import java.util.concurrent.Future

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val executorService by lazy { Executors.newSingleThreadExecutor() }
    private val mBookmarkDao by lazy {
        val db = BookmarkDatabase.getDatabase(this)
        db!!.bookmarkDao()!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onResume() {
        super.onResume()

        val client = ApiClient.getInstance()
        val response = client.getTopAnimes()
        response.enqueue(object : Callback<AnimeResponse> {
            override fun onResponse(p0: Call<AnimeResponse>, p1: Response<AnimeResponse>) {
                if (p1.isSuccessful) {
                    val animes = p1.body()?.data ?: listOf()
                    binding.recyclerView.adapter = RvAdapter(animes, { url: String ->
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                    }, { malId -> check(malId).get() }, { malId -> toggle(malId).get() })

                    val layoutManager = LinearLayoutManager(this@MainActivity)
                    layoutManager.orientation = LinearLayoutManager.VERTICAL
                    binding.recyclerView.layoutManager = layoutManager
                } else {
                    Log.e("FAILED", p1.body().toString())
                    Toast.makeText(this@MainActivity, "Failed fetch data", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(p0: Call<AnimeResponse>, p1: Throwable) {
                Log.e("ERROR", p1.toString())
                Toast.makeText(this@MainActivity, "Failed fetch data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun save(anime: Anime) {
        executorService.execute {
            val bookmarkAnime = Bookmark(
                malId = anime.malId,
                title = anime.title,
                status = anime.status,
                url = anime.url,
                image = anime.images.jpg.imageUrl,
                score = anime.score
            )
            mBookmarkDao.insert(bookmarkAnime)
        }
    }

    private fun delete(bookmark: Bookmark) {
        executorService.execute {
            mBookmarkDao.delete(bookmark)
        }
    }

    private fun check(malId: Int): Future<Boolean> {
        return executorService.submit<Boolean> {
            val bookmark = mBookmarkDao.getOne(malId)
            bookmark != null
        }
    }

    private fun toggle(anime: Anime): Future<Boolean> {
        return executorService.submit<Boolean>({
            val bookmark = mBookmarkDao.getOne(anime.malId)
            if (bookmark == null) {
                save(anime)
                true
            } else {
                delete(bookmark)
                false
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_items, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_bookmarks -> {
                startActivity(Intent(this, BookmarksActivity::class.java))
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}