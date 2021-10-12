package fr.delcey.pokedexfullflow.ui.flow

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.delcey.pokedexfullflow.CoroutineToolsProvider
import fr.delcey.pokedexfullflow.data.PokemonRepository
import fr.delcey.pokedexfullflow.data.pokemon.PokemonResponse
import fr.delcey.pokedexfullflow.ui.PokemonViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.plus
import java.util.Locale
import javax.inject.Inject

@Suppress("EXPERIMENTAL_API_USAGE")
@HiltViewModel
class FlowPokemonViewModel @Inject constructor(
    pokemonRepository: PokemonRepository,
    coroutineToolsProvider: CoroutineToolsProvider
) : ViewModel() {

    // MutableStateFlow won't work because it doesn't emit downstream a value that is equal to the previous one
    private val triggerRefreshMutableSharedFlow = MutableSharedFlow<Unit>(replay = 1).apply {
        tryEmit(Unit)
    }

    val viewStateFlow: Flow<List<PokemonViewState>> = flow {
        emit(listOf(PokemonViewState(1, "TOTO", "", "666")))
    }.onCompletion {
        Log.d("Nino", "viewStateFlow() onCompletion")
    }.stateIn(
        scope = viewModelScope.plus(coroutineToolsProvider.ioCoroutineDispatcher),
        started = coroutineToolsProvider.sharingStartedStrategy,
        initialValue = emptyList()
    )

    fun refresh() {
        triggerRefreshMutableSharedFlow.tryEmit(Unit)
    }

    private fun map(pokemonResponse: PokemonResponse): PokemonViewState? = if (pokemonResponse.id != null
        && pokemonResponse.name != null
        && pokemonResponse.sprites?.frontDefault != null
    ) {
        PokemonViewState(
            id = pokemonResponse.id,
            name = pokemonResponse.name.replaceFirstChar { it.titlecase(Locale.getDefault()) },
            imageUrl = pokemonResponse.sprites.frontDefault,
            number = pokemonResponse.id.toString()
        )
    } else {
        null
    }
}