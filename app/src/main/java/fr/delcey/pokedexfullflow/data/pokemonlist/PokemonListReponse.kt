package fr.delcey.pokedexfullflow.data.pokemonlist

import com.google.gson.annotations.SerializedName

data class PokemonListReponse(

    @field:SerializedName("next")
    val next: String? = null,

    @field:SerializedName("previous")
    val previous: String? = null,

    @field:SerializedName("count")
    val count: Int? = null,

    @field:SerializedName("results")
    val pokemonLiteReponses: List<PokemonLiteReponse> = emptyList()
)