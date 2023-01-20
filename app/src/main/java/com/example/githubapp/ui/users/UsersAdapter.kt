package com.example.githubapp.ui.users

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.githubapp.R
import com.squareup.picasso.Picasso

class UsersAdapter(private val names: List<Triple<String, String, String>>) : RecyclerView
.Adapter<UsersAdapter.MyViewHolder>(){

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val largeTextView: TextView = itemView.findViewById(R.id.textNameUser)
        val smallTextView: TextView = itemView.findViewById(R.id.textDataStar)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recyclerview_item1, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val name = names[position].first
        holder.largeTextView.text = name
        holder.smallTextView.text = names[position].second+" "+names[position].third
        Picasso.get().load("https://avatars.githubusercontent.com/$name").into(holder.imageView)
    }

    override fun getItemCount() = names.size
}