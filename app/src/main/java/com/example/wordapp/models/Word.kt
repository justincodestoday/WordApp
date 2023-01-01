package com.example.wordapp.models

data class Word(
    val id: Long? = null,
    val word: String,
    val meaning: String,
    val synonym: String,
    val example: String,
    val date: String,
    var status: Boolean = false
)
