package com.scholarship.platform.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    // Set ALLOWED_ORIGINS env var to comma-separated list in production
    // e.g. ALLOWED_ORIGINS=https://fsd-frontend-jc1h.vercel.app,https://custom-domain.com
    @Value("${allowed.origins:http://localhost:3000,http://localhost:5173,https://fsd-frontend-jc1h.vercel.app}")
    private String allowedOriginsEnv;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);

        List<String> origins = Arrays.asList(allowedOriginsEnv.split(","));
        config.setAllowedOrigins(origins);

        config.setAllowedHeaders(Arrays.asList(
            "Origin", "Content-Type", "Accept",
            "Authorization", "X-Requested-With"
        ));
        config.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
        ));
        config.setExposedHeaders(List.of("Authorization"));
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
