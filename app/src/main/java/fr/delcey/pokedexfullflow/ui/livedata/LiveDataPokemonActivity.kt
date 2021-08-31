package fr.delcey.pokedexfullflow.ui.livedata

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import fr.delcey.pokedexfullflow.databinding.PokemonActivityBinding
import fr.delcey.pokedexfullflow.ui.PokemonAdapter

@AndroidEntryPoint
class LiveDataPokemonActivity : AppCompatActivity() {

    private val viewModel by viewModels<LiveDataPokemonViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = PokemonActivityBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setSupportActionBar(binding.pokemonsToolbar)

        val adapter = PokemonAdapter { Toast.makeText(this, "Pokemon clicked : ${it.name}", Toast.LENGTH_LONG).show() }
        binding.pokemonsRecyclerview.adapter = adapter

        viewModel.uiStateLiveData.observe(this) {
            adapter.submitList(it)
        }
    }
}