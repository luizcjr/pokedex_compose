package com.example.pokedexcompose.data.remote

import com.example.pokedexcompose.data.remote.responses.PokemonListResponse
import com.example.pokedexcompose.data.remote.responses.PokemonResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonService {
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): PokemonListResponse

    @GET("pokemon/{name}")
    suspend fun getPokemonInfo(
        @Path("name") name: String
    ): PokemonResponse
}