package com.example.weather.client;

import com.example.weather.dto.OpenWeatherMapDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class OpenWeatherMapClient {

    private final WebClient webClient;
    private final String apiKey;
    private final String lang;

    public OpenWeatherMapClient(@Value("${weather.api.url}") String apiUrl,
                                @Value("${weather.api.key}") String apiKey,
                                @Value("${weather.api.lang}") String lang) {
        this.webClient = WebClient.builder()
                .baseUrl(apiUrl)
                .build();
        this.apiKey = apiKey;
        this.lang = lang;
    }

    public Mono<OpenWeatherMapDTO> getWeather(String city) {
        log.info("Appel API OpenWeatherMap pour la ville : {}", city);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("q", city)
                        .queryParam("appid", apiKey)
                        .queryParam("units", "metric")
                        .queryParam("lang", lang)
                        .build())
                .retrieve()
                .bodyToMono(OpenWeatherMapDTO.class)
                .doOnSuccess(response -> log.info("Réponse reçue pour {}", city))
                .doOnError(error -> log.error("Erreur API pour {} : {}", city, error.getMessage()));
    }
}