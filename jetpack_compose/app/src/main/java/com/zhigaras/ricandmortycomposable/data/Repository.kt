package com.zhigaras.ricandmortycomposable.data

import com.zhigaras.ricandmortycomposable.model.LocationsApiReply
import com.zhigaras.ricandmortycomposable.model.Personage
import com.zhigaras.ricandmortycomposable.model.PersonagesApiReply
import retrofit2.Response

object Repository {
    suspend fun getPersonages(page: Int): Response<PersonagesApiReply> {
        return PersonagesApi.personagesSearchApi.getPersonages(page)
    }
    
    suspend fun getPersonageDetails(id: Int) : Response<Personage> {
        return PersonagesApi.personagesSearchApi.getPersonageDetails(id)
    }
    
    suspend fun getLocations(page: Int): Response<LocationsApiReply> {
        return PersonagesApi.personagesSearchApi.getLocations(page)
    }
}