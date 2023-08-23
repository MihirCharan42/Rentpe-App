package com.example.rentpe.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.rentpe.databinding.HouseItemBinding
import com.example.rentpe.databinding.ImageItemBinding

class ImagesAdapter(val images: List<String>): RecyclerView.Adapter<ImagesAdapter.ImagesViewHolder>() {

    inner class ImagesViewHolder(val binding: ImageItemBinding): RecyclerView.ViewHolder(binding.root){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
        val binding = ImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImagesViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        with(holder){
            with(images[position]) {
                val activity = binding.image.context as AppCompatActivity
                Glide.with(activity).load("https" + this.substring(4)).into(binding.image)
            }
        }
    }
}