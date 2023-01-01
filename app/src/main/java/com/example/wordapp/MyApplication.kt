package com.example.wordapp

import android.app.Application
import com.example.wordapp.repository.WordRepository

class MyApplication: Application() {
    val wordRepo = WordRepository.getInstance()
}