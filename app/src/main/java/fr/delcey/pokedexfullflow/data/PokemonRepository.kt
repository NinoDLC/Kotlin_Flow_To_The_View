package fr.delcey.pokedexfullflow.data

import android.util.Log
import fr.delcey.pokedexfullflow.data.pokemon.PokemonResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PokemonRepository @Inject constructor() {

    private val pokeApi: PokeApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(
                        HttpLoggingInterceptor().apply {
                            setLevel(HttpLoggingInterceptor.Level.BODY)
                        }
                    )
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        pokeApi = retrofit.create(PokeApi::class.java)
    }

    /**
     * A Flow that completes after at least 18 seconds (9 pokemons queried)
     */
    fun getPokemonsFlow(): Flow<List<PokemonResponse>> = flow {

        val pokemonResponses = mutableListOf<PokemonResponse>()

        for (i in 1..9) {

            delay(2_000)

            pokeApi.getPokemonById(i.toString())?.let { pokemonResponse ->
                pokemonResponses.add(pokemonResponse)

                Log.d("PokemonRepository", "getPokemonsFlow() : emitting ${pokemonResponses.size} pokemons")

                emit(pokemonResponses)
            }
        }
    }

    /**
     * A Flow that never completes
     */
    fun getInfinitePokemonsFlow(): Flow<List<PokemonResponse>> = flow {

        val pokemonResponses = mutableListOf<PokemonResponse>()
        var i = 1

        while (true) {
            delay(2_000)

            pokeApi.getPokemonById(i.toString())?.let { pokemonResponse ->
                pokemonResponses.add(pokemonResponse)

                Log.d("PokemonRepository", "getInfinitePokemonsFlow() : emitting ${pokemonResponses.size} pokemons")

                emit(pokemonResponses)
            }

            i++
        }
    }
}
