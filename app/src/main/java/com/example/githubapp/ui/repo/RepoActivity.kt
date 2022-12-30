package com.example.githubapp.ui.repo

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.githubapp.R
import com.example.githubapp.data.remove.GitApi1.retrofitService1
import com.example.githubapp.data.remove.request_second.Repo1
import com.example.githubapp.ui.users.UsersActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Double
import java.lang.Math.ceil


class RepoActivity : AppCompatActivity() {

    private fun roundNum(num: Int): List<Int> {
        if (num in 0..5) {
            return listOf(1, 3, 5)
        } else if (num % 100 == 50 || num % 100 == 0) {
            var num = num - 25
            val list = mutableListOf<Int>()
            for (int in 1..3) {
                list += num
                num += 25
            }
            return list
        } else if (num in 26..74) {
            var num = ((num / 50) + 1) * 50 - 50
            var list = listOf<Int>()
            for (int in 1..3) {
                list += num
                num += 25
            }
            return list
        } else {
            val num = num.toDouble()
            val list = listOf(num - ceil(num / 2.0) / 2.0, num, num + ceil(num / 2.0) / 2.0)
            return list.map { it.toInt() }.toSet().toList()
        }
        return listOf(1, 2, 3)
    }

    private fun selectNum(list: List<Int>): MutableList<Int> {
        var result = mutableListOf(0, 0, 0, 0)
        list.forEach { it ->
            when(it){
                in 3..5 -> result[1]+=1
                in 6..8 -> result[2]+=1
                in 9..11 -> result[3]+=1
                else -> result[0]+=1
            }
        }
        return result
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.repo)

        println("Error 1")

        val button = findViewById<Button>(R.id.button1)

        val title = intent.getStringExtra("title")!!
        val user = intent.getStringExtra("user")!!
        val barChart = findViewById<BarChart>(R.id.idBarChart)

        setTitle(title)

        val listStar: Call<List<com.example.githubapp.data.remove.request_second.Repo1>> = retrofitService1.listRepos1(user, title)
        listStar.enqueue(object : Callback<List<Repo1>> {
            override fun onResponse(call: Call<List<Repo1>>, response: Response<List<Repo1>>) {
                val body = response.body()
                var list_for_star = listOf<Int>()
                if (body != null) {
                    for (i in body){
                        list_for_star+=(i.starred_at.substring(5..6)).toInt()
                        println("Error1 $body")
                    }
                }
                var entries = listOf<BarEntry>()

                var count = 1f

                list_for_star = selectNum(list_for_star)

                println("Error: $list_for_star")

                list_for_star.forEach { value ->
                    entries+=BarEntry(count, value.toFloat(), "October")
                    count+=1
                }

                val barDataSet = BarDataSet(entries, title)

                barChart.axisRight.setDrawLabels(false)
                barChart.axisLeft.granularity = 1f
                barChart.axisLeft.axisMinimum = 0f
                barChart.axisLeft.axisMaximum = 5f
                barChart.xAxis.setDrawLabels(false)
                barChart.setTouchEnabled(false)
                barChart.legend.isEnabled = true

                val data = BarData(barDataSet)
                barChart.data = data
                barChart.invalidate()

                button.setOnClickListener {
                    val intent = Intent(this@RepoActivity, UsersActivity::class.java)
                    intent.putExtra("title", title)
                    intent.putExtra("user", user)
                    startActivity(intent)
                }

            }

            override fun onFailure(call: Call<List<Repo1>>, t: Throwable) {
                t.printStackTrace()
            }

        })

    }


}