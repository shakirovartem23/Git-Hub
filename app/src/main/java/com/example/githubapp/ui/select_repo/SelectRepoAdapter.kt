package com.example.githubapp.ui.select_repo

import Save_Data.EmployeeDao
import Save_Data.Repository
import android.content.res.ColorStateList
import android.content.res.Resources
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.githubapp.R
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SelectRepoAdapter(
    private val resources: Resources,
    private val employeeDao: EmployeeDao,
    private var names: List<String>,
    private var userName: String,
    private var stargazersCount: List<Int>,
    private val callback: callBack,
) :
    RecyclerView.Adapter<SelectRepoAdapter.MyViewHolder>() {

    @RequiresApi(Build.VERSION_CODES.M)
    fun makerColor(bool: Boolean, starButton: MaterialButton) {
        starButton.foregroundTintList = when(bool){
            true ->
                ColorStateList.valueOf(resources.getColor(R.color.white))
            else ->
                ColorStateList.valueOf(resources.getColor(R.color.black))
        }
    }
    fun interface callBack{
        fun call(repoName: String)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val selectButton: MaterialButton = itemView.findViewById(R.id.button)
        val nameRepo: TextView = itemView.findViewById(R.id.textViewSmall)
        val starButton: MaterialButton = itemView.findViewById(R.id.star1)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recyclerview_item, parent, false)
        return MyViewHolder(itemView)
    }



    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            val button = holder.selectButton
            val repoName = names[position]

            button.text = repoName
            holder.nameRepo.text = "Name Repository"

            if(employeeDao.selectRepo(repoName)==null){
                employeeDao.insertRepo(
                    Repository(
                        0,
                        names[position],
                        userName,
                        stargazersCount[position],
                        false
                    )
                )
            }

            button.setOnClickListener {
                callback.call(repoName)
            }

            makerColor(
                employeeDao
                    .selectRepo(
                        repoName
                    )
                    .favourite,
                holder
                    .starButton
            )
            holder.starButton.setOnClickListener {
                GlobalScope.launch(Dispatchers.Main) {
                    var obj = employeeDao.selectRepo(repoName)
                    makerColor(!obj.favourite, holder.starButton)
                    employeeDao
                        .updateRepository(
                            Repository(
                                obj.id,
                                obj.name,
                                obj.ownerName,
                                obj.stargazers_count,
                                !obj.favourite
                            )
                        )
                }
            }
        }
    }

    fun update(items: List<Pair<String, Int>>){
        items.forEach {
            names+=it.first
            stargazersCount+=it.second

        }
        MyViewHolder(items)
    }
    override fun getItemCount() = names.size
}
