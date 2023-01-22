package com.example.githubapp.ui.repo

import java.util.*


interface ForSelectTime{
    fun DurationList(map: Map<String, String>, data: String): List<String>
}

enum class SelectTime: ForSelectTime {
    YEAR{
        override fun DurationList(map: Map<String, String>, data: String): List<String> {
            val list = mutableListOf<String>()
            val season = when(data){
                "Spring" ->3..5
                "Summer" ->6..8
                "Autumn" ->9..11
                else -> listOf(12, 1, 2)
            }
            map.keys.forEach {
                if(it.substring(5..6).toInt() in season){
                    list+=map[it]!!
                }
            }
            return list
        }

    },
    SEASON{
        override fun DurationList(map: Map<String, String>, data: String): List<String> {
            val list = mutableListOf<String>()
            val season = when(data){
                "January" ->1
                "February" ->2
                "March" ->3
                "April" ->4
                "May" ->5
                "June" ->6
                "Jule" ->7
                "August" ->8
                "September" ->9
                "October" ->10
                "November" ->11
                else -> 12
            }
            map.keys.forEach {
                if(it.substring(5..6).toInt() == season) list+=map[it]!!
            }
            return list
        }

    },
    MONTHS{
        override fun DurationList(map: Map<String, String>, data: String): List<String> {
            val list = mutableListOf<String>()
            val seasons = when(data.split(" ")[0]){
                "Jan" ->1
                "Feb" ->2
                "Mar" ->3
                "Apr" ->4
                "May" ->5
                "Jun" ->6
                "Jul" ->7
                "Aug" ->8
                "Sep" ->9
                "Oct" ->10
                "Nov" ->11
                else -> 12
            }
            val season = when(data.split(" ")[1]){
                "One" ->1..7
                "Two" ->8..14
                "Three" ->15..21
                "For" -> 22..28
                else -> 29..31
            }
            map.keys.forEach {
                if(it.substring(8..9).toInt() in season && seasons==it.substring(5..6).toInt()) list+=map[it]!!
            }
            return list
        }

    },
    DAYS{
        override fun DurationList(map: Map<String, String>, data: String): List<String> {
            val list = mutableListOf<String>()
            val list1: List<Int> = data.split(" ").map { it.toInt() }
            map.keys.forEach {
                if(it.substring(8..9).toInt() in list1) {
                    list+=map[it]!!
                }
            }
            return list
        }

    }
}
