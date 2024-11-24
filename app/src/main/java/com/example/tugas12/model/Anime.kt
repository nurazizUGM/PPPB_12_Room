package com.example.tugas12.model

import com.google.gson.annotations.SerializedName

data class Anime(
    @SerializedName("title")
    val title: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("score")
    val score: Float,
    @SerializedName("images")
    val images: AnimeImage,
    @SerializedName("mal_id")
    val malId: Int
)

data class AnimeImage(
    @SerializedName("jpg")
    val jpg: AnimeImageJpg
)

data class AnimeImageJpg(
    @SerializedName("image_url")
    val imageUrl: String
)