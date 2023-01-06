package com.zhigaras.recyclerviewadvanced.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.zhigaras.recyclerviewadvanced.R
import com.zhigaras.recyclerviewadvanced.databinding.FragmentMainBinding
import com.zhigaras.recyclerviewadvanced.ui.recyclerview.MarginItemDecoration
import com.zhigaras.recyclerviewadvanced.ui.recyclerview.PageLoadStateAdapter
import com.zhigaras.recyclerviewadvanced.ui.recyclerview.PersonagesPagerAdapter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainFragment : Fragment() {
    
    companion object {
        fun newInstance() = MainFragment()
    }
    
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: MainViewModel by viewModels()
    
    private val pagedPersonageAdapter = PersonagesPagerAdapter()
    
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupPagedAdapter()
        setupRefreshSwipe()
        setupErrorsShow()
        
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        pagedPersonageAdapter.removeLoadStateListener { }
    }
    
    private fun setupRefreshSwipe() {
        
        binding.swipeRefresh.setOnRefreshListener {
            pagedPersonageAdapter.refresh()
        }
        
        pagedPersonageAdapter.loadStateFlow.onEach {
            binding.swipeRefresh.isRefreshing = it.refresh == LoadState.Loading
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
    
    private fun setupErrorsShow() {
        
        pagedPersonageAdapter.loadStateFlow.onEach {
            val loadStateList = listOf(it.refresh, it.append)
            loadStateList.forEach { loadState ->
                if (loadState is LoadState.Error) {
                    makeToast(loadState.error.message.toString())
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
    
    private fun setupPagedAdapter() {
        
        binding.recyclerView.adapter =
            pagedPersonageAdapter.withLoadStateFooter(PageLoadStateAdapter())
        binding.recyclerView.addItemDecoration(
            MarginItemDecoration(
                resources.getDimensionPixelSize(
                    R.dimen.recycler_view_dimen
                )
            )
        )
        
        viewModel.pagedPersonage.onEach {
            pagedPersonageAdapter.submitData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
    
    private fun makeToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
    }
    
}