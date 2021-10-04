package fr.delcey.pokedexfullflow.ui.flow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.delcey.pokedexfullflow.CoroutineToolsProvider
import fr.delcey.pokedexfullflow.data.PokemonRepository
import fr.delcey.pokedexfullflow.data.pokemon.PokemonResponse
import fr.delcey.pokedexfullflow.ui.PokemonUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.plus
import javax.inject.Inject

@Suppress("EXPERIMENTAL_API_USAGE")
@HiltViewModel
class FlowPokemonViewModel @Inject constructor(
    pokemonRepository: PokemonRepository,
    coroutineToolsProvider: CoroutineToolsProvider
) : ViewModel() {

    val uiStateFlow: Flow<List<PokemonUiState>> = pokemonRepository.pokemonsFlow.mapLatest { pokemonResponses ->
        pokemonResponses.mapNotNull { pokemonResponse ->
            map(pokemonResponse)
        }
    }.stateIn(
        scope = viewModelScope.plus(coroutineToolsProvider.ioCoroutineDispatcher),
        started = coroutineToolsProvider.sharingStartedStrategy,
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
}
