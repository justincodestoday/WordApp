package com.example.wordapp.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wordapp.models.Word
import com.example.wordapp.repository.WordRepository

class AllWordsViewModel(private val repo: WordRepository) : ViewModel() {
    val words: MutableLiveData<List<Word>> = MutableLiveData()

    init {
        getWords("")
    }

    fun getWords(str: String) {
        val res = repo.getWords(str, false)
        words.value = res.filter { !it.status }
    }

//    fun sortWords(order: String, category: String) {
//        val res = repo.sortWords(order, category)
//        words.value = res.filter { !it.status }
//    }

    fun sortWords(order: String, category: String, str: String) {
        var res = repo.getWords(str, false)
        if (order == "Ascending" && category == "Word") {
            res = res.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) {
                it.word
            })
        } else if (order == "Descending" && category == "Word") {
            res = res.sortedWith(compareByDescending(String.CASE_INSENSITIVE_ORDER) {
                it.word
            })
        } else if (order == "Descending" && category == "Date") {
            res = res.reversed()
        }
        words.value = res.filter { !it.status }
    }

    class Provider(private val repo: WordRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AllWordsViewModel(repo) as T
        }
    }
}