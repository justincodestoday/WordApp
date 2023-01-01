package com.example.wordapp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wordapp.models.Word
import com.example.wordapp.repository.WordRepository

class AddWordViewModel(private val repo: WordRepository) : ViewModel() {
    fun addWord(word: Word) {
        repo.addWord(word)
    }

    class Provider(private val repo: WordRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AddWordViewModel(repo) as T
        }
    }
}