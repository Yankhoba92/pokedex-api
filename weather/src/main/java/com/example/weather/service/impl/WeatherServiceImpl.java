package com.example.weather.service.impl;

import com.example.weather.client.OpenWeatherMapClient;
import com.example.weather.dto.OpenWeatherMapDTO;
import com.example.weather.dto.WeatherResponseDTO;
import com.example.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherServiceImpl implements WeatherService {

    private final OpenWeatherMapClient weatherClient;

    @Autowired
    private RedisTemplate<String, WeatherResponseDTO> redisTemplate;

    private static final String CACHE_PREFIX = "weather:";
    private static final long CACHE_TTL_MINUTES = 30;

    @Override
    public WeatherResponseDTO getWeatherForCity(String city) {
        String cacheKey = CACHE_PREFIX + city.toLowerCase();

        // 1. Vérifier le cache
        WeatherResponseDTO cachedWeather = redisTemplate.opsForValue().get(cacheKey);

        if (cachedWeather != null) {
            log.info("Météo trouvée dans le cache pour : {}", city);

            // Calculer le temps restant
            Long ttl = redisTemplate.getExpire(cacheKey, TimeUnit.SECONDS);
            cachedWeather.setSource("CACHE");
            cachedWeather.setCacheExpiresIn(ttl != null ? ttl : 0L);

            return cachedWeather;
        }

        // 2. Si pas en cache, appeler l'API
        log.info("Cache miss - Appel de l'API pour : {}", city);

        try {
            OpenWeatherMapDTO apiResponse = weatherClient.getWeather(city).block();

            if (apiResponse != null && apiResponse.getWeather() != null && !apiResponse.getWeather().isEmpty()) {
                WeatherResponseDTO weather = WeatherResponseDTO.builder()
                        .ville(city)
                        .condition(apiResponse.getWeather().get(0).getMain())
                        .temperature(apiResponse.getMain().getTemp())
                        .humidite(apiResponse.getMain().getHumidity())
                        .description(apiResponse.getWeather().get(0).getDescription())
                        .timestamp(System.currentTimeMillis())
                        .source("API")
                        .cacheExpiresIn(CACHE_TTL_MINUTES * 60) // En secondes
                        .build();

                // 3. Sauvegarder dans le cache
                redisTemplate.opsForValue().set(cacheKey, weather, CACHE_TTL_MINUTES, TimeUnit.MINUTES);
                log.info("Météo sauvegardée dans le cache pour : {}", city);

                return weather;
            }
        } catch (Exception e) {
            log.error("Erreur lors de l'appel API pour {} : {}", city, e.getMessage());
        }

        // 4. Retour par défaut en cas d'erreur
        return WeatherResponseDTO.builder()
                .ville(city)
                .condition("Clear")
                .temperature(20.0)
                .humidite(50.0)
                .description("Données non disponibles")
                .timestamp(System.currentTimeMillis())
                .source("DEFAULT")
                .cacheExpiresIn(0L)
                .build();
    }
}