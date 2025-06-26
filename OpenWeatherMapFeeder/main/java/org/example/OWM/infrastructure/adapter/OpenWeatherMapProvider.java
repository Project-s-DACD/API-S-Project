// src/main/java/org/example/OWM/infrastructure/adapter/OpenWeatherMapProvider.java
package org.example.OWM.infrastructure.adapter;

import org.example.OWM.domain.LocationWeather;
import org.example.OWM.infrastructure.OpenWeatherMapClient;
import org.example.OWM.infrastructure.ports.WeatherProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * Proveedor de clima que delega toda la lógica en OpenWeatherMapClient.
 */
public class OpenWeatherMapProvider implements WeatherProvider {
    private static final Logger log = LoggerFactory.getLogger(OpenWeatherMapProvider.class);

    private final OpenWeatherMapClient apiClient;

    /**
     * @param baseUrl URL base de OpenWeatherMap (sin parámetros)
     * @param apiKey  Tu clave de API de OpenWeatherMap
     * @param coords  Lista de pares [latitud, longitud]
     */
    public OpenWeatherMapProvider(String baseUrl, String apiKey, List<double[]> coords) {
        this.apiClient = new OpenWeatherMapClient(baseUrl, apiKey, coords);
    }

    /**
     * Devuelve la lista completa de LocationWeather consultando todas las coordenadas.
     */
    @Override
    public List<LocationWeather> provide() {
        try {
            return apiClient.fetchAll();
        } catch (Exception e) {
            log.error("Error providing weather data: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}

