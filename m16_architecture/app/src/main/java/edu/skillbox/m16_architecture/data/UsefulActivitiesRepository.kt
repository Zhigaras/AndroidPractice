package edu.skillbox.m16_architecture.data

import edu.skillbox.m16_architecture.entity.UsefulActivity
import retrofit2.Response
import javax.inject.Inject

class UsefulActivitiesRepository @Inject constructor(
    private val usefulActivityDataSource: UsefulActivityDataSource
) {

    suspend fun getUsefulActivity(): Response<UsefulActivityDto> {
        return usefulActivityDataSource.fetchUsefulActivity()
    }
}