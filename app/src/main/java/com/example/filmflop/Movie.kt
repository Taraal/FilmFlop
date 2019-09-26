package com.example.filmflop

import java.util.*

data class Movie (
    val name: String,
    val release: Date,
    val description: String,
    val rating: Double,
    val duration: Integer,
    val adult: Boolean,
    val lastCheck: Date
)
