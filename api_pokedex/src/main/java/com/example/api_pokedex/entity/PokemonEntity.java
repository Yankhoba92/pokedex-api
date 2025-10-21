package com.example.api_pokedex.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import java.util.UUID;

@Entity
@Table(name="pokemons")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class PokemonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @Column(nullable = false)
    @NotBlank(message = "Le type est obligatoire")
    private String type;

    @Column(nullable = false)
    @NotBlank(message = "La ville est obligatoire")
    private String ville;

    @Column(nullable = false)
    private Integer niveau;

    @Column(name = "points_de_vie", nullable = false)
    private Integer pointsDeVie;

    @Column(nullable = false)
    private Integer attaque;

    @Column(nullable = false)
    private Integer defense;

    @Column(columnDefinition = "TEXT")
    private String description;

}