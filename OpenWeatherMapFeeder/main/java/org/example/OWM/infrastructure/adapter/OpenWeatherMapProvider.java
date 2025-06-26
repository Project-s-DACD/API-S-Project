// src/main/java/org/example/OWM/infrastructure/adapter/OpenWeatherMapProvider.java
package org.example.OWM.infrastructure.adapter;

import org.example.OWM.domain.LocationWeather;
import org.example.OWM.infrastructure.OpenWeatherMapClient;
import org.example.OWM.infrastructure.ports.WeatherProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Proveedor de clima que recorre las ciudades configuradas,
 * llama al API y convierte cada respuesta JSON en LocationWeather.
 */
public class OpenWeatherMapProvider implements WeatherProvider {
    private static final Logger log = LoggerFactory.getLogger(OpenWeatherMapProvider.class);

    private final OpenWeatherMapClient apiClient;

    /**
     * @param baseUrl URL base de OpenWeatherMap (sin ?q=...)
     * @param apiKey  tu clave de OpenWeatherMap
     * @param cities  lista de "Ciudad,PAÍS" a consultar
     */
    public OpenWeatherMapProvider(String baseUrl, String apiKey, List<String> cities) {
        this.apiClient = new OpenWeatherMapClient(baseUrl, apiKey, cities);
    }

    /**
     * Recorre todas las ciudades, obtiene el JSON crudo
     * y lo convierte en LocationWeather con WeatherConverter.
     */
    @Override
    public List<LocationWeather> provide() {
        List<LocationWeather> result = new ArrayList<>();
        for (String city : apiClient.getCities()) {
            try {
                apiClient.getCurrentWeather(city)
                        .ifPresent(result::add);
            } catch (Exception e) {
                log.error("Error obteniendo clima para {}: {}", city, e.getMessage(), e);
            }
        }
        return result;
    }
}