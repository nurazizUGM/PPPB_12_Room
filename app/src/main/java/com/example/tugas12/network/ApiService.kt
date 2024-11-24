package com.example.tugas12.network

import com.example.tugas12.model.AnimeResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("/v4/top/anime")
    fun getTopAnimes(): Call<AnimeResponse>
}