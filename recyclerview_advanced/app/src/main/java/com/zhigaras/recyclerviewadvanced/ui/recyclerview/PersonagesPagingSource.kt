package com.zhigaras.recyclerviewadvanced.ui.recyclerview

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zhigaras.recyclerviewadvanced.data.PersonagesApi
import com.zhigaras.recyclerviewadvanced.data.Repository
import com.zhigaras.recyclerviewadvanced.model.Personage
import kotlinx.coroutines.delay

class PersonagesPagingSource : PagingSource<Int, Personage>() {
    
    private val repository = Repository()
    
    override fun getRefreshKey(state: PagingState<Int, Personage>): Int = FIRST_PAGE
    
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Personage> {
        val page = params.key ?: FIRST_PAGE
        return kotlin.runCatching {
            repository.getPersonages(page).body()!!.personages
        }.fold(
            onSuccess = {
                LoadResult.Page(
                    data = it,
                    prevKey = null,
                    nextKey = if (it.isEmpty()) null else page + 1
                
                )
            },
            onFailure = { LoadResult.Error(it) }
        )
    }
    
    companion object {
        private const val FIRST_PAGE = 1
    }
}