package fr.delcey.pokedexfullflow.data.pokemon

import com.google.gson.annotations.SerializedName

data class PokemonType(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("url")
    val url: String? = null
)