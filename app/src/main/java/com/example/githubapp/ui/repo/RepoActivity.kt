package com.example.githubapp.ui.repo

import Save_Data.AppDatabase
import Save_Data.Repository
import Save_Data.Star
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.room.Room
import com.example.githubapp.R
import com.example.githubapp.Saved.loadTimeStarring
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
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class RepoActivity : AppCompatActivity() {

    public fun getContext(): Context {
        return this
    }

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

    @RequiresApi(Build.VERSION_CODES.M)
    fun makerColor(bool: Boolean, starButton: MaterialButton) {
        starButton.foregroundTintList = when(bool){
            true ->
                ColorStateList.valueOf(resources.getColor(R.color.white))
            else ->
                ColorStateList.valueOf(resources.getColor(R.color.black))
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.repo)

        GlobalScope.launch(Dispatchers.Main) {

            onNewIntent(intent)

            val extras = intent.extras!!
            val title1 = extras.getString("title")!!
            val user = extras.getString("user")!!
            val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
            val barChart: BarChart = findViewById(R.id.idBarChart)
            val button6 = findViewById<Button>(R.id.button6)
            var intent: Intent
            val textTitle = findViewById<TextView>(R.id.barText)
            textTitle.text = title1

            val employeeDao = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "Star"
            ).build().employeeDao()

            val star2 = findViewById<MaterialButton>(R.id.star2)
            makerColor(
                employeeDao
                    .selectRepo(
                        title1
                    )
                    .favourite,
                star2
            )
            star2.setOnClickListener {
                GlobalScope.launch(Dispatchers.Main) {
                    var obj = employeeDao.selectRepo(title1)
                    makerColor(!obj.favourite, star2)
                    employeeDao
                        .updateRepository(
                            Repository(
                                obj.id,
                                obj.name,
                                obj.ownerName,
                                !obj.favourite
                            )
                        )
                }
            }

            var map = loadTimeStarring(user, title1, employeeDao.selectStarWR(title1))

            findViewById<Toolbar>(R.id.head).setOnClickListener {
                intent = Intent(this@RepoActivity, SelectRepoActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                startActivity(intent)
            }
            val listYear = listOf("Winter", "Spring", "Summer", "Autumn")
            val listSeason = listOf(
                "January",
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
                "December"
            )
            val listMonth = listOf("First", "Second", "Third", "Forth")
            val listFreeTime = listOf("Result")

            val buttonOfYear = findViewById<RadioButton>(R.id.buttonOfYear)
            buttonOfYear.isChecked = true

            val entries = mutableListOf<BarEntry>()
            var count = 0f

            val text = radioGroup.checkedRadioButtonId

            var list_for_star = when (text) {
                R.id.buttonOfYear -> Year(map)
                R.id.buttonOfSeason -> Season(map)
                else -> Month(map)
            }

            var list = when (text) {
                R.id.buttonOfYear -> listYear
                R.id.buttonOfSeason -> listSeason
                else -> listMonth
            }
            list_for_star.forEach { value ->
                entries += BarEntry(count, value.toFloat())
                count += 1
            }
            println("Error: ${list_for_star}")

            val barDataSet = BarDataSet(entries, title1)
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

            if (list != listFreeTime) {
                xAxis.valueFormatter = IndexAxisValueFormatter(list)
                xAxis.labelCount = list.size - 1
                xAxis.labelRotationAngle = 90f
            } else {
                xAxis.isEnabled = false
            }

            val data = BarData(barDataSet)
            barChart.data = data
            barDataSet.colors = listOf(
                resources.getColor(R.color.teal_200),
                resources.getColor(R.color.purple_200),
                resources.getColor(R.color.redFull),
                resources.getColor(R.color.purple_700)
            )

            barChart.invalidate()
            barChart.refreshDrawableState()

            barChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                override fun onValueSelected(e: Entry?, h: Highlight?) {
                    intent = Intent(this@RepoActivity, UsersActivity::class.java)
                    intent.putExtra("title", title1)
                    intent.putExtra("user", user)
                    val currentDate = Date()
                    val dateFormat: DateFormat = SimpleDateFormat("MM", Locale.getDefault())
                    val dateText: String = dateFormat.format(currentDate)
                    val listResponse = when (text) {
                        R.id.buttonOfYear -> SelectTime.YEAR.DurationList(
                            map,
                            listYear[h!!.x.toInt()]
                        )

                        R.id.buttonOfSeason -> SelectTime.SEASON.DurationList(
                            map,
                            listSeason[h!!.x.toInt()]
                        )

                        else -> SelectTime.MONTHS.DurationList(
                            map,
                            dateText + " " + listMonth[h!!.x.toInt()]
                        )
                    }
                    intent.putExtra("DataValue", listResponse.toTypedArray())
                    startActivity(intent)
                }

                override fun onNothingSelected() {

                }

            })

            button6.setOnClickListener {
                val entries = mutableListOf<BarEntry>()
                var count = 0f

                val text = radioGroup.checkedRadioButtonId

                var list_for_star = when (text) {
                    R.id.buttonOfYear -> Year(map)
                    R.id.buttonOfSeason -> Season(map)
                    else -> Month(map)
                }

                var list = when (text) {
                    R.id.buttonOfYear -> listYear
                    R.id.buttonOfSeason -> listSeason
                    else -> listMonth
                }
                list_for_star.forEach { value ->
                    entries += BarEntry(count, value.toFloat())
                    count += 1
                }
                println("Error: ${list_for_star}")

                val barDataSet = BarDataSet(entries, title1)
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

                if (list != listFreeTime) {
                    xAxis.valueFormatter = IndexAxisValueFormatter(list)
                    xAxis.labelCount = list.size - 1
                    xAxis.labelRotationAngle = 90f
                } else {
                    xAxis.isEnabled = false
                }

                val data = BarData(barDataSet)
                barChart.data = data
                barDataSet.colors = listOf(
                    resources.getColor(R.color.teal_200),
                    resources.getColor(R.color.purple_200),
                    resources.getColor(R.color.redFull),
                    resources.getColor(R.color.purple_700)
                )

                barChart.invalidate()
                barChart.refreshDrawableState()

                barChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                    override fun onValueSelected(e: Entry?, h: Highlight?) {
                        intent = Intent(this@RepoActivity, UsersActivity::class.java)
                        intent.putExtra("title", title1)
                        intent.putExtra("user", user)
                        val currentDate = Date()
                        val dateFormat: DateFormat = SimpleDateFormat("MM", Locale.getDefault())
                        val dateText: String = dateFormat.format(currentDate)
                        val listResponse = when (text) {
                            R.id.buttonOfYear -> SelectTime.YEAR.DurationList(
                                map,
                                listYear[h!!.x.toInt()]
                            )

                            R.id.buttonOfSeason -> SelectTime.SEASON.DurationList(
                                map,
                                listSeason[h!!.x.toInt()]
                            )

                            else -> SelectTime.MONTHS.DurationList(
                                map,
                                dateText + " " + listMonth[h!!.x.toInt()]
                            )
                        }
                        intent.putExtra("DataValue", listResponse.toTypedArray())
                        println("ListResponse: " + listResponse)
                        startActivity(intent)
                    }

                    override fun onNothingSelected() {

                    }

                })
            }
        }
    }
}

