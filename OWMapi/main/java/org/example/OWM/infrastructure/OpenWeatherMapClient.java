package org.example.OWM.infrastructure;

import org.example.OWM.domain.LocationWeather;
import org.example.OWM.infrastructure.adapter.WeatherConverter;
import org.example.OWM.infrastructure.ports.WeatherProvider;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

public class OpenWeatherMapClient implements WeatherProvider {
    private final String apiKey;
    private final String baseUrl;
    private final HttpClient client;
    private final List<String> cities = List.of(
            "Madrid,ES",
            "London,UK",
            "Paris,FR"
            // …añade más si quieres
    );

    public OpenWeatherMapClient(String baseUrl, String apiKey) {
        this.baseUrl = baseUrl;
        this.apiKey  = apiKey;
        this.client  = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    /**
     * Implementación de la interfaz: llama al API para una sola ciudad
     */
    @Override
    public Optional<LocationWeather> getCurrentWeather(String city) {
        try {
            String url = String.format(
                    "%s?q=%s&units=metric&lang=es&appid=%s",
                    baseUrl, city, apiKey
            );
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(10))
                    .GET()
                    .header("Accept", "application/json")
                    .build();
            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
            if (resp.statusCode() != 200 || resp.body() == null) {
                return Optional.empty();
            }
            JsonObject root = JsonParser.parseString(resp.body()).getAsJsonObject();
            return Optional.of(WeatherConverter.fromJson(root));
        } catch (Exception e) {
            // log.error("Error en OWM API: {}", e.getMessage());
            return Optional.empty();
        }
    }

    /** Devuelve la lista de ciudades que este cliente maneja internamente */
    public List<String> getCities() {
        return cities;
    }
}