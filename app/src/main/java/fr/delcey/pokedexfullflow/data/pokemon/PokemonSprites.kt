package fr.delcey.pokedexfullflow.data.pokemon

import com.google.gson.annotations.SerializedName

data class PokemonSprites(

    @field:SerializedName("front_default")
    val frontDefault: String? = null
)