package com.koindemo.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.koindemo.databinding.ItemPostBinding
import com.koindemo.model.Post

/**
 * Created by Manish Patel on 4/1/2020.
 */
class PostViewHolder(private val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post, onItemClickListener: PostListAdapter.OnItemClickListener? = null) {
        binding.postTitle.text = post.title
        binding.postAuthor.text = post.id

        onItemClickListener?.let { listener ->
            binding.root.setOnClickListener {
                listener.onItemClicked(post)
            }
        }
    }
}