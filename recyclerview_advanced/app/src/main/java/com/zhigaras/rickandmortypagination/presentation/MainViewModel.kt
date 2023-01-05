package com.zhigaras.rickandmortypagination.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhigaras.rickandmortypagination.data.Repository
import com.zhigaras.rickandmortypagination.model.Personage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    
    private val _charactersFlow = MutableStateFlow<List<Personage>>(emptyList())
    val charactersFlow = _charactersFlow.asStateFlow()
    
    
    fun getCharacters() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                repository.loadCharacters()
            }.fold(
                onSuccess = { _charactersFlow.value = it!! },
                onFailure = { Log.d("MainViewModel",it.message ?: "")}
            )
        }
    }
}