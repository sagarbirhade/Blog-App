package com.example.blogapp.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.blogapp.Model.BlogItemModel
import com.example.blogapp.databinding.ActivitySplashBinding
import com.example.blogapp.databinding.BlogItemBinding

class BlogAdapter(private val items : List<BlogItemModel>) : RecyclerView.Adapter<BlogAdapter.BLogViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BLogViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = BlogItemBinding.inflate(inflater, parent, false)
        return BLogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BLogViewHolder, position: Int) {
        holder.bind(items[position])
    }
    override fun getItemCount(): Int {
        return items.size
    }
    inner class BLogViewHolder(private val binding: BlogItemBinding) : RecyclerView.ViewHolder(binding.root){
            fun bind(blogItemModel: BlogItemModel) {
                binding.heading.text = blogItemModel.heading
                Glide.with(binding.profile).load(blogItemModel.imageUrl).into(binding.profile)
                binding.userName.text = blogItemModel.userName
                binding.date.text = blogItemModel.date
                binding.post.text = blogItemModel.post
                binding.likeCount.text = blogItemModel.likeCount.toString()
        }


    }
}