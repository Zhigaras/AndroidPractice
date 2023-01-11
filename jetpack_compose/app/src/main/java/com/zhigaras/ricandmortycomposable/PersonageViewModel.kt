package com.zhigaras.ricandmortycomposable

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.zhigaras.ricandmortycomposable.data.LocationsPagingSource
import com.zhigaras.ricandmortycomposable.data.PersonagesPagingSource
import com.zhigaras.ricandmortycomposable.data.Repository
import com.zhigaras.ricandmortycomposable.model.Location
import com.zhigaras.ricandmortycomposable.model.Personage
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PersonageViewModel : ViewModel() {
    
    val pagedPersonages: Flow<PagingData<Personage>> = Pager(
        config = PagingConfig(20),
        pagingSourceFactory = { PersonagesPagingSource() }
    ).flow.cachedIn(viewModelScope)
    
    val pagedLocations: Flow<PagingData<Location>> = Pager(
        config = PagingConfig(20),
        pagingSourceFactory = { LocationsPagingSource() }
    ).flow.cachedIn(viewModelScope)
    
    private val coroutineExceptionHandler = CoroutineExceptionHandler{ _, throwable ->
        _errorFlow.value = throwable.localizedMessage
    }
    
    private val _personageDetailsChannel = MutableStateFlow<Personage?>(null)
    val personageDetailsChannel = _personageDetailsChannel.asStateFlow()
    
    private val _errorFlow = MutableStateFlow<String?>(null)
    val errorFlow = _errorFlow.asStateFlow()
    
    fun getPersonageDetails(id: Int) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val result = Repository.getPersonageDetails(id)
            if (result.isSuccessful) {
                result.body()?.let { _personageDetailsChannel.value = it }
                _errorFlow.value = null
            }
        }
    }
}