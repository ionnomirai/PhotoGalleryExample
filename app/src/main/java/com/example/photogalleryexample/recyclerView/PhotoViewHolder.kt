package com.example.photogalleryexample.recyclerView

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.photogalleryexample.R
import com.example.photogalleryexample.databinding.ListItemGalleryBinding
import com.example.photogalleryexample.retrofit.data.GalleryItem

class PhotoViewHolder(private val binding: ListItemGalleryBinding) :
    RecyclerView.ViewHolder(binding.root) {
        fun bind(galleryItem: GalleryItem){
            binding.itemImageView.load(galleryItem.url){
                placeholder(R.drawable.def)
            }
        }
}