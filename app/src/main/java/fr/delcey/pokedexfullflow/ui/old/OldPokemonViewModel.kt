package fr.delcey.pokedexfullflow.ui.old

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.delcey.pokedexfullflow.CoroutineToolsProvider
import fr.delcey.pokedexfullflow.data.PokemonRepository
import fr.delcey.pokedexfullflow.data.pokemon.PokemonResponse
import fr.delcey.pokedexfullflow.ui.PokemonUiState
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

@Suppress("EXPERIMENTAL_API_USAGE")
@HiltViewModel
class OldPokemonViewModel @Inject constructor(
    pokemonRepository: PokemonRepository,
    coroutineToolsProvider: CoroutineToolsProvider
) : ViewModel() {

    val uiStateLiveData = pokemonRepository.pokemonsFlow.mapLatest { pokemonResponses ->
        pokemonResponses.mapNotNull { pokemonResponse ->
            map(pokemonResponse)
        }
    }.asLiveData(coroutineToolsProvider.ioCoroutineDispatcher)

    private fun map(pokemonResponse: PokemonResponse): PokemonUiState? = if (pokemonResponse.id != null
        && pokemonResponse.name != null
        && pokemonResponse.sprites?.frontDefault != null
    ) {
        PokemonUiState(
            id = pokemonResponse.id,
            name = pokemonResponse.name,
            imageUrl = pokemonResponse.sprites.frontDefault,
            number = pokemonResponse.id.toString()
        )
    } else {
        null
    }
}
