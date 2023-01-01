package com.example.wordapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.wordapp.adapters.HomeAdapter
import com.example.wordapp.databinding.FragmentHomeBinding
import com.example.wordapp.viewModels.HomeViewModel
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val allWordsFragment = AllWordsFragment.getInstance()
    private val completedWordsFragment = CompletedWordsFragment.getInstance()
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var currentPage = 0

        val adapter = HomeAdapter(
            listOf(allWordsFragment, completedWordsFragment),
//            requireActivity().supportFragmentManager,
            childFragmentManager,
            lifecycle
        )

        binding.vpHome.adapter = adapter
        //optional
        binding.vpHome.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentPage = position
            }
        })
        TabLayoutMediator(binding.tlHome, binding.vpHome) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Words of the Day"
                }
                else -> tab.text = "Completed Words"
            }
        }.attach()

        setFragmentResults()
    }

    private fun setFragmentResults() {
        setFragmentResultListener("result_from_add_word") { _, result ->
            val refresh = result.getBoolean("refresh")
            viewModel.doRefreshAllWords(refresh)
        }

        setFragmentResultListener("result_from_edit_word") { _, result ->
            val refresh = result.getBoolean("refresh")
            viewModel.doRefreshAllWords(refresh)
            viewModel.doRefreshCompletedWords(refresh)
        }

        setFragmentResultListener("result_from_delete_word") { _, result ->
            val refresh = result.getBoolean("refresh")
            viewModel.doRefreshAllWords(refresh)
            viewModel.doRefreshCompletedWords(refresh)
        }

        setFragmentResultListener("result_from_complete_word") { _, result ->
            val refresh = result.getBoolean("refresh")
            viewModel.doRefreshAllWords(refresh)
            viewModel.doRefreshCompletedWords(refresh)
        }
    }
}