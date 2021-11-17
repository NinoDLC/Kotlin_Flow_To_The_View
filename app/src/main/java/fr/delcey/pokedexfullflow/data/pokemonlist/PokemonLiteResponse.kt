package fr.delcey.pokedexfullflow.data.pokemonlist

import com.google.gson.annotations.SerializedName

data class PokemonLiteResponse(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("url")
    val url: String? = null
)