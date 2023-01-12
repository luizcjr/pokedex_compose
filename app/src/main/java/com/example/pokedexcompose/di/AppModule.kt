package com.example.pokedexcompose.di

import com.example.pokedexcompose.data.remote.PokemonService
import com.example.pokedexcompose.repository.PokemonRepositoryImpl
import com.example.pokedexcompose.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providePokemonRepository(
        pokemonService: PokemonService
    ) = PokemonRepositoryImpl(pokemonService)

    @Singleton
    @Provides
    fun providePokeService(): PokemonService =
        Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(PokemonService::class.java)
}