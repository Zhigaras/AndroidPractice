package com.zhigaras.rickandmortypagination.data

import com.zhigaras.rickandmortypagination.model.Personage
import javax.inject.Inject

class Repository @Inject constructor(
    private val charactersApi: CharactersApi
) {
    suspend fun loadCharacters(): List<Personage>? {
        return charactersApi.charactersSearchApi.findRoverPhotos(4).body()?.personages
    }
}