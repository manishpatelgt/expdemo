package com.daggerhiltdemo.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import com.daggerhiltdemo.data.model.Post
import com.daggerhiltdemo.databinding.ItemPostBinding

class PostViewHolder(private val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.postTitle.text = post.title
        binding.postAuthor.text = post.id
    }
}