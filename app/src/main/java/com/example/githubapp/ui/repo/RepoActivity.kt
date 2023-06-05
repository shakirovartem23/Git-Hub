package com.example.githubapp.ui.repo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.githubapp.R
import com.example.githubapp.Saved.SaveDataForSelect
import com.example.githubapp.ui.select_repo.SelectRepoActivity
import com.example.githubapp.ui.users.UsersActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class RepoActivity : AppCompatActivity() {

    private fun Year(map: MutableMap<String, String>): MutableList<Int> {
        var listNum = mutableListOf<Int>()
        listNum += SelectTime.YEAR.DurationList(map, "Winter").size
        listNum += SelectTime.YEAR.DurationList(map, "Spring").size
        listNum += SelectTime.YEAR.DurationList(map, "Summer").size
        listNum += SelectTime.YEAR.DurationList(map, "Autumn").size
        return listNum
    }

    private fun Season(map: MutableMap<String, String>): MutableList<Int> {
        var listNum = mutableListOf<Int>()
        listNum += SelectTime.SEASON.DurationList(map, "January").size
        listNum += SelectTime.SEASON.DurationList(map, "February").size
        listNum += SelectTime.SEASON.DurationList(map, "March").size
        listNum += SelectTime.SEASON.DurationList(map, "April").size
        listNum += SelectTime.SEASON.DurationList(map, "May").size
        listNum += SelectTime.SEASON.DurationList(map, "June").size
        listNum += SelectTime.SEASON.DurationList(map, "Jule").size
        listNum += SelectTime.SEASON.DurationList(map, "August").size
        listNum += SelectTime.SEASON.DurationList(map, "September").size
        listNum += SelectTime.SEASON.DurationList(map, "October").size
        listNum += SelectTime.SEASON.DurationList(map, "November").size
        listNum += SelectTime.SEASON.DurationList(map, "December").size
        return listNum
    }

    private fun Month(map: MutableMap<String, String>): MutableList<Int> {
        var listNum = mutableListOf<Int>()
        val currentDate = Date()
        val dateFormat: DateFormat = SimpleDateFormat("MM", Locale.getDefault())
        val dateText: String = dateFormat.format(currentDate)
        listNum += SelectTime.MONTHS.DurationList(map, "$dateText First").size
        listNum += SelectTime.MONTHS.DurationList(map, "$dateText Second").size
        listNum += SelectTime.MONTHS.DurationList(map, "$dateText Third").size
        listNum += SelectTime.MONTHS.DurationList(map, "$dateText Forth").size
        return listNum
    }

    private fun FreeTime(map: MutableMap<String, String>, inputText: String): MutableList<Int> {
        var listNum = mutableListOf<Int>()
        val currentDate = Date()
        val dateFormat: DateFormat = SimpleDateFormat("MM", Locale.getDefault())
        val dateText: String = dateFormat.format(currentDate)
        listNum += SelectTime.DAYS.DurationList(map, "$dateText $inputText").size
        return listNum
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.repo)

        val title = intent.getStringExtra("title")!!
        val user = intent.getStringExtra("user")!!
        val barChart: BarChart = findViewById(R.id.idBarChart)
        val text = findViewById<EditText>(R.id.text2)
        val button6 = findViewById<Button>(R.id.button6)

        val mapResult = SaveDataForSelect()
//        mapResult.loadTimeStarring(user, title)
        val map = mapResult.resultTimeStarring

        setTitle(title)

        findViewById<Toolbar>(R.id.head).setOnClickListener {
            val intent = Intent(this@RepoActivity, SelectRepoActivity::class.java)
            startActivity(intent)
        }
        val entries = mutableListOf<BarEntry>()
            var count = 0f
            val listYear = listOf("Winter", "Spring", "Summer", "Autumn")
            val listSeason = listOf("January",
                "February",
                "March",
                "April",
                "May",
                "June",
                "Jule",
                "August",
                "September",
                "October",
                "November",
                "December")
            val listMonth = listOf("First", "Second", "Third", "Forth")
            val listFreeTime = listOf("Result")

            button6.setOnClickListener {
            var list_for_star = when (text.text.toString().split(" ")[0]) {
                "Year" -> Year(map)
                "Season" -> Season(map)
                "Month" -> Month(map)
                else -> FreeTime(map, text.text.toString())
            }
            val list = when (text.text.toString().split(", ")[0]) {
                "Year" -> listYear
                "Season" -> listSeason
                "Month" -> listMonth
                else -> listOf("Result")
            }
            list_for_star.forEach { value ->
                entries += BarEntry(count, value.toFloat())
                count += 1
            }
            println("Error: ${text.text}")

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
            barChart.legend.isEnabled = false
            xAxis.setDrawGridLines(false)
            xAxis.setDrawAxisLine(false)
            barChart.isScaleXEnabled = false
            barChart.description.isEnabled = false
            if(list!=listFreeTime){
                xAxis.valueFormatter = IndexAxisValueFormatter(list)
                xAxis.labelCount = list.size - 1
                xAxis.labelRotationAngle = 90f
            } else{
                xAxis.isEnabled = false
            }

            val data = BarData(barDataSet)
            barChart.data = data
            barDataSet.colors = listOf(resources.getColor(R.color.teal_200),
                resources.getColor(R.color.purple_200),
                resources.getColor(R.color.redFull),
                resources.getColor(R.color.purple_700))

            barChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                    override fun onValueSelected(e: Entry?, h: Highlight?) {
                        val intent = Intent(this@RepoActivity, UsersActivity::class.java)
                        intent.putExtra("title", title)
                        intent.putExtra("user", user)
                        val currentDate = Date()
                        val dateFormat: DateFormat = SimpleDateFormat("MM", Locale.getDefault())
                        val dateText: String = dateFormat.format(currentDate)
                        val listResponse = when (text.text.toString().split(" ")[0]) {
                            "Year" -> SelectTime.YEAR.DurationList(map, listYear[h!!.x.toInt()])
                            "Season" -> SelectTime.SEASON.DurationList(map, listSeason[h!!.x.toInt()])
                            "Month" -> SelectTime.MONTHS.DurationList(map,
                                dateText + " " + listMonth[h!!.x.toInt()])
                            else -> SelectTime.DAYS.DurationList(map,
                                dateText + " " + text.text.toString())
                        }
                        intent.putExtra("DataValue", listResponse.toTypedArray())
                        startActivity(intent)
                    }

                    override fun onNothingSelected() {

                    }

                })
            }
        }
    }

