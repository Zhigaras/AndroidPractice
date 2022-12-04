package edu.skillbox.m14retrofit.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

private const val TAG = "MainViewModel"

class MainViewModel : ViewModel() {

    init {
        Log.d(TAG, "init")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "onCleared")
    }

    private var _state = MutableStateFlow<State>(State.Success())
    val state = _state.asStateFlow()

    private val _user = Channel<Results>()
    val user = _user.receiveAsFlow()

    fun onRefreshBtnClick() {
        Log.d(TAG, "onRefreshBtnClick")
        viewModelScope.launch {
            _state.value = State.Progress
            val response = RetrofitInstance.searchUserApi.getUser()
            if (response.isSuccessful) {
                try {
                    _user.send(response.body()!!.results.first())
                    _state.value = State.Success()
                } catch (e: Throwable) {
                    _state.value = State.Error(e.message.toString())
                }
            } else
                _state.value = State.Error("Download error")
        }
    }
}