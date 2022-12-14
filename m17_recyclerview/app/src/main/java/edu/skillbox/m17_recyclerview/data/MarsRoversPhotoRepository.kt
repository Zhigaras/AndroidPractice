package edu.skillbox.m17_recyclerview.data


import edu.skillbox.m17_recyclerview.entity.MarsRoversPhoto
import javax.inject.Inject

class MarsRoversPhotoRepository @Inject constructor(
    private val remoteRepository: RemoteRepository
) {
    suspend fun loadMarsRoversPhoto(): List<MarsRoversPhoto> {
        return remoteRepository.nasaMarsRoverApi.findRoverPhotos(1000).photos
    }
}