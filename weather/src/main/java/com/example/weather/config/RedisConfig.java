package com.example.weather.config;

import com.example.weather.dto.WeatherResponseDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, WeatherResponseDTO> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, WeatherResponseDTO> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Configuration des serializers
        Jackson2JsonRedisSerializer<WeatherResponseDTO> serializer = new Jackson2JsonRedisSerializer<>(WeatherResponseDTO.class);
        ObjectMapper om = new ObjectMapper();
        serializer.setObjectMapper(om);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }
}