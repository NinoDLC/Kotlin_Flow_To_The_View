package fr.delcey.pokedexfullflow.ui.flow

import fr.delcey.pokedexfullflow.CoroutineToolsProvider
import fr.delcey.pokedexfullflow.data.PokemonRepository
import fr.delcey.pokedexfullflow.data.pokemon.PokemonResponse
import fr.delcey.pokedexfullflow.data.pokemon.PokemonSprites
import fr.delcey.pokedexfullflow.ui.PokemonViewState
import fr.delcey.pokedexfullflow.utils.TestCoroutineRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceTimeBy
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FlowPokemonViewModelTest {

    companion object {
        private const val DEFAULT_LIST_SIZE = 3
    }

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val pokemonRepository = mockk<PokemonRepository>()

    private val coroutineToolsProvider = mockk<CoroutineToolsProvider>()

    @Before
    fun setUp() {
        every { pokemonRepository.getPokemonsFlow() } returns flowOf(getDefaultPokemonList())

        every { coroutineToolsProvider.ioCoroutineDispatcher } returns testCoroutineRule.testCoroutineDispatcher
        every { coroutineToolsProvider.sharingStartedStrategy } returns SharingStarted.Eagerly
    }

    @Test
    fun `nominal case`() = testCoroutineRule.runBlockingTest {
        // Given
        val viewModel = FlowPokemonViewModel(pokemonRepository, coroutineToolsProvider)

        // When
        val result = viewModel.viewStateFlow.first()

        // Then
        assertEquals(getExpectedPokemonViewStates(), result)
    }

    @Test
    fun `not so nominal case - a delay between 2 values !`() = testCoroutineRule.runBlockingTest {
        // Given
        every { pokemonRepository.getPokemonsFlow() } returns flow {
            emit(getDefaultPokemonList(1))
            delay(3_000)
            emit(getDefaultPokemonList())
        }
        val viewModel = FlowPokemonViewModel(pokemonRepository, coroutineToolsProvider)

        // When
        val result1 = viewModel.viewStateFlow.first()
        advanceTimeBy(3_000)
        val result2 = viewModel.viewStateFlow.first()

        // Then
        assertEquals(getExpectedPokemonViewStates(1), result1)
        assertEquals(getExpectedPokemonViewStates(), result2)
    }

    @Test
    fun `another not so nominal case - an initial delay !`() = testCoroutineRule.runBlockingTest {
        // Given
        every { pokemonRepository.getPokemonsFlow() } returns flow {
            delay(3_000)
            emit(getDefaultPokemonList())
        }
        val viewModel = FlowPokemonViewModel(pokemonRepository, coroutineToolsProvider)

        // When
        advanceTimeBy(3_000)
        val result = viewModel.viewStateFlow.first()

        // Then
        assertEquals(getExpectedPokemonViewStates(), result)
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