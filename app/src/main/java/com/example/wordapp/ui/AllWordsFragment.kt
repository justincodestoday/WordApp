package com.example.wordapp.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wordapp.MyApplication
import com.example.wordapp.R
import com.example.wordapp.adapters.WordAdapter
import com.example.wordapp.databinding.FragmentAllWordsBinding
import com.example.wordapp.databinding.SortDialogBinding
import com.example.wordapp.viewModels.AllWordsViewModel
import com.example.wordapp.viewModels.HomeViewModel
import com.google.android.material.snackbar.Snackbar

class AllWordsFragment : Fragment() {
    private lateinit var binding: FragmentAllWordsBinding
    private lateinit var adapter: WordAdapter
    private val viewModel: AllWordsViewModel by viewModels {
        AllWordsViewModel.Provider((requireContext().applicationContext as MyApplication).wordRepo)
    }
    private val homeViewModel: HomeViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllWordsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()

        var sortSearch = ""
        viewModel.words.observe(viewLifecycleOwner) { words ->
            binding.llEmpty.isGone = words.isNotEmpty()
            adapter.setWords(words)
        }

        homeViewModel.refreshAllWords.observe(viewLifecycleOwner) {
            if (it) {
                refresh()
                homeViewModel.doRefreshAllWords(false)
            }
        }

        binding.run {
            search.etSearch.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    search.ibCancel.isVisible = true
                }
            }

            search.ibCancel.setOnClickListener {
                viewModel.getWords("")
                search.ibCancel.isVisible = false
                search.etSearch.clearFocus()
                search.etSearch.text.clear()
                sortSearch = ""

                if (viewModel.words.value?.size == 0) {
                    binding.llEmpty.isGone = false
                    binding.tvLine1.text = "No word(s) of the day."
                    binding.tvLine2.text = "Add a word today."
                } else {
                    binding.llEmpty.isGone = true
                }
            }

            search.ibSearch.setOnClickListener {
                search.ibCancel.isVisible = false
                search.etSearch.clearFocus()
                val search = search.etSearch.text.trim().toString()
                viewModel.getWords(search)
                sortSearch = search

                if (viewModel.words.value?.size == 0) {
                    binding.llEmpty.isGone = false
                    binding.tvLine1.text = "No matching result found for \"${search}\"."
                    binding.tvLine2.text = ""
                }

                val snackBar =
                    Snackbar.make(
                        it,
                        "${viewModel.words.value?.size} match(s) found",
                        Snackbar.LENGTH_LONG
                    )
                snackBar.setBackgroundTint(
                    ContextCompat.getColor(requireContext(), R.color.blue_900)
                )
                snackBar.setAction("Hide") {
                    snackBar.dismiss()
                }
                snackBar.show()
            }

            fabAdd.setOnClickListener {
                val directory = HomeFragmentDirections.actionHomeFragmentToAddWordFragment()
                NavHostFragment.findNavController(requireParentFragment()).navigate(directory)
            }

            search.ibSort.setOnClickListener {
                var order = ""
                var category = ""
                val dialogBinding = SortDialogBinding.inflate(layoutInflater)
                val myDialog = Dialog(requireContext(), R.style.Custom_AlertDialog)
                myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialogBinding.radioGroup.setOnCheckedChangeListener { _, id ->
                    order = when (id) {
                        R.id.rbAsc -> {
                            "Ascending"
                        }
                        else -> {
                            "Descending"
                        }
                    }

                }
                dialogBinding.radioGroup2.setOnCheckedChangeListener { _, id ->
                    category = when (id) {
                        R.id.rbWord -> {
                            "Word"
                        }
                        else -> {
                            "Date"
                        }
                    }
                }
                myDialog.setContentView(dialogBinding.root)
                myDialog.setCancelable(true)
                myDialog.show()

                dialogBinding.btnDone.setOnClickListener {
                    if (dialogBinding.radioGroup.checkedRadioButtonId == -1
                        || dialogBinding.radioGroup2.checkedRadioButtonId == -1
                    ) {
                        dialogBinding.tvWarning.isVisible = true
                    } else {
                        sortRefresh(order, category, sortSearch)
                        myDialog.dismiss()
                    }
                }
            }
        }
    }

    private fun setupAdapter() {
        val layoutManager = LinearLayoutManager(this.activity)
        adapter = WordAdapter(emptyList()) {
            val directory = HomeFragmentDirections.actionHomeFragmentToWordFragment(it.id!!)
            NavHostFragment.findNavController(this).navigate(directory)
        }
        binding.rvAllWords.adapter = adapter
        binding.rvAllWords.layoutManager = layoutManager
    }

    private fun refresh() {
        viewModel.getWords("")
    }

    private fun sortRefresh(order: String, category: String, str: String) {
        viewModel.sortWords(order, category, str)
    }

    companion object {
        private var allWordsFragmentInstance: AllWordsFragment? = null

        fun getInstance(): AllWordsFragment {
            if (allWordsFragmentInstance == null) {
                allWordsFragmentInstance = AllWordsFragment()
            }
            return allWordsFragmentInstance!!
        }
    }
}