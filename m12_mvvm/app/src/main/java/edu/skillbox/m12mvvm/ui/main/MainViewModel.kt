package edu.skillbox.m12mvvm.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainViewModel : ViewModel() {

    private var _state = MutableStateFlow<State>(State.Success())
    val state = _state.asStateFlow()

    fun onButtonClick(searchRequest: String) {
        viewModelScope.launch {
            _state.value = State.Progress
            delay(5_000)
            if (Random.nextBoolean())
                _state.value = State.Success("Done. Here is the search result for the query '$searchRequest'.")
            else
                _state.value = State.Error("No results found for query '$searchRequest'.")
        }
    }
}