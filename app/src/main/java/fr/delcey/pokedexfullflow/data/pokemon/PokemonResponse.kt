package fr.delcey.pokedexfullflow.data.pokemon

import com.google.gson.annotations.SerializedName

data class PokemonResponse(

    @field:SerializedName("types")
    val types: List<PokemonTypeItem> = emptyList(),

    @field:SerializedName("base_experience")
    val baseExperience: Int? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("weight")
    val weight: Int? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("is_default")
    val isDefault: Boolean? = null,

    @field:SerializedName("sprites")
    val sprites: PokemonSprites? = null,

    @field:SerializedName("height")
    val height: Int? = null,

    @field:SerializedName("order")
    val order: Int? = null
)