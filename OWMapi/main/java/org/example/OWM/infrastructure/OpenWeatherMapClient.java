package org.example.OWM.infrastructure;

import org.example.OWM.domain.Weather;
import org.example.OWM.infrastructure.adapter.WeatherConverter;
import org.example.OWM.infrastructure.ports.WeatherProvider;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;

public class OpenWeatherMapClient implements WeatherProvider {
    private final String apiKey;
    private final String baseUrl;
    private final HttpClient client;

    public OpenWeatherMapClient(String baseUrl, String apiKey) {
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    @Override
    public Optional<Weather> getCurrentWeather(String city) {
        try {
            // Montamos la URL
            String url = String.format("%s?q=%s&units=metric&lang=es&appid=%s",
                    baseUrl, city, apiKey);
            // Log de la URL
            System.out.println("LLAMANDO A: " + url);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(10))
                    .GET()
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            // Si no es 200, imprime status + body
            if (response.statusCode() != 200) {
                System.err.printf("ERROR HTTP %d para %s%n%s%n",
                        response.statusCode(), city, response.body());
                return Optional.empty();
            }

            // Si ok, parseamos y devolvemos
            JsonObject root = JsonParser
                    .parseString(response.body())
                    .getAsJsonObject();
            return Optional.of(WeatherConverter.fromJson(root));

        } catch (Exception e) {
            System.err.printf("EXCEPCIÓN al llamar a %s: %s%n",
                    city, e.toString());
            return Optional.empty();
        }
    }
}
