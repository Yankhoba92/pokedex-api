package com.example.weather.dto;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeatherResponseDTO implements Serializable {
    private String ville;
    private String condition;
    private Double temperature;
    private Double humidite;
    private String description;
    private String source;
    private Long cacheExpiresIn;
    private Long timestamp;
}