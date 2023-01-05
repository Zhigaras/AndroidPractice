package com.zhigaras.viewsandanimations

import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhigaras.viewsandanimations.databinding.FragmentMainBinding

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
        
        setUpButtonsClickListeners()
        
        binding.customTimerView.addUpdateListener { updateDigitalCLock(it) }
        binding.customTimerView.addUpdateListener { updateButtonState(it) }
        
    }
    
    private fun setUpButtonsClickListeners() {
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
    
    private fun updateDigitalCLock(timerState: TimerState) {
        val calendar = Calendar.getInstance()
        calendar.timeZone = TimeZone.GMT_ZONE
        calendar.timeInMillis = timerState.time
        val seconds = calendar.get(Calendar.SECOND)
        val minutes = calendar.get(Calendar.MINUTE)
        val hours = calendar.get(Calendar.HOUR)
        val timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds)
        binding.timerText.text = timeString
    }
    
    private fun updateButtonState(timerState: TimerState) {
        currentBtnState = if (timerState.isRunning) {
            StartStopButtonState.Running()
        } else {
            StartStopButtonState.Waiting()
        }
        binding.startStopButton.text = currentBtnState.text
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        binding.customTimerView.removeUpdateListener { updateDigitalCLock(it) }
        binding.customTimerView.removeUpdateListener { updateButtonState(it) }
        _binding = null
    }
}