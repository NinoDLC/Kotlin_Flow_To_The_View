package fr.delcey.pokedexfullflow.data

import fr.delcey.pokedexfullflow.data.pokemon.PokemonResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeApi {
    @GET("pokemon/{pokemonId}/")
    suspend fun getPokemonById(@Path("pokemonId") pokemonId: String): PokemonResponse
}