package com.example.githubapp.ui.repo

import Save_Data.AppDatabase
import Save_Data.Repository
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
import java.util.Calendar
import java.util.Date
import java.util.Locale


class RepoActivity : AppCompatActivity() {

    public fun getContext(): Context {
        return this
    }

    private fun Year(map: MutableMap<String, String>, isYear: Int): MutableList<Int> {
        var listNum = mutableListOf<Int>()
        listNum += SelectTime.YEAR.DurationList(map, "Winter").filter{ it.key==isYear }.size
        listNum += SelectTime.YEAR.DurationList(map, "Spring").filter{ it.key==isYear }.size
        listNum += SelectTime.YEAR.DurationList(map, "Summer").filter{ it.key==isYear }.size
        listNum += SelectTime.YEAR.DurationList(map, "Autumn").filter{ it.key==isYear }.size
        return listNum
    }

    private fun Season(map: MutableMap<String, String>, isYear: Int): MutableList<Int> {
        var listNum = mutableListOf<Int>()
        listNum += SelectTime.SEASON.DurationList(map, "January").filter{ it.key==isYear }.size
        listNum += SelectTime.SEASON.DurationList(map, "February").filter{ it.key==isYear }.size
        listNum += SelectTime.SEASON.DurationList(map, "March").filter{ it.key==isYear }.size
        listNum += SelectTime.SEASON.DurationList(map, "April").filter{ it.key==isYear }.size
        listNum += SelectTime.SEASON.DurationList(map, "May").filter{ it.key==isYear }.size
        listNum += SelectTime.SEASON.DurationList(map, "June").filter{ it.key==isYear }.size
        listNum += SelectTime.SEASON.DurationList(map, "Jule").filter{ it.key==isYear }.size
        listNum += SelectTime.SEASON.DurationList(map, "August").filter{ it.key==isYear }.size
        listNum += SelectTime.SEASON.DurationList(map, "September").filter{ it.key==isYear }.size
        listNum += SelectTime.SEASON.DurationList(map, "October").filter{ it.key==isYear }.size
        listNum += SelectTime.SEASON.DurationList(map, "November").filter{ it.key==isYear }.size
        listNum += SelectTime.SEASON.DurationList(map, "December").filter{ it.key==isYear }.size
        return listNum
    }

    private fun Month(map: MutableMap<String, String>, isYear: Int): MutableList<Int> {
        var listNum = mutableListOf<Int>()
        val currentDate = Date()
        val dateFormat: DateFormat = SimpleDateFormat("MM", Locale.getDefault())
        val dateText: String = dateFormat.format(currentDate)
        listNum += SelectTime.MONTHS.DurationList(map, "$dateText First").filter{ it.key==isYear }.size
        listNum += SelectTime.MONTHS.DurationList(map, "$dateText Second").filter{ it.key==isYear }.size
        listNum += SelectTime.MONTHS.DurationList(map, "$dateText Third").filter{ it.key==isYear }.size
        listNum += SelectTime.MONTHS.DurationList(map, "$dateText Forth").filter{ it.key==isYear }.size
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

            var isYear = Calendar.getInstance().get(Calendar.YEAR)
            val title1 = intent.getStringExtra("title")!!
            val user = intent.getStringExtra("user")!!
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
                                obj.stargazers_count,
                                !obj.favourite
                            )
                        )
                }
            }

            var map = loadTimeStarring(user, title1, employeeDao.selectStarWR(title1))
            if(map.isEmpty()){
                repeat(4){
                    map+=Pair("", "")
                }
            }

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

            val list_for_star = when (text) {
                R.id.buttonOfYear -> Year(map, isYear)
                R.id.buttonOfSeason -> Season(map, isYear)
                else -> Month(map, isYear)
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
            barDataSet.label = isYear.toString()
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
                    intent.putExtra("DataValue", listResponse.filter{ it.key == isYear }.values.toTypedArray())
                    println(listResponse)
                    startActivity(intent)
                }

                override fun onNothingSelected() {

                }

            })

            button6.setOnClickListener {
                val entries = mutableListOf<BarEntry>()
                var count = 0f

                val text = radioGroup.checkedRadioButtonId

                val list_for_star = when (text) {
                    R.id.buttonOfYear -> Year(map, isYear)
                    R.id.buttonOfSeason -> Season(map, isYear)
                    else -> Month(map, isYear)
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
                        intent.putExtra("DataValue", listResponse.filter{ it.key == isYear }.values.toTypedArray())
                        println("ListResponse: " + listResponse)
                        startActivity(intent)
                    }

                    override fun onNothingSelected() {

                    }

                })
            }

            val min = findViewById<MaterialButton>(R.id.min)
            min.setOnClickListener {

                isYear-=1

                val entries = mutableListOf<BarEntry>()
                var count = 0f

                val text = radioGroup.checkedRadioButtonId

                val list_for_star = when (text) {
                    R.id.buttonOfYear -> Year(map, isYear)
                    R.id.buttonOfSeason -> Season(map, isYear)
                    else -> Month(map, isYear)
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
                        intent.putExtra("DataValue", listResponse.filter{ it.key == isYear }.values.toTypedArray())
                        println(isYear)
                        startActivity(intent)
                    }

                    override fun onNothingSelected() {

                    }

                })
            }

            val plus = findViewById<MaterialButton>(R.id.plus)
            plus.setOnClickListener {

                isYear+=1

                val entries = mutableListOf<BarEntry>()
                var count = 0f

                val text = radioGroup.checkedRadioButtonId

                val list_for_star = when (text) {
                    R.id.buttonOfYear -> Year(map, isYear)
                    R.id.buttonOfSeason -> Season(map, isYear)
                    else -> Month(map, isYear)
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
                        intent.putExtra("DataValue", listResponse.filter{ it.key == isYear }.values.toTypedArray())
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

