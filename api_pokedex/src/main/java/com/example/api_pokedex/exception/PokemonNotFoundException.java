package com.example.api_pokedex.exception;

import java.util.UUID;

public class PokemonNotFoundException extends RuntimeException {

    public PokemonNotFoundException(UUID id) {
        super("Pokémon non trouvé avec l'ID : " + id);
    }

    public PokemonNotFoundException(String nom) {
        super("Pokémon non trouvé avec le nom : " + nom);
    }
    public PokemonNotFoundException(Long id) {
        super("Pokémon non trouvé avec l'ID : " + id);
    }
}