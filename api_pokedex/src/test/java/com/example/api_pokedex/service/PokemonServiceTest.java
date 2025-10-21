package com.example.api_pokedex.service;

import com.example.api_pokedex.dto.PokemonWithWeatherDTO;
import com.example.api_pokedex.dto.WeatherDTO;
import com.example.api_pokedex.entity.PokemonEntity;
import com.example.api_pokedex.exception.PokemonNotFoundException;
import com.example.api_pokedex.repository.PokemonRepository;
import com.example.api_pokedex.service.impl.PokemonServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PokemonServiceTest {

    @Mock
    private PokemonRepository pokemonRepository;

    @Mock
    private WeatherService weatherService;

    @InjectMocks
    private PokemonServiceImpl pokemonService;

    private UUID testId;
    private PokemonEntity testPokemon;
    private WeatherDTO testWeather;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();

        testPokemon = new PokemonEntity();
        testPokemon.setId(testId);
        testPokemon.setNom("Salamèche");
        testPokemon.setType("Feu");
        testPokemon.setVille("Paris");
        testPokemon.setNiveau(5);
        testPokemon.setPointsDeVie(39);
        testPokemon.setAttaque(52);
        testPokemon.setDefense(43);

        testWeather = new WeatherDTO(
                "Paris", "Rain", 15.0, 70.0, "Pluie",
                System.currentTimeMillis(), "CACHE", 1000L
        );
    }

    @Test
    void getAllPokemons() {
        // Given
        List<PokemonEntity> pokemons = Arrays.asList(testPokemon);
        when(pokemonRepository.findAll()).thenReturn(pokemons);

        // When
        List<PokemonEntity> result = pokemonService.getAllPokemons();

        // Then
        assertEquals(1, result.size());
        assertEquals("Salamèche", result.get(0).getNom());
        verify(pokemonRepository, times(1)).findAll();
    }

    @Test
    void getPokemonByIdWithWeather_Success() {
        // Given
        when(pokemonRepository.findById(testId)).thenReturn(Optional.of(testPokemon));
        when(weatherService.getWeatherForCity("Paris")).thenReturn(testWeather);

        // When
        PokemonWithWeatherDTO result = pokemonService.getPokemonByIdWithWeather(testId);

        // Then
        assertNotNull(result);
        assertEquals("Salamèche", result.getNom());
        assertEquals("Rain", result.getConditionMeteo());
        assertTrue(result.getAttaqueModifiee() < testPokemon.getAttaque()); // Feu + Pluie = malus
    }

    @Test
    void getPokemonById_NotFound() {
        // Given
        when(pokemonRepository.findById(testId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(PokemonNotFoundException.class,
                () -> pokemonService.getPokemonById(testId));
    }

    @Test
    void createPokemon_Success() {
        // Given
        when(pokemonRepository.save(any(PokemonEntity.class))).thenReturn(testPokemon);

        // When
        PokemonEntity result = pokemonService.createPokemon(testPokemon);

        // Then
        assertNotNull(result);
        assertEquals("Salamèche", result.getNom());
        verify(pokemonRepository, times(1)).save(testPokemon);
    }

    @Test
    void deletePokemon_Success() {
        // Given
        when(pokemonRepository.findById(testId)).thenReturn(Optional.of(testPokemon));

        // When
        pokemonService.deletePokemon(testId);

        // Then
        verify(pokemonRepository, times(1)).delete(testPokemon);
    }
}