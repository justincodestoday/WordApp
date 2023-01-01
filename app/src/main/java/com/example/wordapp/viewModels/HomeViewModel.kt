package com.example.wordapp.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel: ViewModel() {
    val refreshAllWords: MutableLiveData<Boolean> = MutableLiveData(false)
    val refreshCompletedWords: MutableLiveData<Boolean> = MutableLiveData(false)

    fun doRefreshAllWords(refresh: Boolean) {
        refreshAllWords.value = refresh
    }

    fun doRefreshCompletedWords(refresh: Boolean) {
        refreshCompletedWords.value = refresh
    }
}