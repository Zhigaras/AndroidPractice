package com.zhigaras.ricandmortycomposable

import com.zhigaras.ricandmortycomposable.model.Location
import com.zhigaras.ricandmortycomposable.model.Origin
import com.zhigaras.ricandmortycomposable.model.Personage

object Test {
    
    val testPersonage = Personage(
        created = "created",
        episode = listOf("1", "2", "3"),
        gender = "gender",
        id = 111,
        image = "https://rickandmortyapi.com/api/character/avatar/18.jpeg",
        location = Location(
            name = "locationName",
            url = "https://rickandmortyapi.com/api/location/3",
            created = "created",
            dimension = "dimension",
            id = 1,
            type = "type",
            residents = listOf("resident1", "resident2")
        ),
        name = "Morty Smith",
        origin = Origin("unknown", ""),
        species = "Human",
        status = "Alive",
        type = "Human with antennae",
        url = "https://rickandmortyapi.com/api/character/18"
    )
}