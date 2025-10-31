package edu.basedatos.oracleexample.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración Web de la aplicación
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configuración de CORS para permitir peticiones desde diferentes orígenes
     * En producción, debes especificar los orígenes permitidos
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("*")  // En producción: especificar dominios permitidos
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .maxAge(3600);
    }
}