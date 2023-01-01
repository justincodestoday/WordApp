package com.example.wordapp.ui

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.example.wordapp.MyApplication
import com.example.wordapp.R
import com.example.wordapp.databinding.FragmentWordBinding
import com.example.wordapp.viewModels.WordViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class WordFragment : Fragment() {
    private lateinit var binding: FragmentWordBinding
    private val viewModel: WordViewModel by viewModels {
        WordViewModel.Provider((requireContext().applicationContext as MyApplication).wordRepo)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWordBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var status = false
        val args: WordFragmentArgs by navArgs()
        viewModel.getWordById(args.id)
        viewModel.word.observe(viewLifecycleOwner) {
            binding.apply {
                status = it.status
                if (it.status) {
                    fabComplete.setImageResource(R.drawable.ic_undo);
                    fabComplete.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.yellow_500
                        )
                    )
                }
                tvDate.text = it.date
                tvWord.text = it.word
                tvMeaning.text = it.meaning
                tvSynonym.text = it.synonym
                tvExample.text = it.example
            }
        }

        binding.apply {
            fabComplete.setOnClickListener {
                if (!status) {
                    MaterialAlertDialogBuilder(
                        requireContext(), R.style.Confirm_AlertDialog
                    ).setTitle("Mark word as completed")
                        .setMessage(
                            "Are you sure you want to mark \"${tvWord.text}\" as completed?" +
                                    "\nYou can change this again later."
                        )
                        .setPositiveButton("Confirm") { _, _ ->
                            val bundle = Bundle()
                            bundle.putBoolean("refresh", true)
                            setFragmentResult("result_from_complete_word", bundle)
                            viewModel.changeWordStatus(args.id)
                            Toast.makeText(requireContext(), "Word completed", Toast.LENGTH_SHORT)
                                .show()
                            NavHostFragment.findNavController(requireParentFragment())
                                .popBackStack()
                        }
                        .setNegativeButton("Cancel") { _, _ -> }
                        .create().show()
                } else {
                    MaterialAlertDialogBuilder(
                        requireContext(), R.style.Confirm2_AlertDialog
                    ).setTitle("Mark word as not completed")
                        .setMessage(
                            "Are you sure you want to revert \"${tvWord.text}\" to not completed?" +
                                    "\nYou can change this again later."
                        )
                        .setPositiveButton("Confirm") { _, _ ->
                            val bundle = Bundle()
                            bundle.putBoolean("refresh", true)
                            setFragmentResult("result_from_complete_word", bundle)
                            viewModel.changeWordStatus(args.id)
                            Toast.makeText(requireContext(), "Word completed", Toast.LENGTH_SHORT)
                                .show()
                            NavHostFragment.findNavController(requireParentFragment())
                                .popBackStack()
                        }
                        .setNegativeButton("Cancel") { _, _ -> }
                        .create().show()
                }
            }
            fabEdit.setOnClickListener {
                val editWord = WordFragmentDirections.actionWordFragmentToEditWordFragment(args.id)
                NavHostFragment.findNavController(requireParentFragment()).navigate(editWord)
            }
            fabDelete.setOnClickListener {
                MaterialAlertDialogBuilder(
                    requireContext(), R.style.Delete_AlertDialog
                ).setTitle("Delete Word")
                    .setMessage(
                        "Are you sure you want to remove \"${tvWord.text}\" from the board?" +
                                "\nYou cannot undo this action."
                    )
                    .setPositiveButton("Delete") { _, _ ->
                        val bundle = Bundle()
                        bundle.putBoolean("refresh", true)
                        setFragmentResult("result_from_delete_word", bundle)
                        viewModel.deleteWord(args.id)
                        Toast.makeText(requireContext(), "Word deleted", Toast.LENGTH_SHORT).show()
                        NavHostFragment.findNavController(requireParentFragment()).popBackStack()
                    }
                    .setNegativeButton("Cancel") { _, _ -> }
                    .create().show()
            }
        }
    }
}
//