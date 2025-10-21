package com.example.api_pokedex.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherDTO {
    private String ville;
    private String condition;
    private Double temperature;
    private Double humidite;
    private String description;
    private Long timestamp;
    private String source;
    private Long cacheExpiresIn;
}