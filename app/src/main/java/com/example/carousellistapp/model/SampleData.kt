package com.example.carousellistapp.model

import com.example.carousellistapp.R
import com.example.carousellistapp.data.Page

object SampleData {
    val pages = listOf(
        Page(
            imageResId = R.drawable.image1,
            imageUrl = null,
            image = R.drawable.image1,
            items = listOf(
                "apple",
                "banana",
                "cherry",
                "date",
                "elderberry",
                "quince",
                "raspberry",
                "strawberry",
                "tangerine",
                "ugli fruit"
            )
        ),
        Page(
            imageResId = R.drawable.image2,
            imageUrl = null,
            image = R.drawable.image2,
            items = listOf(
                "fig",
                "grape",
                "honeydew",
                "jackfruit",
                "kiwi",
                "vanilla bean",
                "watermelon",
                "xigua ",
                "yellow passion fruit",
                "zucchini "
            )
        ),
        Page(
            imageResId = R.drawable.image3,
            imageUrl = null,
            image = R.drawable.image3,
            items = listOf(
                "lemon",
                "mango",
                "nectarine",
                "orange",
                "papaya",
                "blackberry",
                "cantaloupe",
                "dragonfruit",
                "gooseberry",
                "lychee"
            )
        ),
        Page(
            imageResId = R.drawable.image4,
            imageUrl = null,
            image = R.drawable.image4,
            items = listOf(
                "mulberry",
                "olive",
                "peach",
                "plum",
                "pineapple",
                "rambutan",
                "soursop",
                "starfruit",
                "tamarind",
                "boysenberry"
            )
        ),
        Page(
            imageResId = R.drawable.image5,
            imageUrl = null,
            image = R.drawable.image5,
            items = listOf(
                "coconut",
                "cranberry",
                "currant",
                "jabuticaba",
                "kumquat",
                "longan",
                "miracle fruit",
                "persimmon",
                "sapodilla",
                "white currant"
            )
        )
    )
}