package com.example.demo;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/processInput") // Adjust the mapping pattern as needed
                .allowedOrigins("http://localhost:3000") // Add the origin of your React app
                // .allowedOrigins("https://github.com/ethanlim04/creditcardform") // Add the origin of your React app
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(true)
                .allowedHeaders("*")
                .exposedHeaders("Authorization")
                .maxAge(3600);
        registry.addMapping("/api/updateInput") // Adjust the mapping pattern as needed
                .allowedOrigins("http://localhost:3000") // Add the origin of your React app
                // .allowedOrigins("https://github.com/ethanlim04/creditcardform") // Add the origin of your React app
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(true)
                .allowedHeaders("*")
                .exposedHeaders("Authorization")
                .maxAge(3600);
        registry.addMapping("/api/user") // Adjust the mapping pattern as needed
                .allowedOrigins("http://localhost:3000") // Add the origin of your React app
                // .allowedOrigins("https://github.com/ethanlim04/creditcardform") // Add the origin of your React app
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(true)
                .allowedHeaders("*")
                .exposedHeaders("Authorization")
                .maxAge(3600);
    }
}