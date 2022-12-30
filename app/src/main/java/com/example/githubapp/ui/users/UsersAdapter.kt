package com.example.githubapp.ui.users

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.githubapp.R
import com.example.githubapp.ui.select_repo.RepoAdapter
import com.google.android.material.button.MaterialButton

class UsersAdapter(private val names: List<Triple<String, String, Int>>) : RecyclerView
.Adapter<UsersAdapter.MyViewHolder>(){

    class MyViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
        val largeTextView: TextView = itemView.findViewById(R.id.textNameUser)
        val smallTextView: TextView = itemView.findViewById(R.id.textDataStar)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recyclerview_item1, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.largeTextView.text = names[position].first
        holder.smallTextView.text = names[position].second+" "+names[position].third.toString()
    }

    override fun getItemCount() = names.size
}