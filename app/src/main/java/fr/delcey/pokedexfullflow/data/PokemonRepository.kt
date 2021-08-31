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
        // TODO provide in a module

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
     * A Flow that completes after at least 40 seconds (20 pokemon queried)
     */
    val pokemonsFlow: Flow<List<PokemonResponse>> = flow {
        val pokemonLiteListResponse = pokeApi.getAllPokemon()

        val pokemonResponses = ArrayList<PokemonResponse>()

        // TODO Paging to do ! (now we only have the 20 first pokemons queried)
        for (i in pokemonLiteListResponse?.pokemonLiteReponses?.indices ?: IntRange.EMPTY) {

            delay(2_000)

            val pokemonLiteResponse = pokemonLiteListResponse?.pokemonLiteReponses?.get(i)

            if (pokemonLiteResponse?.name != null) {
                val pokemonFullResponse = pokeApi.getPokemonById(pokemonLiteResponse.name)

                if (pokemonFullResponse != null) {
                    pokemonResponses.add(pokemonFullResponse)

                    Log.d("Nino", "emitting ${pokemonResponses.size} pokemons")

                    emit(pokemonResponses)
                }
            }
        }
    }

    /**
     * A Flow that never completes
     */
    val infinitePokemonFlow: Flow<List<PokemonResponse>> = flow {

        val pokemonReponses = ArrayList<PokemonResponse>()
        var i = 1

        while (true) {
            delay(2_000)

            pokeApi.getPokemonById(i.toString())?.let {
                pokemonReponses.add(it)
            }

            i++

            emit(pokemonReponses)
        }
    }
}
