package com.example.api_pokedex.dto;

import com.example.api_pokedex.entity.PokemonEntity;
import lombok.Data;
import lombok.Builder;
import java.util.UUID;

@Data
@Builder
public class PokemonWithWeatherDTO {
    // Données du Pokémon
    private UUID id;
    private String nom;
    private String type;
    private String ville;
    private Integer niveau;
    private Integer pointsDeVie;
    private Integer attaque;
    private Integer defense;
    private String description;
    private String imageUrl;

    // Stats modifiées selon la météo
    private Integer attaqueModifiee;
    private Integer defenseModifiee;
    private Integer pointsDeVieModifies;

    // Info météo
    private String conditionMeteo;
    private Double temperature;
    private String descriptionMeteo;
    private String modificateurs;

    private String weatherSource;
    private Long cacheExpiresIn;

    public static PokemonWithWeatherDTO fromPokemon(PokemonEntity pokemon, WeatherDTO weather) {
        PokemonWithWeatherDTO dto = PokemonWithWeatherDTO.builder()
                .id(pokemon.getId())
                .nom(pokemon.getNom())
                .type(pokemon.getType())
                .ville(pokemon.getVille())
                .niveau(pokemon.getNiveau())
                .pointsDeVie(pokemon.getPointsDeVie())
                .attaque(pokemon.getAttaque())
                .defense(pokemon.getDefense())
                .description(pokemon.getDescription())
                .conditionMeteo(weather.getCondition())
                .temperature(weather.getTemperature())
                .descriptionMeteo(weather.getDescription())

                .build();

        // Calculer les modifications selon la météo
        calculerModifications(dto, pokemon, weather);

        return dto;
    }

    private static void calculerModifications(PokemonWithWeatherDTO dto, PokemonEntity pokemon, WeatherDTO weather) {
        int attaqueMod = pokemon.getAttaque();
        int defenseMod = pokemon.getDefense();
        int pvMod = pokemon.getPointsDeVie();
        StringBuilder modificateurs = new StringBuilder();

        // Logique de modification selon type et météo
        if ("Rain".equalsIgnoreCase(weather.getCondition())) {
            if ("Feu".equalsIgnoreCase(pokemon.getType())) {
                attaqueMod = (int) (attaqueMod * 0.8); // -20%
                defenseMod = (int) (defenseMod * 0.9); // -10%
                modificateurs.append("Pluie défavorable pour type Feu (-20% attaque, -10% défense). ");
            } else if ("Eau".equalsIgnoreCase(pokemon.getType())) {
                attaqueMod = (int) (attaqueMod * 1.15); // +15%
                defenseMod = (int) (defenseMod * 1.1); // +10%
                modificateurs.append("Pluie favorable pour type Eau (+15% attaque, +10% défense). ");
            } else if ("Electrique".equalsIgnoreCase(pokemon.getType())) {
                attaqueMod = (int) (attaqueMod * 1.2); // +20%
                modificateurs.append("Pluie favorable pour type Électrique (+20% attaque). ");
            }
        } else if ("Clear".equalsIgnoreCase(weather.getCondition()) || "Sunny".equalsIgnoreCase(weather.getCondition())) {
            if ("Feu".equalsIgnoreCase(pokemon.getType())) {
                attaqueMod = (int) (attaqueMod * 1.2); // +20%
                modificateurs.append("Soleil favorable pour type Feu (+20% attaque). ");
            } else if ("Eau".equalsIgnoreCase(pokemon.getType())) {
                attaqueMod = (int) (attaqueMod * 0.85); // -15%
                modificateurs.append("Soleil défavorable pour type Eau (-15% attaque). ");
            } else if ("Plante".equalsIgnoreCase(pokemon.getType())) {
                pvMod = (int) (pvMod * 1.1); // +10% PV
                modificateurs.append("Soleil favorable pour type Plante (+10% PV). ");
            }
        }

        if (modificateurs.length() == 0) {
            modificateurs.append("Aucune modification due à la météo.");
        }

        dto.setAttaqueModifiee(attaqueMod);
        dto.setDefenseModifiee(defenseMod);
        dto.setPointsDeVieModifies(pvMod);
        dto.setModificateurs(modificateurs.toString());
        dto.setWeatherSource(weather.getSource());
        dto.setCacheExpiresIn(weather.getCacheExpiresIn());
    }
}