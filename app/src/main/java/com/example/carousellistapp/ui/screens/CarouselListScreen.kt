package com.example.carousellistapp.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.carousellistapp.data.Page
import com.example.carousellistapp.util.Dimens
import com.example.carousellistapp.viewmodel.CarouselListViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CarouselListScreen(
    viewModel: CarouselListViewModel = viewModel()
) {
    // Collect UI state from ViewModel
    val uiState by viewModel.uiState.collectAsState()
    // Pager state for horizontal carousel
    val pagerState = rememberPagerState(
        initialPage = uiState.currentPage,
        pageCount = { uiState.pages.size }
    )
    // Bottom sheet state (collapsed/expanded)
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    // Update ViewModel when page changes
    LaunchedEffect(pagerState.currentPage) {
        viewModel.updateCurrentPage(pagerState.currentPage)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Main content list
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = Dimens.PaddingExtraLarge)
        ) {
            // Carousel section
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(Dimens.CarouselHeight)
                            .padding(horizontal = Dimens.PaddingSmall),
                    ) { pageIndex ->
                        CarouselItem(page = uiState.pages[pageIndex])
                    }
                  // Page indicator dots
                    PagerIndicator(
                        pageCount = uiState.pages.size,
                        currentPage = pagerState.currentPage,
                        modifier = Modifier.padding(vertical = Dimens.PaddingSmall)
                    )
                }
            }
            // Sticky search header
            stickyHeader {
                SearchHeader(
                    query = uiState.searchQuery,
                    onQueryChange = { viewModel.updateSearchQuery(it) }
                )
            }

            items(uiState.currentPageItems) { item ->
                ListItem(
                    title = item.title,
                    subtitle = "Page ${uiState.currentPage + 1}",
                    imageResId = item.imageResId,
                    modifier = Modifier.padding(
                        horizontal = Dimens.PaddingLarge,
                        vertical = Dimens.PaddingExtraSmall
                    )
                )
            }

            item { Spacer(modifier = Modifier.height(Dimens.FabSize)) }
        }
        // Floating action button for showing stats
        FloatingActionButton(
            onClick = { scope.launch { bottomSheetState.show() } },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(Dimens.PaddingLarge)
        ) {
            Icon(Icons.Default.MoreVert, contentDescription = "Statistics")
        }
    }
    // Show bottom sheet when visible
    if (bottomSheetState.isVisible) {
        StatisticsBottomSheet(
            viewModel = viewModel,
            sheetState = bottomSheetState,
            onDismiss = { scope.launch { bottomSheetState.hide() } }
        )
    }
}
// Component Breakdown:

@Composable
private fun CarouselItem(page: Page) {
    Card(
        shape = RoundedCornerShape(Dimens.ShapeMedium),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Dimens.PaddingLarge),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.ElevationSmall)
    ) {
        Image(
            painter = painterResource(id = page.imageResId),
            contentDescription = "Carousel image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun PagerIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxWidth()
    ) {
        repeat(pageCount) { index ->
            val color = if (index == currentPage) Color.DarkGray else Color.LightGray
            val width = if (index == currentPage) Dimens.PagerIndicatorMedium else Dimens.PagerIndicatorMedium // active indicator longer
            val shape = RoundedCornerShape(50)
            Box(
                modifier = Modifier
                    .height(Dimens.IndicatorDotSize)
                    .width(width)
                    .padding(horizontal = Dimens.SpaceExtraSmall)
                    .clip(shape)
                    .background(color)
            )
        }
    }
}
// Sticky search header component
@Composable
private fun SearchHeader(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        tonalElevation = Dimens.ElevationSmall,
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center

        ) {
            SearchBar(
                query = query,
                onQueryChange = onQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.PaddingExtraSmall)
            )
        }
    }
}
// Single list item component (card with image and text)
@Composable
private fun ListItem(
    title: String,
    subtitle: String,
    @DrawableRes imageResId: Int,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(Dimens.ShapeMedium),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.PaddingExtraSmall)
    ) {
        Row(
            modifier = Modifier.padding(Dimens.PaddingMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(Dimens.ListItemImageSize)
                    .clip(RoundedCornerShape(Dimens.ShapeSmall))
            )
            Spacer(modifier = Modifier.width(Dimens.PaddingMedium))
            Column {
                Text(text = title, style = MaterialTheme.typography.titleMedium)
                Text(text = subtitle, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
// Statistics bottom sheet component
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsBottomSheet(
    viewModel: CarouselListViewModel,
    sheetState: SheetState,  // Correct parameter
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        val stats = viewModel.getStatistics()
        Column(modifier = Modifier.padding(Dimens.SpaceMedium)) {
            Text(
                text = "List ${stats.pageIndex + 1} (${stats.itemCount} items)",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(Dimens.PaddingSmall))
            Text("Top 3 Characters:", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(Dimens.PaddingExtraSmall))
            stats.topCharacters.forEach { (char, count) ->
                Text(
                    text = "$char = $count",
                    modifier = Modifier.padding(vertical = Dimens.StatisticsBottomSheetSmall)
                )
            }
        }
    }
}


