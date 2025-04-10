package com.example.carousellistapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.example.carousellistapp.ui.theme.CarouselListAppTheme

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

@Composable
fun CarouselListScreen() {
    val images = remember {
        listOf(R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4, R.drawable.image5)
    }

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { images.size })
    var searchQuery by remember { mutableStateOf("") }

    val currentPage = pagerState.currentPage

    val listItems = remember(currentPage, searchQuery) {
        List(25) { "Item ${it + 1} Page $currentPage" }
            .filter { it.contains(searchQuery, ignoreCase = true) }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        // Carousel
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) { page ->
            Card( // ✅ Wrap image in Card to add corner and shadow
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Image(
                    painter = painterResource(id = images[page]),
                    contentDescription = "Image $page",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // ✅ Dots below the carousel
        PagerIndicator(size = images.size, currentPage = currentPage)

        Spacer(modifier = Modifier.height(8.dp))

        SearchBar(query = searchQuery, onQueryChange = { searchQuery = it })

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(listItems) { item ->
                ListItem(title = item, subtitle = "Subtitle")
            }
        }

        FloatingActionButton(
            onClick = { /* Show bottom sheet */ },
            modifier = Modifier.align(Alignment.End).padding(top = 8.dp)
        ) {
            Icon(Icons.Default.MoreVert, contentDescription = "Statistics")
        }
    }
}

@Composable
fun ImageCarousel(images: List<Int>, onPageChanged: (Int) -> Unit) {
    // Placeholder for carousel. Replace with HorizontalPager or custom logic.
    Box(modifier = Modifier.height(200.dp).fillMaxWidth().background(Color.Gray), contentAlignment = Alignment.Center) {
        Image(painter = painterResource(id = images[0]), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
    }
}

@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text("Search") },
        shape = RoundedCornerShape(16.dp), // ✅ Round the edges
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(16.dp))
    )
}
@Composable
fun ListItem(title: String, subtitle: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.LightGray, shape = CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, style = MaterialTheme.typography.titleMedium)
                Text(text = subtitle, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
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
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .padding(horizontal = 4.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}
