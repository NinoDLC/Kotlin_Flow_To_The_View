package fr.delcey.pokedexfullflow.ui.flow

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import fr.delcey.pokedexfullflow.databinding.PokemonActivityBinding
import fr.delcey.pokedexfullflow.ui.PokemonAdapter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FlowPokemonActivity : AppCompatActivity() {

    private val viewModel by viewModels<FlowPokemonViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = PokemonActivityBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setSupportActionBar(binding.pokemonsToolbar)

        val adapter = PokemonAdapter { Toast.makeText(this, "Pokemon clicked : ${it.name}", Toast.LENGTH_LONG).show() }
        binding.pokemonsRecyclerview.adapter = adapter

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiStateFlow.collect {
                    adapter.submitList(it)
                }
            }
        }

        /*
        Could also use :

        lifecycleScope.launch {
            viewModel.uiStateFlow
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    adapter.submitList(it)
                }
        }

        or even better with the extension below :

        viewModel.uiStateFlow.collectWithLifecycle(this) {
            adapter.submitList(it)
        }
         */
    }

    private inline fun <T> Flow<T>.collectWithLifecycle(
        owner: LifecycleOwner,
        crossinline action: suspend (value: T) -> Unit
    ) = owner.lifecycleScope.launch {
        owner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            collect {
                action(it)
            }
        }
    }
}