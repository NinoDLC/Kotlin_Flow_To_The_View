package fr.delcey.pokedexfullflow.ui.livedata

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import fr.delcey.pokedexfullflow.R
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
        title = this::class.java.simpleName

        val adapter = PokemonAdapter { Toast.makeText(this, "Pokemon clicked : ${it.name}", Toast.LENGTH_LONG).show() }
        binding.pokemonsRecyclerview.adapter = adapter

        viewModel.viewStateLiveData.observe(this) {
            Log.d("LiveDataPokemonActivity", "viewStateLiveData.observe{} called")
            adapter.submitList(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.pokemons_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.pokemons_menu_refresh -> {
                viewModel.refresh()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}