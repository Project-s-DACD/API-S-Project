// src/main/java/org/example/OWM/infrastructure/adapter/OpenWeatherMapClient.java
package org.example.OWM.infrastructure;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.example.OWM.infrastructure.adapter.WeatherConverter;
import org.example.OWM.infrastructure.ports.WeatherProvider;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.example.OWM.domain.LocationWeather;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Cliente HTTP para OpenWeatherMap.
 * Se encarga de llamar al endpoint, parsear el JSON,
 * y delegar la conversión a LocationWeather.
 */
public class OpenWeatherMapClient implements WeatherProvider {
    private final OkHttpClient client;
    private final Gson gson;
    private final String baseUrl;
    private final String apiKey;
    private final List<String> cities;

    /**
     * @param baseUrl URL base de la API (p.ej. https://api.openweathermap.org/data/2.5/weather)
     * @param apiKey  tu clave de OpenWeatherMap
     * @param cities  lista de "Ciudad,PAÍS" a consultar
     */
    public OpenWeatherMapClient(String baseUrl, String apiKey, List<String> cities) {
        this.client  = new OkHttpClient();
        this.gson    = new Gson();
        this.baseUrl = baseUrl;
        this.apiKey  = apiKey;
        this.cities  = Collections.unmodifiableList(cities);
    }

    /**
     * Devuelve la lista de ciudades configuradas.
     */
    public List<String> getCities() {
        return cities;
    }

    /**
     * Consulta el clima para la ciudad indicada y devuelve
     * un Optional con LocationWeather si todo fue OK.
     */
    public Optional<LocationWeather> getCurrentWeather(String city) {
        try {
            String encoded = URLEncoder.encode(city, StandardCharsets.UTF_8);
            String url = String.format(
                    "%s?q=%s&units=metric&lang=es&appid=%s",
                    baseUrl, encoded, apiKey
            );

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("Accept", "application/json")
                    .build();

            try (Response resp = client.newCall(request).execute()) {
                if (!resp.isSuccessful() || resp.body() == null) {
                    return Optional.empty();
                }
                String json = resp.body().string();
                JsonObject root = gson.fromJson(json, JsonObject.class);
                return Optional.of(WeatherConverter.fromJson(root));
            }
        } catch (IOException e) {
            // podrias log.error("Error calling OWM API", e);
            return Optional.empty();
        }
    }

    @Override
    public List<LocationWeather> provide() {
        return List.of();
    }
}