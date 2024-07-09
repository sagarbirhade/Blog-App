package com.example.blogapp.Adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.blogapp.Model.BlogItemModel
import com.example.blogapp.databinding.ActivitySplashBinding
import com.example.blogapp.databinding.BlogItemBinding

class BlogAdapter(private val items : List<BlogItemModel>) : RecyclerView.Adapter<BlogAdapter.BLogViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BLogViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: BLogViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
    override fun getItemCount(): Int {
        return items.size
    }
    inner class BLogViewHolder(private val binding: BlogItemBinding) : RecyclerView.ViewHolder(binding.root){


    }
}