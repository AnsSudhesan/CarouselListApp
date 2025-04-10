package com.example.carousellistapp.data

data class Page(
    val imageResId: Int,           // for local image resource (XML)
    val imageUrl: String?,        // for network or Coil
    val image: Int,
    val items: List<String>
)
