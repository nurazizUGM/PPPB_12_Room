package com.example.tugas11

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugas11.databinding.ActivityMainBinding
import com.example.tugas11.model.AnimeResponse
import com.example.tugas11.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val client = ApiClient.getInstance()
        val response = client.getTopAnimes()
        response.enqueue(object : Callback<AnimeResponse> {
            override fun onResponse(p0: Call<AnimeResponse>, p1: Response<AnimeResponse>) {
                if (p1.isSuccessful) {
                    val animes = p1.body()?.data ?: listOf()
                    binding.recyclerView.adapter = RvAdapter(animes) { url: String ->
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                    }
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
}