package com.example.tugas12

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.tugas12.databinding.RvItemBinding
import com.example.tugas12.model.Anime
import com.squareup.picasso.Picasso
import java.util.concurrent.Future


class RvAdapter(
    private var animes: List<Anime>,
    private val action: (String) -> Unit,
    private val isSaved: (Int) -> Boolean,
    private val toggle: (Anime) -> Boolean
) : RecyclerView.Adapter<RvAdapter.ItemViewHolder>() {
    inner class ItemViewHolder(private val binding: RvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(anime: Anime) {
            with(binding) {
                Picasso.get().load(anime.images.jpg.imageUrl).into(imgThumbnail)
                txtTitle.text = anime.title
                txtStatus.text = "Status: ${anime.status}"
                txtRating.text = String.format("Rating: %.2f", anime.score)
                binding.txtTitle.setOnClickListener {
                    action(anime.url)
                }

                if (isSaved(anime.malId)) {
                    binding.btnBookmark.setImageResource(R.drawable.baseline_bookmark_24)
                } else {
                    binding.btnBookmark.setImageResource(R.drawable.baseline_bookmark_border_24)
                }

                btnBookmark.setOnClickListener {
                    val result = toggle(anime)
                    if (result) {
                        Log.d("a", "bind: saving ${anime.title}")
                        binding.btnBookmark.setImageResource(R.drawable.baseline_bookmark_24)
                    } else {
                        binding.btnBookmark.setImageResource(R.drawable.baseline_bookmark_border_24)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = RvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return animes.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        return holder.bind(animes[position])
    }
}