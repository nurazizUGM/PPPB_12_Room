package com.example.tugas12.model

import com.google.gson.annotations.SerializedName

data class AnimeResponse(
    @SerializedName("data")
    val data: List<Anime>
)
