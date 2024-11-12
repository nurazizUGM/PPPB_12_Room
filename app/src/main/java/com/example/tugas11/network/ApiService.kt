package com.example.tugas11.network

import com.example.tugas11.model.AnimeResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("/v4/top/anime")
    fun getTopAnimes(): Call<AnimeResponse>
}