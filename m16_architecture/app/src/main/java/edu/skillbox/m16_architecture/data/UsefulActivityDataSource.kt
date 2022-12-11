package edu.skillbox.m16_architecture.data

import edu.skillbox.m16_architecture.entity.UsefulActivity
import retrofit2.Response
import javax.inject.Inject

class UsefulActivityDataSource @Inject constructor(
    private val remoteRepository: RemoteRepository
) {

    suspend fun fetchUsefulActivity() : Response<UsefulActivityDto> {
        return remoteRepository.searchUsefulActivityApi.findUsefulActivity()
    }
}