package com.example.pokedexcompose.data.remote.responses

data class PokemonListResponse(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<Result>
)