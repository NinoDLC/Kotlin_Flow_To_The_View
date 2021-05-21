package fr.delcey.pokedexfullflow.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.delcey.pokedexfullflow.CoroutineContextProvider
import fr.delcey.pokedexfullflow.data.PokemonRepository
import fr.delcey.pokedexfullflow.data.pokemon.PokemonResponse
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@Suppress("EXPERIMENTAL_API_USAGE")
@HiltViewModel
class PokemonViewModel @Inject constructor(
    pokemonRepository: PokemonRepository,
    coroutineContextProvider: CoroutineContextProvider
) : ViewModel() {

    val uiStateFlow = pokemonRepository.pokemonsFlow.mapLatest { pokemonResponses ->
        pokemonResponses.mapNotNull { pokemonResponse ->
            map(pokemonResponse)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

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

    fun onPokemonClicked(pokemonUiState: PokemonUiState) {
        TODO("Not yet implemented")
    }

}
