package com.zhigaras.recyclerviewadvanced.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.zhigaras.recyclerviewadvanced.model.Personage
import com.zhigaras.recyclerviewadvanced.ui.recyclerview.PersonagesPagingSource
import kotlinx.coroutines.flow.Flow

class MainViewModel : ViewModel() {
    
    val pagedPersonage: Flow<PagingData<Personage>> = Pager(
        config = PagingConfig(20),
        pagingSourceFactory = { PersonagesPagingSource() }
    ).flow.cachedIn(viewModelScope)
    
}