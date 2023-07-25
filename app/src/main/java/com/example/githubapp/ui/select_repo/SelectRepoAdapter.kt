package com.example.githubapp.ui.select_repo

import Save_Data.EmployeeDao1
import Save_Data.Repository
import Save_Data.User
import android.content.res.ColorStateList
import android.content.res.Resources
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.githubapp.R
import com.example.githubapp.Saved.returnResult
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SelectRepoAdapter(private val resources: Resources, private val employeeDao1: EmployeeDao1, private val callBack1: returnResult, private val names: List<String>, private val callBack: CallBack) :
    RecyclerView.Adapter<SelectRepoAdapter.MyViewHolder>() {

    fun interface CallBack{
        fun onClick(str: String)
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

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            holder.selectButton.text = names[position]
            holder.nameRepo.text = "Name Repo"
            GlobalScope.launch(Dispatchers.Main) {
                val bool = when (employeeDao1.allLikeStar()
                    .find { it.repositoryId1.name1 == names[position] }) {
                    null -> false
                    else -> true
                }
                if (bool) {
                    println("Error")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        holder.starButton.foregroundTintList =
                            ColorStateList.valueOf(resources.getColor(R.color.white))
                    }
                } else {
                    println("Error1")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        holder.starButton.foregroundTintList =
                            ColorStateList.valueOf(resources.getColor(R.color.black))
                    }
                }
            }
            holder.selectButton.setOnClickListener {
                GlobalScope.launch(Dispatchers.Main) {
                    val allStar = employeeDao.selectStarOfRepo(holder.selectButton.text.toString())
                    if (ColorStateList.valueOf(resources.getColor(R.color.white)) == starButton.foregroundTintList
                    ) {
                        GlobalScope.launch(Dispatchers.Main) {
                            allStar.forEach {
                                val OS =
                                    Repository(
                                        it.repositoryId.id,
                                        it.repositoryId.name,
                                        User(
                                            it.userId.id,
                                            it.userId.name,
                                            it.userId.avatarUrl
                                        )
                                    )
                                employeeDao.insertOfRepo(OS)
                                starButton.foregroundTintList =
                                    ColorStateList.valueOf(resources.getColor(R.color.black))
                            }
                        }
                    } else {
                        GlobalScope.launch(Dispatchers.Main) {
                            allStar.forEach {
                                val OS = Repository(
                                    it.repositoryId.id,
                                    it.repositoryId.name,
                                    User(
                                        it.userId.id,
                                        it.userId.name,
                                        it.userId.avatarUrl
                                    )
                                )
                                employeeDao.insertOfRepo(OS)
                                starButton.foregroundTintList =
                                    ColorStateList.valueOf(resources.getColor(R.color.white))
                            }
                        }
                    }
                }
            }
            holder.starButton.setOnClickListener {
                println("Error2")
                callBack1.onClick(holder.starButton, holder.selectButton.text.toString())
            }
        }
    }

    override fun getItemCount() = names.size
}