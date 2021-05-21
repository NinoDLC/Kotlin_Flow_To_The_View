package fr.delcey.pokedexfullflow.ui.list

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.addRepeatingJob
import dagger.hilt.android.AndroidEntryPoint
import fr.delcey.pokedexfullflow.databinding.PokemonActivityBinding
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class PokemonActivity : AppCompatActivity() {

    private lateinit var binding: PokemonActivityBinding

    private val pokemonViewModel : PokemonViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = PokemonActivityBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val adapter = PokemonAdapter { pokemonViewModel.onPokemonClicked(it) }
        binding.pokemonsRv.adapter = adapter

        addRepeatingJob(Lifecycle.State.STARTED) {
            Log.d("Nino", "onCreate().addRepeatingJob called")
            pokemonViewModel.uiStateFlow.collect {
                Log.d("Nino", "PokemonActivity : onCreate().collect called, view has to display ${it.size} items")
                adapter.submitList(it)
            }
        }
    }
}