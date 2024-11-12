package com.example.tugas11.model

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
    val images: AnimeImage
)

data class AnimeImage(
    @SerializedName("jpg")
    val jpg: AnimeImageJpg
)

data class AnimeImageJpg(
    @SerializedName("image_url")
    val imageUrl: String
)