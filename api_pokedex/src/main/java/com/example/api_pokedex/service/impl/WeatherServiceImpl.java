package com.example.api_pokedex.service.impl;

import com.example.api_pokedex.client.WeatherClient;
import com.example.api_pokedex.dto.WeatherDTO;
import com.example.api_pokedex.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherServiceImpl implements WeatherService {

    private final WeatherClient weatherClient;

    @Override
    public WeatherDTO getWeatherForCity(String city) {
        try {
            log.info("Appel du service météo pour la ville : {}", city);
            return weatherClient.getWeatherByCity(city);
        } catch (Exception e) {
            log.error("Erreur lors de l'appel au service météo : ", e);
            // Retourner une météo par défaut en cas d'erreur
            return new WeatherDTO(city, "Clear", 20.0, 50.0, "Météo non disponible", System.currentTimeMillis(), "ERROR", 0L);
        }
    }
}