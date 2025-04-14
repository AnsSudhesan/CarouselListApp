package com.example.carousellistapp.data

data class CarouselListUiState(val pages: List<Page> = emptyList(),
                               val currentPage: Int = 0,
                               val currentPageItems: List<ListDisplayItem> = emptyList(),
                               val searchQuery: String = "")
