package com.example.api_pokedex.service;

import com.example.api_pokedex.dto.PokemonWithWeatherDTO;
import com.example.api_pokedex.entity.PokemonEntity;
import java.util.List;
import java.util.UUID;

public interface PokemonService {
    List<PokemonEntity> getAllPokemons();
    PokemonEntity getPokemonById(UUID id);
    PokemonWithWeatherDTO getPokemonByIdWithWeather(UUID id);
    List<PokemonEntity> getPokemonsByVille(String ville);
    List<PokemonWithWeatherDTO> getPokemonsByVilleWithWeather(String ville);
    PokemonEntity createPokemon(PokemonEntity pokemon);
    PokemonEntity updatePokemon(UUID id, PokemonEntity pokemon);
    void deletePokemon(UUID id);
}