package com.example.pokedexcompose.repository

import com.example.pokedexcompose.data.remote.PokemonService
import com.example.pokedexcompose.data.remote.responses.PokemonListResponse
import com.example.pokedexcompose.data.remote.responses.PokemonResponse
import com.example.pokedexcompose.util.Resource
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val service: PokemonService
) : PokemonRepository {

    override suspend fun getPokemonList(limit: Int, offset: Int): Resource<PokemonListResponse> {
        val response = try {
            service.getPokemonList(limit = limit, offset = offset)
        } catch (e: Exception) {
            return Resource.Error(message = "An unknown error occured")
        }

        return Resource.Success(data = response)
    }

    override suspend fun getPokemon(name: String): Resource<PokemonResponse> {
        val response = try {
            service.getPokemonInfo(name = name)
        } catch (e: Exception) {
            return Resource.Error(message = "An unknown error occured")
        }

        return Resource.Success(data = response)
    }
}