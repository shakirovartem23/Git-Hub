package com.example.githubapp.ui.repo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.githubapp.R
import com.example.githubapp.data.remove.GitApi1.retrofitService1
import com.example.githubapp.data.remove.request_second.Repo1
import com.example.githubapp.ui.select_repo.SelectRepoActivity
import com.example.githubapp.ui.users.UsersActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Double


class RepoActivity : AppCompatActivity() {

    private fun selectNum(list: List<Int>): MutableList<Int> {
        val result = mutableListOf(0, 0, 0, 0)
        list.forEach {
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

//        val button = findViewById<Button>(R.id.buttonOnChart)
        val button1 = findViewById<Toolbar>(R.id.head)

        val title = intent.getStringExtra("title")!!
        val user = intent.getStringExtra("user")!!
        val barChart = findViewById<BarChart>(R.id.idBarChart)
        val button2 = findViewById<Button>(R.id.button2)
        val button3 = findViewById<Button>(R.id.button3)
        val button4 = findViewById<Button>(R.id.button4)
        val button5 = findViewById<Button>(R.id.button5)

        setTitle(title)

        button1.setOnClickListener{
            val intent = Intent(this@RepoActivity, SelectRepoActivity::class.java)
            startActivity(intent)
        }

        val listStar: Call<List<Repo1>> = retrofitService1.listRepos1(user, title)
        listStar.enqueue(object : Callback<List<Repo1>> {
            override fun onResponse(call: Call<List<Repo1>>, response: Response<List<Repo1>>) {
                val body = response.body()
                val map = mutableMapOf<String, String>()
                var list_for_star = mutableListOf<Int>()
                if (body != null) {
                    for (i in body){
                        list_for_star+=(i.starred_at.substring(5..6)).toInt()
                        i.
                        map+=Double(i.starred_at, i.user.login)
                    }
                }
                val entries = mutableListOf<BarEntry>()
                var count = 1f

                list_for_star = selectNum(list_for_star)

                list_for_star.forEach { value ->
                    entries+=BarEntry(count, value.toFloat())
                    count+=1
                }

                val barDataSet = BarDataSet(entries, title)
                val axisLeft = barChart.axisLeft
                val axisRight = barChart.axisRight
                val xAxis = barChart.xAxis


                barChart.axisRight.setDrawAxisLine(false)
                axisLeft.isEnabled = false
                axisLeft.setDrawAxisLine(false)
                axisRight.setDrawAxisLine(false)
                axisRight.isEnabled = false
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                barChart.setTouchEnabled(true)
                xAxis.isEnabled = false
                barChart.legend.isEnabled = false
                barChart.notifyDataSetChanged()
                xAxis.setDrawAxisLine(false)
                xAxis.setDrawGridLines(false)
                barChart.description.isEnabled = false

                val data = BarData(barDataSet)
                barChart.data = data
                barDataSet.colors = listOf(resources.getColor(R.color.teal_200), resources.getColor(R.color.purple_200), resources.getColor(R.color.redFull), resources.getColor(R.color.purple_700))

                val intent = Intent(this@RepoActivity, UsersActivity::class.java)
                intent.putExtra("title", title)
                intent.putExtra("user", user)
                startActivity(intent)

            }

            override fun onFailure(call: Call<List<Repo1>>, t: Throwable) {
                t.printStackTrace()
            }

        })

    }


}