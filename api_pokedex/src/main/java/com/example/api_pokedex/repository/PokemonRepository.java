package com.example.api_pokedex.repository;


import com.example.api_pokedex.entity.PokemonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PokemonRepository extends JpaRepository<PokemonEntity, UUID> {

    Optional<PokemonEntity> findByNom(String nom);

    List<PokemonEntity> findByType(String type);

    List<PokemonEntity> findByVille(String ville);

    List<PokemonEntity> findByVilleAndType(String ville, String type);

}