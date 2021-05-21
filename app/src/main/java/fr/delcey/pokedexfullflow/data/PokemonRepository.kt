package fr.delcey.pokedexfullflow.data

import android.util.Log
import fr.delcey.pokedexfullflow.data.pokemon.PokemonResponse
import kotlinx.coroutines.delay
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

    val pokemonsFlow = flow {
        Log.d("Nino", "PokemonRepository : flow started")

        emit(listOf())

        val pokemons = pokeApi.getAllPokemon()

        Log.d("Nino", "PokemonRepository : all pokemons queried")

        val arrayList = ArrayList<PokemonResponse>()

        for (i in 0..3) {

            Log.d("Nino", "PokemonRepository : delaying...")

            delay(2_000)

            val pokemonLiteReponse = pokemons?.pokemonLiteReponses?.get(i)

            if (pokemonLiteReponse?.name != null) {
                pokeApi.getPokemonById(pokemonLiteReponse.name)?.let {
                    Log.d("Nino", "PokemonRepository : new pokemon queried : ${it.name}")
                    arrayList.add(it)
                }

                emit(arrayList)
            }
        }
    }


}
