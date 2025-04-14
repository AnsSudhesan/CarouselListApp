package com.example.carousellistapp.data

data class StatisticsData(val pageIndex: Int,
                          val itemCount: Int,
                          val topCharacters: List<Map.Entry<Char, Int>>)
