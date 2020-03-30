/*
 * Copyright 2020 Manish Patel. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.expdemo.ui.jetpackpaging

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.expdemo.R
import kotlinx.android.synthetic.main.recyclerview_item.view.*

/**
 * Created by Manish Patel on 3/30/2020.
 */
class MainAdapter(onImageListener: OnImageListener): PagedListAdapter<UnsplashImageDetails, MainAdapter.MyViewHolder>(diffCallback) {

    private lateinit var ctx : Context
    private var mOnImageListener: OnImageListener = onImageListener

    class MyViewHolder(imageView: ImageView, onImageListener: OnImageListener) :
        RecyclerView.ViewHolder(imageView), View.OnClickListener {

        var onImageListener : OnImageListener
        init {
            this.onImageListener = onImageListener
            imageView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            onImageListener.onImageClick(adapterPosition)
        }
    }

    interface OnImageListener{
        fun onImageClick(position: Int)
    }

    companion object {

        private val diffCallback = object : DiffUtil.ItemCallback<UnsplashImageDetails>() {
            override fun areItemsTheSame(oldItem: UnsplashImageDetails, newItem: UnsplashImageDetails): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: UnsplashImageDetails, newItem: UnsplashImageDetails): Boolean =
                oldItem.equals(newItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val imageView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_item, parent, false) as ImageView
        ctx = parent.context
        return MyViewHolder(imageView,mOnImageListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val post = getItem(position)
        Glide.with(ctx)
            .load(post?.urls?.regular)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.itemView.imageViewItem)
    }

}