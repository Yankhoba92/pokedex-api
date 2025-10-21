package com.example.weather.service;

import com.example.weather.dto.WeatherResponseDTO;

public interface WeatherService {
    WeatherResponseDTO getWeatherForCity(String city);
}