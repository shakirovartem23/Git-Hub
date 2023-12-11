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
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
fun fun1(
    list: EmployeeDao,
    name: String
): Boolean{
    val list1 = mutableListOf<String>()
    GlobalScope.launch(Dispatchers.IO) {
        list.allRepos().forEach {
            list1 += it.name
        }
    }
    return name in list1
}
@RequiresApi(Build.VERSION_CODES.M)
fun fun2(
    button: MaterialButton,
    white: ColorStateList,
    black: ColorStateList,
    employeeDao: EmployeeDao,
    s: String
): Boolean{
    val perm = fun1(employeeDao, s)

    when(perm){
        true -> button.foregroundTintList =
            white
        else -> button.foregroundTintList =
            black
    }

    return perm
}

class SelectRepoAdapter(
    private val resources: Resources,
    private val employeeDao: EmployeeDao,
    private val callback: callBack,
    private val names: List<String>,
    private val userName: String
) :
    RecyclerView.Adapter<SelectRepoAdapter.MyViewHolder>() {

    fun interface callBack{
        fun call(repoName: String)
    }

    val black = ColorStateList.valueOf(resources.getColor(R.color.black))
    val white = ColorStateList.valueOf(resources.getColor(R.color.white))

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
        val button = holder.selectButton

        button.text = names[position]
        holder.nameRepo.text = "Name Repository"

        holder.starButton.setOnClickListener {
            callback.call(names[position])
        }

        holder.starButton.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                employeeDao.updateRepository(Repository(
                    button.text.toString().hashCode(),
                    button.text.toString(),
                    userName,
                    fun2(button, white, black, employeeDao, names[position])
                ))
                    button.foregroundTintList =
                        black
            }
        }
    }

    override fun getItemCount() = names.size
}
