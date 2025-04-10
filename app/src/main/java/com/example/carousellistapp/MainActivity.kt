package com.example.carousellistapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.carousellistapp.model.SampleData
import com.example.carousellistapp.ui.theme.CarouselListAppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CarouselListAppTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    CarouselListScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CarouselListScreen() {
    val pages = remember { SampleData.pages }
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { pages.size })
    val currentPage = pagerState.currentPage

    var searchQuery by remember { mutableStateOf("") }

    val listItems = remember(currentPage, searchQuery) {
        pages[currentPage].items
            .filter { it.contains(searchQuery, ignoreCase = true) }
            .map { ListDisplayItem(title = it, imageResId = pages[currentPage].image) }
    }

    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {

            item {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) { pageIndex ->
                    val page = pages[pageIndex]
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Image(
                            painter = painterResource(id = page.imageResId),
                            contentDescription = "Image $pageIndex",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                PagerIndicator(size = pages.size, currentPage = currentPage)
                Spacer(modifier = Modifier.height(8.dp))
            }

            stickyHeader {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    SearchBar(
                        query = searchQuery,
                        onQueryChange = { searchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(52.dp)
                    )
                }
            }

            items(listItems) { item ->
                ListItem(title = item.title, subtitle = "Subtitle", imageResId = item.imageResId)
            }

            item {
                Spacer(modifier = Modifier.height(72.dp))
            }
        }

        FloatingActionButton(
            onClick = {
                scope.launch { bottomSheetState.show() }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.MoreVert, contentDescription = "Statistics")
        }
    }

    if (bottomSheetState.isVisible) {
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch { bottomSheetState.hide() }
            },
            sheetState = bottomSheetState
        ) {
            StatisticsContent(items = listItems, pageIndex = currentPage)
        }
    }
}

@Composable
fun ListItem(
    title: String,
    subtitle: String,
    @DrawableRes imageResId: Int
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(text = title, style = MaterialTheme.typography.titleMedium)
                Text(text = subtitle, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text("Search") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon"
            )
        },
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .height(56.dp)
            .background(Color.White, shape = RoundedCornerShape(16.dp)),
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyLarge,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.Gray,
            focusedBorderColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
fun PagerIndicator(size: Int, currentPage: Int) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        repeat(size) { index ->
            val color = if (index == currentPage) Color.DarkGray else Color.LightGray
            val width = if (index == currentPage) 18.dp else 18.dp // active indicator longer
            val shape = RoundedCornerShape(50)

            Box(
                modifier = Modifier
                    .height(10.dp)
                    .width(width)
                    .padding(horizontal = 4.dp)
                    .clip(shape)
                    .background(color)
            )
        }
    }
}

// ✅ Updated for ListDisplayItem
@Composable
fun StatisticsContent(items: List<ListDisplayItem>, pageIndex: Int) {
    val charFrequency = remember(items) {
        items.joinToString("") { it.title }
            .lowercase()
            .filter { it.isLetter() }
            .groupingBy { it }
            .eachCount()
            .entries
            .sortedByDescending { it.value }
            .take(3)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("List ${pageIndex + 1} (${items.size} items)", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Top 3 Characters:")
        charFrequency.forEach {
            Text("${it.key} = ${it.value}")
        }
    }
}

data class ListDisplayItem(val title: String, @DrawableRes val imageResId: Int)
