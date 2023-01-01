package com.example.wordapp.repository

import com.example.wordapp.models.Word

class WordRepository {
    private var counter = -1L
    private val wordMap: MutableMap<Long, Word> = mutableMapOf()

    fun getWords(str: String, status: Boolean = false): List<Word> {
        return wordMap.filter { (key, value) ->
            Regex(
                str,
                RegexOption.IGNORE_CASE
            ).containsMatchIn(value.word) && value.status == status
        }.values.toList()
    }

    fun getWordById(id: Long): Word? {
        return wordMap[id]
    }

    fun addWord(word: Word): Word? {
        wordMap[++counter] = word.copy(id = counter)
        return wordMap[counter]
    }

    fun changeWordStatus(id: Long): Word? {
        wordMap[id]?.status = !wordMap[id]?.status!!
        return wordMap[id]
    }

    fun editWord(id: Long, word: Word): Word? {
        wordMap[id] = word
        return wordMap[id]
    }

    fun deleteWord(id: Long) {
        wordMap.remove(id)
    }

//    fun sortWords(order: String, category: String): List<Word> {
//        if (order == "Ascending" && category == "Word") {
//            val res = wordMap.values.toList()
//            return res.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.word })
//        } else if (order == "Descending" && category == "Word") {
//            val res = wordMap.values.toList()
//            return res.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.word }).reversed()
//        } else if (order == "Descending" && category == "Date") {
//            return wordMap.values.toList().reversed()
//        }
//        return wordMap.values.toList()
//    }

    companion object {
        private var wordRepository: WordRepository? = null
        fun getInstance(): WordRepository {
            if (wordRepository == null) {
                wordRepository = WordRepository()
            }

            return wordRepository!!
        }
    }
}