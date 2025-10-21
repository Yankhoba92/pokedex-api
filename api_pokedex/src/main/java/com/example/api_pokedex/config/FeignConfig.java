package com.example.api_pokedex.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.example.api_pokedex.client")
public class FeignConfig {
}