package com.example.api_pokedex.service;

import com.example.api_pokedex.dto.WeatherDTO;

public interface WeatherService {
    WeatherDTO getWeatherForCity(String city);
}