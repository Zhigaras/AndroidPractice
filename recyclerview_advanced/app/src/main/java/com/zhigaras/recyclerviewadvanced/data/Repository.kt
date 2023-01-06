package com.zhigaras.recyclerviewadvanced.data

import com.zhigaras.recyclerviewadvanced.model.ApiReply
import com.zhigaras.recyclerviewadvanced.model.Personage
import kotlinx.coroutines.delay
import retrofit2.Response

class Repository {
    suspend fun getPersonages(page: Int): Response<ApiReply> {
        delay(2000)
        return PersonagesApi.personagesSearchApi.findPersonages(page)
    }
}