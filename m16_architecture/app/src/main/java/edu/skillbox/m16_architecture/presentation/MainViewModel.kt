package edu.skillbox.m16_architecture.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.skillbox.m16_architecture.domain.GetUsefulActivityUseCase
import edu.skillbox.m16_architecture.domain.State
import edu.skillbox.m16_architecture.entity.UsefulActivity
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getUsefulActivityUseCase: GetUsefulActivityUseCase
) : ViewModel() {

    private val _usefulActivityFlow = MutableStateFlow<UsefulActivity?>(null)
    val usefulActivityFlow = _usefulActivityFlow.asStateFlow()

    private var _stateFlow = MutableStateFlow<State>(State.Success)
    val stateFlow = _stateFlow.asStateFlow()

    private val _errorChannel = Channel<String>()
    val errorChannel = _errorChannel.receiveAsFlow()

    fun reloadUsefulActivity() {
        viewModelScope.launch {
            _stateFlow.value = State.Progress
            val response = getUsefulActivityUseCase.execute()
            if (response.isSuccessful) {
                try {
                    _usefulActivityFlow.value = response.body()
                    _stateFlow.value = State.Success
                } catch (e: Throwable) {
                    _stateFlow.value = State.Error(e.toString())
                    _errorChannel.send(e.message.toString())
                }
            } else {
                _stateFlow.value = State.Error()
                _errorChannel.send("Download error!")
            }
        }
    }
}