package edu.skillbox.m14retrofit.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

private const val TAG = "MainViewModel"

class MainViewModel : ViewModel() {

    private var _state = MutableStateFlow<State>(State.Success())
    val stateFlow = _state.asStateFlow()

    private val _error = Channel<String>()
    val errorChannel = _error.receiveAsFlow()

    val user: MutableLiveData<User> by lazy {
        MutableLiveData<User>()
    }

    init {
        Log.d(TAG, "init")
        fetchUser()
    }

    fun fetchUser() {
        viewModelScope.launch {
            _state.value = State.Progress
            val response = RetrofitInstance.searchUserApi.getUser()
            if (response.isSuccessful) {
                try {
                    user.value = response.body()!!.user.first()
                    _state.value = State.Success()
                } catch (e: Throwable) {
                    _state.value = State.Error()
                    _error.send(e.message.toString())
                }
            } else {
                _state.value = State.Error()
                _error.send("Download error")
            }
        }
    }
}