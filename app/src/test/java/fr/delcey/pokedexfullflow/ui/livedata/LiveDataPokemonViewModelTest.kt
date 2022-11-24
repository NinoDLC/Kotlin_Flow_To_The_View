package fr.delcey.pokedexfullflow.ui.livedata

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import fr.delcey.pokedexfullflow.CoroutineToolsProvider
import fr.delcey.pokedexfullflow.data.PokemonRepository
import fr.delcey.pokedexfullflow.data.pokemon.PokemonResponse
import fr.delcey.pokedexfullflow.data.pokemon.PokemonSprites
import fr.delcey.pokedexfullflow.ui.PokemonViewState
import fr.delcey.pokedexfullflow.utils.TestCoroutineRule
import fr.delcey.pokedexfullflow.utils.observeForTesting
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceTimeBy
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LiveDataPokemonViewModelTest {

    companion object {
        private const val DEFAULT_LIST_SIZE = 3
    }

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val pokemonRepository = mockk<PokemonRepository>()

    private val coroutineToolsProvider = mockk<CoroutineToolsProvider>()

    @Before
    fun setUp() {
        every { pokemonRepository.getPokemonsFlow() } returns flowOf(getDefaultPokemonList())

        every { coroutineToolsProvider.ioCoroutineDispatcher } returns testCoroutineRule.testCoroutineDispatcher
    }

    @Test
    fun `nominal case`() = testCoroutineRule.runBlockingTest {
        // Given
        val viewModel = LiveDataPokemonViewModel(pokemonRepository, coroutineToolsProvider)

        // When
        viewModel.viewStateLiveData.observeForever {
            // Then
            assertEquals(getExpectedPokemonViewStates(), it)
        }
    }

    @Test
    fun `not so nominal case - a delay between 2 values !`() = testCoroutineRule.runBlockingTest {
        // Given
        every { pokemonRepository.getPokemonsFlow() } returns flow {
            emit(getDefaultPokemonList(1))
            delay(3_000)
            emit(getDefaultPokemonList())
        }
        val viewModel = LiveDataPokemonViewModel(pokemonRepository, coroutineToolsProvider)

        // When
        viewModel.viewStateLiveData.observeForTesting {
            advanceTimeBy(3_000)

            // Then
            assertEquals(getExpectedPokemonViewStates(), it.value)
        }
    }

    @Test
    fun `another not so nominal case - an initial delay !`() = testCoroutineRule.runBlockingTest {
        // Given
        every { pokemonRepository.getPokemonsFlow() } returns flow {
            delay(3_000)
            emit(getDefaultPokemonList())
        }
        val viewModel = LiveDataPokemonViewModel(pokemonRepository, coroutineToolsProvider)

        // When
        viewModel.viewStateLiveData.observeForTesting {
            advanceTimeBy(3_000)

            // Then
            assertEquals(getExpectedPokemonViewStates(), it.value)
        }
    }

    // region IN
    private fun getDefaultPokemonList(size: Int = DEFAULT_LIST_SIZE): List<PokemonResponse> = List(size) { index ->
        PokemonResponse(
            name = "name$index",
            id = index,
            sprites = getDefaultPokemonSprites(index),
        )
    }

    private fun getDefaultPokemonSprites(index: Int) = PokemonSprites(
        frontDefault = "frontDefault$index"
    )
    // endregion

    // region OUT
    private fun getExpectedPokemonViewStates(size: Int = DEFAULT_LIST_SIZE): List<PokemonViewState> = List(size) { index: Int ->
        PokemonViewState(
            id = index,
            name = "Name$index",
            imageUrl = "frontDefault$index",
            number = index.toString()
        )
    }
    // endregion
}