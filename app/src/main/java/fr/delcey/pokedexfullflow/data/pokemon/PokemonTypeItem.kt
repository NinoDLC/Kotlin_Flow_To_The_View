package fr.delcey.pokedexfullflow.data.pokemon

import com.google.gson.annotations.SerializedName

data class PokemonTypeItem(

    @field:SerializedName("slot")
    val slot: Int? = null,

    @field:SerializedName("type")
    val type: PokemonType? = null
)