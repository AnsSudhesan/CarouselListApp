package com.example.carousellistapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.carousellistapp.data.CarouselListUiState
import com.example.carousellistapp.data.ListDisplayItem
import com.example.carousellistapp.data.StatisticsData
import com.example.carousellistapp.model.SampleData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CarouselListViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CarouselListUiState())
    val uiState: StateFlow<CarouselListUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        _uiState.update { currentState ->
            currentState.copy(
                pages = SampleData.pages,
                currentPageItems = SampleData.pages[0].items.map {
                    ListDisplayItem(it, SampleData.pages[0].imageResId)
                }
            )
        }
    }
    // Update current page when user swipes carousel
    fun updateCurrentPage(pageIndex: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                currentPage = pageIndex,
                currentPageItems = currentState.pages[pageIndex].items.map {
                    ListDisplayItem(it, currentState.pages[pageIndex].imageResId)
                },
                searchQuery = ""
            )
        }
    }
    // Handle search query changes
    fun updateSearchQuery(query: String) {
        _uiState.update { currentState ->
            val filtered = currentState.pages[currentState.currentPage]
                .items
                .filter { it.contains(query, ignoreCase = true) }
                .map {
                    ListDisplayItem(it, currentState.pages[currentState.currentPage].imageResId)
                }

            currentState.copy(
                searchQuery = query,
                currentPageItems = filtered
            )
        }
    }
    // Generate statistics data for bottom sheet
    fun getStatistics(): StatisticsData {
        val currentItems = _uiState.value.currentPageItems.map { it.title }
        val charFrequency = currentItems.joinToString("")
            .lowercase()
            .filter { it.isLetter() }
            .groupingBy { it }
            .eachCount()
            .entries
            .sortedByDescending { it.value }
            .take(3)

        return StatisticsData(
            pageIndex = _uiState.value.currentPage,
            itemCount = currentItems.size,
            topCharacters = charFrequency
        )
    }
}