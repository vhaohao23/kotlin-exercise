package com.example.lab5.data

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.example.lab5.R

data class Dog(
    val avatarColor: Color,
    @StringRes val name: Int,
    val age: Int,
    @StringRes val hobbies: Int
)

val dogs = listOf(
    Dog(Color(0xFFF4A460), R.string.dog_name_1, 2, R.string.dog_description_1),
    Dog(Color(0xFFC0C0C0), R.string.dog_name_2, 16, R.string.dog_description_2),
    Dog(Color(0xFF8B4513), R.string.dog_name_3, 2, R.string.dog_description_3),
    Dog(Color(0xFFDEB887), R.string.dog_name_4, 8, R.string.dog_description_4),
    Dog(Color(0xFFFFFACD), R.string.dog_name_5, 5, R.string.dog_description_5),
    Dog(Color(0xFFE8D87A), R.string.dog_name_6, 2, R.string.dog_description_6),
    Dog(Color(0xFF708090), R.string.dog_name_7, 2, R.string.dog_description_7),
    Dog(Color(0xFFFFDAB9), R.string.dog_name_8, 7, R.string.dog_description_8),
    Dog(Color(0xFFCD853F), R.string.dog_name_9, 1, R.string.dog_description_9),
)
