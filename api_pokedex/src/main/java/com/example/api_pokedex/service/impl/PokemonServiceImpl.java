package com.example.api_pokedex.service.impl;

import com.example.api_pokedex.dto.PokemonWithWeatherDTO;
import com.example.api_pokedex.dto.WeatherDTO;
import com.example.api_pokedex.entity.PokemonEntity;
import com.example.api_pokedex.exception.PokemonNotFoundException;
import com.example.api_pokedex.repository.PokemonRepository;
import com.example.api_pokedex.service.PokemonService;
import com.example.api_pokedex.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PokemonServiceImpl implements PokemonService {

    private final PokemonRepository pokemonRepository;
    private final WeatherService weatherService;

    @Override
    public List<PokemonEntity> getAllPokemons() {
        log.info("Récupération de tous les Pokémon");
        return pokemonRepository.findAll();
    }

    @Override
    public PokemonEntity getPokemonById(UUID id) {
        log.info("Récupération du Pokémon avec l'ID : {}", id);
        return pokemonRepository.findById(id)
                .orElseThrow(() -> new PokemonNotFoundException(id));
    }

    @Override
    public PokemonWithWeatherDTO getPokemonByIdWithWeather(UUID id) {
        log.info("Récupération du Pokémon {} avec météo", id);
        PokemonEntity pokemon = getPokemonById(id);
        WeatherDTO weather = weatherService.getWeatherForCity(pokemon.getVille());
        return PokemonWithWeatherDTO.fromPokemon(pokemon, weather);
    }

    @Override
    public List<PokemonEntity> getPokemonsByVille(String ville) {
        log.info("Récupération des Pokémon de la ville : {}", ville);
        return pokemonRepository.findByVille(ville);
    }

    @Override
    public List<PokemonWithWeatherDTO> getPokemonsByVilleWithWeather(String ville) {
        log.info("Récupération des Pokémon de {} avec météo", ville);
        List<PokemonEntity> pokemons = pokemonRepository.findByVille(ville);
        WeatherDTO weather = weatherService.getWeatherForCity(ville);

        return pokemons.stream()
                .map(pokemon -> PokemonWithWeatherDTO.fromPokemon(pokemon, weather))
                .collect(Collectors.toList());
    }

    @Override
    public PokemonEntity createPokemon(PokemonEntity pokemon) {
        log.info("Création d'un nouveau Pokémon : {}", pokemon.getNom());
        return pokemonRepository.save(pokemon);
    }

    @Override
    public PokemonEntity updatePokemon(UUID id, PokemonEntity pokemonDetails) {
        log.info("Mise à jour du Pokémon : {}", id);
        PokemonEntity pokemon = getPokemonById(id);

        pokemon.setNom(pokemonDetails.getNom());
        pokemon.setType(pokemonDetails.getType());
        pokemon.setVille(pokemonDetails.getVille());
        pokemon.setNiveau(pokemonDetails.getNiveau());
        pokemon.setPointsDeVie(pokemonDetails.getPointsDeVie());
        pokemon.setAttaque(pokemonDetails.getAttaque());
        pokemon.setDefense(pokemonDetails.getDefense());
        pokemon.setDescription(pokemonDetails.getDescription());

        return pokemonRepository.save(pokemon);
    }

    @Override
    public void deletePokemon(UUID id) {
        log.info("Suppression du Pokémon : {}", id);
        PokemonEntity pokemon = getPokemonById(id);
        pokemonRepository.delete(pokemon);
    }
}