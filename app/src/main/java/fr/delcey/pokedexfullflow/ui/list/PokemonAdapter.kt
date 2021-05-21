package fr.delcey.pokedexfullflow.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fr.delcey.pokedexfullflow.databinding.PokemonItemviewBinding
import fr.delcey.pokedexfullflow.ui.list.PokemonAdapter.PokemonViewHolder

class PokemonAdapter(private val listener: (PokemonUiState) -> Unit) : ListAdapter<PokemonUiState, PokemonViewHolder>(PokemonDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        return PokemonViewHolder(
            PokemonItemviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }

    class PokemonViewHolder(private val binding: PokemonItemviewBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(pokemonUiState: PokemonUiState, listener: (PokemonUiState) -> Unit) {

            Glide.with(binding.pokemonItemviewIv).load(pokemonUiState.imageUrl).fitCenter().into(binding.pokemonItemviewIv)
            binding.pokemonItemviewTvName.text = pokemonUiState.name
            binding.pokemonItemviewTvNumber.text = pokemonUiState.number

            itemView.setOnClickListener { listener(pokemonUiState) }
        }
    }

    private class PokemonDiffCallback : DiffUtil.ItemCallback<PokemonUiState>() {
        override fun areItemsTheSame(oldItem: PokemonUiState, newItem: PokemonUiState): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PokemonUiState, newItem: PokemonUiState): Boolean {
            return oldItem == newItem
        }
    }
}