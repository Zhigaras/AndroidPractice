package com.zhigaras.ricandmortycomposable.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zhigaras.ricandmortycomposable.model.Personage

class PersonagesPagingSource : PagingSource<Int, Personage>() {
    
    private val repository = Repository
    
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