package com.example.api_pokedex.client;

import com.example.api_pokedex.dto.WeatherDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "weather-service", url = "${weather.service.url:http://localhost:8081}")
public interface WeatherClient {

    @GetMapping("/api/weather/{city}")
    WeatherDTO getWeatherByCity(@PathVariable("city") String city);
}