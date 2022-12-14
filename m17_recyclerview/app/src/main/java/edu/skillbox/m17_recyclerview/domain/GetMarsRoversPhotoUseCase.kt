package edu.skillbox.m17_recyclerview.domain

import edu.skillbox.m17_recyclerview.data.MarsRoversPhotoRepository
import edu.skillbox.m17_recyclerview.entity.MarsRoversPhoto
import edu.skillbox.m17_recyclerview.entity.MarsRoversPhotosList
import retrofit2.Response
import javax.inject.Inject

class GetMarsRoversPhotoUseCase @Inject constructor(
    private val marsRoversPhotoRepository: MarsRoversPhotoRepository
) {
    suspend fun fetchMarsRoversPhoto(): List<MarsRoversPhoto> {
        return marsRoversPhotoRepository.loadMarsRoversPhoto()
    }
}
