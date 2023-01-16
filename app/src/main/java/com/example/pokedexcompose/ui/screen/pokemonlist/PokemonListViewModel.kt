package com.example.pokedexcompose.ui.screen.pokemonlist

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.nfc.tech.MifareUltralight.PAGE_SIZE
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.example.pokedexcompose.data.model.PokedexListEntry
import com.example.pokedexcompose.repository.PokemonRepository
import com.example.pokedexcompose.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private var curPage = 0
    var pokemonList = mutableStateOf<List<PokedexListEntry>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    private var cachedPokemonList = listOf<PokedexListEntry>()
    private var isSearchStarting = true
    var isSearching = mutableStateOf(false)

    fun searchPokemonList(query: String) {
        val listToSearch = if (isSearchStarting) {
            pokemonList.value
        } else {
            cachedPokemonList
        }

        viewModelScope.launch(Dispatchers.Default) {
            if (query.isEmpty()) {
                pokemonList.value = cachedPokemonList
                isSearching.value = false
                isSearchStarting = true
                return@launch
            }

            val results = listToSearch.filter {
                it.pokemonName.contains(query.trim(), ignoreCase = true) ||
                        it.number.toString() == query.trim()
            }
            if(isSearchStarting) {
                cachedPokemonList = pokemonList.value
                isSearchStarting = false
            }

            pokemonList.value = results
            isSearching.value = true
        }
    }

    init {
        loadingPokemonPaginated()
    }

    fun loadingPokemonPaginated() {
        viewModelScope.launch {
            isLoading.value = true
            val result = repository.getPokemonList(PAGE_SIZE, curPage * PAGE_SIZE)
            when (result) {
                is Resource.Success -> {
                    result.data?.let { pokemonResponse ->
                        endReached.value = curPage * PAGE_SIZE >= pokemonResponse.count
                        val pokedexEntries = pokemonResponse.results.map { entry ->
                            val number = if (entry.url.endsWith("/")) {
                                entry.url.dropLast(1).takeLastWhile { it.isDigit() }
                            } else {
                                entry.url.takeLastWhile { it.isDigit() }
                            }
                            val url =
                                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"
                            PokedexListEntry(
                                pokemonName = entry.name.capitalize(Locale.ROOT),
                                imageUrl = url,
                                number = number.toInt()
                            )
                        }
                        curPage++
                        loadError.value = ""
                        isLoading.value = false
                        pokemonList.value += pokedexEntries
                    }
                }
                is Resource.Error -> {
                    result.message?.let { message ->
                        loadError.value = message
                        isLoading.value = false
                    }
                }
            }
        }
    }

    fun calcDominantColor(drawable: Drawable, onFinish: (Color) -> Unit) {
        val bitmap = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)

        Palette.from(bitmap).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(Color(colorValue))
            }
        }
    }
}