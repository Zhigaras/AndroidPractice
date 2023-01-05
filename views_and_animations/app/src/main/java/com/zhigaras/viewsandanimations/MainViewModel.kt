package com.zhigaras.viewsandanimations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.job
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    
    private val scope = viewModelScope
    private var job = scope.coroutineContext.job
    private val timerInstance = TimerState(0L, false)
    
    private val _timerFlow = MutableStateFlow(timerInstance)
    val timerFlow = _timerFlow.asStateFlow()
    
    
    fun startCount() {
        timerInstance.isRunning = true
        job = scope.launch {
            while (isActive) {
                delay(100)
                timerInstance.time += 100
                _timerFlow.value = timerInstance
            }
        }
    }
    
    fun stopCount() {
        job.cancel()
        timerInstance.isRunning = false
    }
    
    fun resetCount() {
        stopCount()
        timerInstance.time = 0L
    }
}