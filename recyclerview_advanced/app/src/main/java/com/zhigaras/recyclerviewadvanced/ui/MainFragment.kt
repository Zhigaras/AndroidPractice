package com.zhigaras.recyclerviewadvanced.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.zhigaras.recyclerviewadvanced.databinding.FragmentMainBinding
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
        
        binding.recyclerView.adapter = pagedPersonageAdapter
        
        viewModel.pagedPersonage.onEach {
            pagedPersonageAdapter.submitData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
        
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
}