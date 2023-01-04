package com.zhigaras.viewsandanimations

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.zhigaras.viewsandanimations.databinding.FragmentMainBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainFragment : Fragment() {
    
    companion object {
        fun newInstance() = MainFragment()
    }
    
    private var currentBtnState: StartStopButtonState = StartStopButtonState.Waiting()
    
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setUpStartStopBtn()
        setUpButtonsListeners()
        
    }
    
    private fun setUpButtonsListeners() {
        binding.startStopButton.setOnClickListener {
            when (currentBtnState) {
                is StartStopButtonState.Waiting -> binding.customTimerView.start()
                is StartStopButtonState.Running -> binding.customTimerView.stop()
            }
        }
        
        binding.resetButton.setOnClickListener {
            binding.customTimerView.reset()
        }
    }
    
    private fun setUpStartStopBtn() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                binding.customTimerView.currentTime().collect { timerState ->
                    currentBtnState = if (timerState.isRunning) {
                        StartStopButtonState.Running()
                    } else {
                        StartStopButtonState.Waiting()
                    }
                    binding.startStopButton.text = currentBtnState.text
                    binding.timerText.text = timerState.time.toString()
                }
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
}