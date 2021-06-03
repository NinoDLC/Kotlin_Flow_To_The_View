package fr.delcey.pokedexfullflow.data

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
        val pokemonListReponse = pokeApi.getAllPokemon()

        val arrayList = ArrayList<PokemonResponse>()

        // TODO Paging to do ! (now we only have the 20 first pokemons queried)
        for (i in pokemonListReponse?.pokemonLiteReponses?.indices ?: IntRange.EMPTY) {

            delay(2_000)

            val pokemonLiteReponse = pokemonListReponse?.pokemonLiteReponses?.get(i)

            if (pokemonLiteReponse?.name != null) {
                pokeApi.getPokemonById(pokemonLiteReponse.name)?.let {
                    arrayList.add(it)
                }

                emit(arrayList)
            }
        }
    }

    /**
     * A Flow that never completes
     */
    val infinitePokemonFlow: Flow<List<PokemonResponse>> = flow {

        val arrayList = ArrayList<PokemonResponse>()
        var i = 1

        while (true) {
            delay(2_000)

            pokeApi.getPokemonById(i.toString())?.let {
                arrayList.add(it)
            }

            i++

            emit(arrayList)
        }
    }
}
