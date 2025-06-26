package org.example.OWM.infrastructure.adapter;

import org.example.OWM.domain.LocationWeather;
import org.example.OWM.infrastructure.OpenWeatherMapClient;
import org.example.OWM.infrastructure.ports.WeatherProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;


public class OpenWeatherMapProvider implements WeatherProvider {
    private static final Logger log = LoggerFactory.getLogger(OpenWeatherMapProvider.class);

    private final OpenWeatherMapClient apiClient;

    public OpenWeatherMapProvider(String baseUrl, String apiKey, List<double[]> coords) {
        this.apiClient = new OpenWeatherMapClient(baseUrl, apiKey, coords);
    }

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

