package com.example.carousellistapp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import com.example.carousellistapp.ui.screens.CarouselListScreen
import com.example.carousellistapp.ui.theme.CarouselListAppTheme
import com.example.carousellistapp.viewmodel.CarouselListViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: CarouselListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CarouselListAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CarouselListScreen(viewModel)
                }
            }
        }
    }

}