package com.example.githubapp.ui.repo


interface ForSelectTime{
    fun DurationList(map: Map<String, String>, data: String): Map<Int, String>
}

enum class SelectTime: ForSelectTime {
    YEAR{
        override fun DurationList(map: Map<String, String>, data: String): Map<Int, String> {
            val map1 = mutableMapOf<Int, String>()
            val season = when(data){
                "Spring" ->3..5
                "Summer" ->6..8
                "Autumn" ->9..11
                else -> listOf(12, 1, 2)
            }
            map.keys.forEach {
                if(it.substring(5..6).toInt() in season){
                    map1[it.substring(0..3).toInt()]=map[it]!!
                }
            }
            return map1
        }

    },
    SEASON{
        override fun DurationList(map: Map<String, String>, data: String): Map<Int, String> {
            val map1 = mutableMapOf<Int, String>()
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
                if(it.substring(5..6).toInt() == season) map1[it.substring(0..3).toInt()]=map[it]!!
            }
            return map1
        }

    },
    MONTHS{
        override fun DurationList(map: Map<String, String>, data: String): Map<Int, String> {
            val map1 = mutableMapOf<Int, String>()
            val season = when(data){
                "One" ->1
                "Two" ->2
                "Three" ->3
                else ->4
            }
            map.keys.forEach {
                if(it.substring(8..9).toInt() in listOf(29, 30, 31) || it.substring(8..9).toInt() == season) map1[it.substring(0..3).toInt()]=map[it]!!
            }
            return map1
        }
    }
}