package fr.delcey.pokedexfullflow.ui.old

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import fr.delcey.pokedexfullflow.databinding.PokemonActivityBinding
import fr.delcey.pokedexfullflow.ui.PokemonAdapter

@AndroidEntryPoint
class OldPokemonActivity : AppCompatActivity() {

    private lateinit var binding: PokemonActivityBinding

    private val pokemonViewModel: OldPokemonViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = PokemonActivityBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val adapter = PokemonAdapter { Toast.makeText(this, "Pokemon clicked : ${it.name}", Toast.LENGTH_LONG).show() }
        binding.pokemonsRv.adapter = adapter

        pokemonViewModel.uiStateLiveData.observe(this) {
            adapter.submitList(it)
        }
    }
}