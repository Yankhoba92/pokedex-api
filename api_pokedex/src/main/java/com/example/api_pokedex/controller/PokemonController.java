package com.example.api_pokedex.controller;

import com.example.api_pokedex.dto.PokemonWithWeatherDTO;
import com.example.api_pokedex.entity.PokemonEntity;
import com.example.api_pokedex.service.PokemonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/pokemons")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // À configurer selon vos besoins
public class PokemonController {

    private final PokemonService pokemonService;

    @GetMapping
    public ResponseEntity<List<PokemonEntity>> getAllPokemons() {
        return ResponseEntity.ok(pokemonService.getAllPokemons());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PokemonWithWeatherDTO> getPokemonById(@PathVariable UUID id) {
        return ResponseEntity.ok(pokemonService.getPokemonByIdWithWeather(id));
    }

    @GetMapping("/ville/{ville}")
    public ResponseEntity<List<PokemonWithWeatherDTO>> getPokemonsByVille(@PathVariable String ville) {
        return ResponseEntity.ok(pokemonService.getPokemonsByVilleWithWeather(ville));
    }

    @PostMapping
    public ResponseEntity<PokemonEntity> createPokemon(@Valid @RequestBody PokemonEntity pokemon) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pokemonService.createPokemon(pokemon));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PokemonEntity> updatePokemon(
            @PathVariable UUID id,
            @Valid @RequestBody PokemonEntity pokemon) {
        return ResponseEntity.ok(pokemonService.updatePokemon(id, pokemon));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePokemon(@PathVariable UUID id) {
        pokemonService.deletePokemon(id);
        return ResponseEntity.noContent().build();
    }
}