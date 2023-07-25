package com.example.githubapp.ui.select_repo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.githubapp.R
import com.google.android.material.button.MaterialButton

class SelectRepoAdapter(private val names: List<String>, private val callBack: CallBack) :
    RecyclerView.Adapter<SelectRepoAdapter.MyViewHolder>() {

    fun interface CallBack{
        fun onClick(str: String)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val largeTextView: MaterialButton = itemView.findViewById(R.id.button)
        val smallTextView: TextView = itemView.findViewById(R.id.textViewSmall)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recyclerview_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.largeTextView.text = names[position]
        holder.smallTextView.text = "Name Repo"
        holder.largeTextView.setOnClickListener{
            val text = holder.largeTextView.text.toString()
            callBack.onClick(text)
        }

    }

    override fun getItemCount() = names.size
}