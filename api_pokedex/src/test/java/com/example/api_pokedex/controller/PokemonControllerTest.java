package com.example.api_pokedex.controller;

import com.example.api_pokedex.dto.PokemonWithWeatherDTO;
import com.example.api_pokedex.dto.WeatherDTO;
import com.example.api_pokedex.entity.PokemonEntity;
import com.example.api_pokedex.exception.PokemonNotFoundException;
import com.example.api_pokedex.service.PokemonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@WebMvcTest(PokemonController.class)
class PokemonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PokemonService pokemonService;

    @Autowired
    private ObjectMapper objectMapper;

    private PokemonEntity testPokemon;
    private PokemonWithWeatherDTO testPokemonWithWeather;
    private UUID testId;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();

        // Pokémon de test
        testPokemon = new PokemonEntity();
        testPokemon.setId(testId);
        testPokemon.setNom("Salamèche");
        testPokemon.setType("Feu");
        testPokemon.setVille("Paris");
        testPokemon.setNiveau(5);
        testPokemon.setPointsDeVie(39);
        testPokemon.setAttaque(52);
        testPokemon.setDefense(43);
        testPokemon.setDescription("Pokémon Lézard");

        // Météo de test
        WeatherDTO weather = new WeatherDTO(
                "Paris", "Rain", 15.0, 70.0, "Pluie modérée",
                System.currentTimeMillis(), "CACHE", 1500L
        );

        // Pokémon avec météo
        testPokemonWithWeather = PokemonWithWeatherDTO.fromPokemon(testPokemon, weather);
    }

    @Test
    void getAllPokemons() throws Exception {
        // Given
        List<PokemonEntity> pokemons = Arrays.asList(testPokemon);
        when(pokemonService.getAllPokemons()).thenReturn(pokemons);

        // When & Then
        mockMvc.perform(get("/api/pokemons"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].nom").value("Salamèche"))
                .andExpect(jsonPath("$[0].type").value("Feu"));

        verify(pokemonService, times(1)).getAllPokemons();
    }

    @Test
    void getPokemonById() throws Exception {
        // Given
        when(pokemonService.getPokemonByIdWithWeather(testId)).thenReturn(testPokemonWithWeather);

        // When & Then
        mockMvc.perform(get("/api/pokemons/{id}", testId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Salamèche"))
                .andExpect(jsonPath("$.type").value("Feu"))
                .andExpect(jsonPath("$.conditionMeteo").value("Rain"))
                .andExpect(jsonPath("$.attaqueModifiee").exists())
                .andExpect(jsonPath("$.modificateurs").exists());

        verify(pokemonService, times(1)).getPokemonByIdWithWeather(testId);
    }

    @Test
    void getPokemonsByVille() throws Exception {
        // Given
        List<PokemonWithWeatherDTO> pokemonsWithWeather = Arrays.asList(testPokemonWithWeather);
        when(pokemonService.getPokemonsByVilleWithWeather("Paris")).thenReturn(pokemonsWithWeather);

        // When & Then
        mockMvc.perform(get("/api/pokemons/ville/{ville}", "Paris"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].ville").value("Paris"))
                .andExpect(jsonPath("$[0].conditionMeteo").value("Rain"));

        verify(pokemonService, times(1)).getPokemonsByVilleWithWeather("Paris");
    }

    @Test
    void createPokemon() throws Exception {
        // Given
        PokemonEntity newPokemon = new PokemonEntity();
        newPokemon.setNom("Pikachu");
        newPokemon.setType("Electrique");
        newPokemon.setVille("Lyon");
        newPokemon.setNiveau(10);
        newPokemon.setPointsDeVie(35);
        newPokemon.setAttaque(55);
        newPokemon.setDefense(40);

        PokemonEntity savedPokemon = new PokemonEntity();
        savedPokemon.setId(UUID.randomUUID());
        savedPokemon.setNom(newPokemon.getNom());
        savedPokemon.setType(newPokemon.getType());
        savedPokemon.setVille(newPokemon.getVille());
        savedPokemon.setNiveau(newPokemon.getNiveau());
        savedPokemon.setPointsDeVie(newPokemon.getPointsDeVie());
        savedPokemon.setAttaque(newPokemon.getAttaque());
        savedPokemon.setDefense(newPokemon.getDefense());

        when(pokemonService.createPokemon(any(PokemonEntity.class))).thenReturn(savedPokemon);

        // When & Then
        mockMvc.perform(post("/api/pokemons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPokemon)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nom").value("Pikachu"))
                .andExpect(jsonPath("$.type").value("Electrique"))
                .andExpect(jsonPath("$.id").exists());

        verify(pokemonService, times(1)).createPokemon(any(PokemonEntity.class));
    }

    @Test
    void createPokemon_ValidationError() throws Exception {
        // Given - Pokémon invalide (nom manquant)
        PokemonEntity invalidPokemon = new PokemonEntity();
        invalidPokemon.setType("Electrique");

        // When & Then
        mockMvc.perform(post("/api/pokemons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPokemon)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(pokemonService, never()).createPokemon(any(PokemonEntity.class));
    }

    @Test
    void updatePokemon() throws Exception {
        // Given
        PokemonEntity updatedPokemon = new PokemonEntity();
        updatedPokemon.setId(testId);
        updatedPokemon.setNom("Reptincel");
        updatedPokemon.setType("Feu");
        updatedPokemon.setVille("Paris");
        updatedPokemon.setNiveau(16);
        updatedPokemon.setPointsDeVie(58);
        updatedPokemon.setAttaque(64);
        updatedPokemon.setDefense(58);

        when(pokemonService.updatePokemon(eq(testId), any(PokemonEntity.class))).thenReturn(updatedPokemon);

        // When & Then
        mockMvc.perform(put("/api/pokemons/{id}", testId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPokemon)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Reptincel"))
                .andExpect(jsonPath("$.niveau").value(16));

        verify(pokemonService, times(1)).updatePokemon(eq(testId), any(PokemonEntity.class));
    }

    @Test
    void deletePokemon() throws Exception {
        // Given
        doNothing().when(pokemonService).deletePokemon(testId);

        // When & Then
        mockMvc.perform(delete("/api/pokemons/{id}", testId))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(pokemonService, times(1)).deletePokemon(testId);
    }

    @Test
    void getPokemonById_NotFound() throws Exception {
        // Given
        when(pokemonService.getPokemonByIdWithWeather(testId))
                .thenThrow(new PokemonNotFoundException(testId));

        // When & Then
        mockMvc.perform(get("/api/pokemons/{id}", testId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getPokemonById_InvalidUUID() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/pokemons/{id}", "invalid-uuid"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}