package com.example.wordapp.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wordapp.models.Word
import com.example.wordapp.repository.WordRepository

class EditWordViewModel(private val repo: WordRepository): ViewModel() {
    val word: MutableLiveData<Word> = MutableLiveData()

    fun getWordById(id: Long) {
        val res = repo.getWordById(id)
        res?.let {
            word.value = it
        }
    }

    fun editWord(id: Long, word: Word) {
        repo.editWord(id, word)
    }

    class Provider(private val repo: WordRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return EditWordViewModel(repo) as T
        }
    }
}