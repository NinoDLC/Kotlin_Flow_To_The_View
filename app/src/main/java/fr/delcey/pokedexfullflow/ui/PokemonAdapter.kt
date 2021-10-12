package fr.delcey.pokedexfullflow.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fr.delcey.pokedexfullflow.databinding.PokemonItemviewBinding
import fr.delcey.pokedexfullflow.ui.PokemonAdapter.PokemonViewHolder

class PokemonAdapter(private val listener: (PokemonViewState) -> Unit) : ListAdapter<PokemonViewState, PokemonViewHolder>(PokemonDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        return PokemonViewHolder(
            PokemonItemviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }

    class PokemonViewHolder(private val binding: PokemonItemviewBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(pokemonViewState: PokemonViewState, listener: (PokemonViewState) -> Unit) {

            Glide.with(binding.pokemonItemviewIv)
                .load(pokemonViewState.imageUrl)
                .fitCenter()
                .into(binding.pokemonItemviewIv)

            binding.pokemonItemviewTvName.text = pokemonViewState.name
            binding.pokemonItemviewTvNumber.text = pokemonViewState.number

            itemView.setOnClickListener { listener(pokemonViewState) }
        }
    }

    private class PokemonDiffCallback : DiffUtil.ItemCallback<PokemonViewState>() {
        override fun areItemsTheSame(oldItem: PokemonViewState, newItem: PokemonViewState) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: PokemonViewState, newItem: PokemonViewState) = oldItem == newItem
    }
}