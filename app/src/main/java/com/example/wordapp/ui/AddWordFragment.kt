package com.example.wordapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.example.wordapp.MyApplication
import com.example.wordapp.R
import com.example.wordapp.databinding.FragmentAddEditWordBinding
import com.example.wordapp.models.Word
import com.example.wordapp.viewModels.AddWordViewModel
import com.google.android.material.snackbar.Snackbar
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class AddWordFragment : Fragment() {
    private lateinit var binding: FragmentAddEditWordBinding
    private val viewModel: AddWordViewModel by viewModels {
        AddWordViewModel.Provider((requireContext().applicationContext as MyApplication).wordRepo)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddEditWordBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            btnSave.text = "Proceed"
            btnSave.setOnClickListener {
                val word = etWord.text.toString()
                val meaning = etMeaning.text.toString()
                val synonym = etSynonym.text.toString()
                val example = etExample.text.toString()

                val timeZone = TimeZone.getTimeZone("GMT+8")
                val calendar = Calendar.getInstance()
//                val dateFormat: DateFormat =
//                    SimpleDateFormat("EEE, d MMM yyyy, HH:mm a z", Locale.ENGLISH)
                val dateFormat: DateFormat =
                    SimpleDateFormat("LLLL d yyyy, HH:mm a (z)", Locale.ENGLISH)
                dateFormat.timeZone = timeZone
                val date = dateFormat.format(calendar.time)

                if (validate(word, meaning, synonym, example)) {
                    val bundle = Bundle()
                    bundle.putBoolean("refresh", true)
                    setFragmentResult("result_from_add_word", bundle)
                    viewModel.addWord(Word(null, word, meaning, synonym, example, date))
                    Toast.makeText(requireContext(), "New word added", Toast.LENGTH_SHORT).show()
                    NavHostFragment.findNavController(requireParentFragment()).popBackStack()
                } else {
                    val snackBar =
                        Snackbar.make(it, "Please enter all the values", Snackbar.LENGTH_LONG)
                    snackBar.setBackgroundTint(
                        ContextCompat.getColor(requireContext(), R.color.red_200)
                    )
                    snackBar.setAction("Hide") {
                        snackBar.dismiss()
                    }
                    snackBar.show()
                }
            }
        }
    }

    private fun validate(vararg list: String): Boolean {
        for (field in list) {
            if (field.isEmpty()) {
                return false
            }
        }
        return true
    }
}