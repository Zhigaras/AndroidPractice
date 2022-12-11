package edu.skillbox.m16_architecture.domain

import edu.skillbox.m16_architecture.data.UsefulActivitiesRepository
import edu.skillbox.m16_architecture.data.UsefulActivityDto
import edu.skillbox.m16_architecture.entity.UsefulActivity
import retrofit2.Response
import javax.inject.Inject

class GetUsefulActivityUseCase @Inject constructor(
    private val usefulActivitiesRepository: UsefulActivitiesRepository
    ) {
    suspend fun execute(): Response<UsefulActivityDto> {
        return usefulActivitiesRepository.getUsefulActivity()

    }
}