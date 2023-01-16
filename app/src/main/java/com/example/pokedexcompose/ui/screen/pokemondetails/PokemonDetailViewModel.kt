package com.example.pokedexcompose.ui.screen.pokemondetails

import androidx.lifecycle.ViewModel
import com.example.pokedexcompose.data.remote.responses.PokemonResponse
import com.example.pokedexcompose.repository.PokemonRepository
import com.example.pokedexcompose.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    suspend fun getPokemonInfo(pokemonName: String): Resource<PokemonResponse> =
        repository.getPokemon(pokemonName)
}