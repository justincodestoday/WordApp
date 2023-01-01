package com.example.wordapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wordapp.databinding.ItemLayoutWordBinding
import com.example.wordapp.models.Word
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class WordAdapter(private var items: List<Word>, val onClick: (word: Word) -> Unit) :
    RecyclerView.Adapter<WordAdapter.WordHolder>() {
    inner class WordHolder(val binding: ItemLayoutWordBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val item = items[adapterPosition]
            binding.run {
                tvWord.text = item.word
                tvMeaning.text = item.meaning
                cvWord.setOnClickListener {
                    onClick(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordHolder {
        val binding =
            ItemLayoutWordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WordHolder(binding)
    }

    override fun onBindViewHolder(holder: WordHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setWords(items: List<Word>) {
        this.items = items
        notifyDataSetChanged()
    }
}