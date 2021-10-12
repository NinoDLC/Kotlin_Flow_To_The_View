package fr.delcey.pokedexfullflow.ui.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.delcey.pokedexfullflow.CoroutineToolsProvider
import fr.delcey.pokedexfullflow.data.PokemonRepository
import fr.delcey.pokedexfullflow.data.pokemon.PokemonResponse
import fr.delcey.pokedexfullflow.ui.PokemonViewState
import kotlinx.coroutines.flow.mapLatest
import java.util.Locale
import javax.inject.Inject

@Suppress("EXPERIMENTAL_API_USAGE")
@HiltViewModel
class LiveDataPokemonViewModel @Inject constructor(
    pokemonRepository: PokemonRepository,
    coroutineToolsProvider: CoroutineToolsProvider
) : ViewModel() {

    private val triggerRefreshMutableLiveData = MutableLiveData(Unit)

    val viewStateLiveData: LiveData<List<PokemonViewState>> = triggerRefreshMutableLiveData.switchMap {
        pokemonRepository.getPokemonsFlow().mapLatest { pokemonResponses ->
            pokemonResponses.mapNotNull { pokemonResponse ->
                map(pokemonResponse)
            }
        }.asLiveData(coroutineToolsProvider.ioCoroutineDispatcher)
    }

    fun refresh() {
        triggerRefreshMutableLiveData.value = Unit
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
