package com.example.pokedexcompose.repository

import com.example.pokedexcompose.data.remote.responses.PokemonListResponse
import com.example.pokedexcompose.data.remote.responses.PokemonResponse
import com.example.pokedexcompose.util.Resource

interface PokemonRepository {
    suspend fun getPokemonList(limit: Int, offset: Int): Resource<PokemonListResponse>
    suspend fun getPokemon(name: String): Resource<PokemonResponse>
}