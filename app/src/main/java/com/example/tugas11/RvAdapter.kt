package com.example.tugas11

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tugas11.databinding.RvItemBinding
import com.example.tugas11.model.Anime
import com.squareup.picasso.Picasso


class RvAdapter(
    private val animes: List<Anime>,
    private val action: (String) -> Unit
) : RecyclerView.Adapter<RvAdapter.ItemViewHolder>() {
    inner class ItemViewHolder(private val binding: RvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(anime: Anime) {
            with(binding) {
                Picasso.get().load(anime.images.jpg.imageUrl).into(imgThumbnail)
                txtTitle.text = anime.title
                txtStatus.text = "Status: ${anime.status}"
                txtRating.text = String.format("Rating: %.2f", anime.score)
                binding.root.setOnClickListener {
                    action(anime.url)
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