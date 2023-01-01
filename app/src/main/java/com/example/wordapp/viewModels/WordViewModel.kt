package com.example.wordapp.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wordapp.models.Word
import com.example.wordapp.repository.WordRepository

class WordViewModel(private val repo: WordRepository) : ViewModel() {
    val word: MutableLiveData<Word> = MutableLiveData()

    fun getWordById(id: Long) {
        val res = repo.getWordById(id)
        res?.let {
            word.value = it
        }
    }

    fun changeWordStatus(id: Long) {
        repo.changeWordStatus(id)
    }

    fun deleteWord(id: Long) {
        repo.deleteWord(id)
    }

    class Provider(private val repo: WordRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return WordViewModel(repo) as T
        }
    }
}