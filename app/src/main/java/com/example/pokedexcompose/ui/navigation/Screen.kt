package com.example.pokedexcompose.ui.navigation

sealed class Screen(val route: String) {
    object PokemonListScreen: Screen("pokemon_list_screen")
    object PokemonDetailsScreen: Screen("pokemon_details_screen")

}
