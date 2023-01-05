package com.zhigaras.rickandmortypagination

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.zhigaras.rickandmortypagination.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    
    companion object {
        fun newInstance() = MainFragment()
    }
    
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        
        
    }
    
}