package com.example.weather.controller;

import com.example.weather.dto.WeatherResponseDTO;
import com.example.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/{city}")
    public ResponseEntity<WeatherResponseDTO> getWeatherByCity(@PathVariable String city) {
        log.info("Requête météo reçue pour la ville : {}", city);
        WeatherResponseDTO weather = weatherService.getWeatherForCity(city);
        return ResponseEntity.ok(weather);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Weather service is running!");
    }
}