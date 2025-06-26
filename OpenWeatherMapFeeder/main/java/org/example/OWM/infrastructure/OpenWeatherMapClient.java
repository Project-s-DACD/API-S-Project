package org.example.OWM.infrastructure;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.example.OWM.domain.LocationWeather;
import org.example.OWM.infrastructure.adapter.WeatherConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Cliente HTTP para OpenWeatherMap que maneja internamente una lista de coordenadas.
 */
public class OpenWeatherMapClient {
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();

    private final String baseUrl;
    private final String apiKey;
    private final List<double[]> coords;

    /**
     * @param baseUrl URL base de la API (por ejemplo https://api.openweathermap.org/data/2.5/weather)
     * @param apiKey  Tu clave de API de OpenWeatherMap
     * @param coords  Lista de pares [lat, lon] a consultar
     */
    public OpenWeatherMapClient(String baseUrl, String apiKey, List<double[]> coords) {
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
        // guardamos una copia inmutable de las coordenadas
        this.coords = Collections.unmodifiableList(new ArrayList<>(coords));
    }

    /**
     * Consulta la API para todas las coordenadas configuradas y devuelve la lista de LocationWeather.
     */
    public List<LocationWeather> fetchAll() {
        List<LocationWeather> result = new ArrayList<>();
        for (double[] latLon : coords) {
            fetchWeather(latLon[0], latLon[1])
                    .ifPresent(result::add);
        }
        return result;
    }

    /**
     * Consulta la API para una sola coordenada.
     * @param lat latitud
     * @param lon longitud
     * @return Optional con LocationWeather si éxito, o empty si falla
     */
    public Optional<LocationWeather> fetchWeather(double lat, double lon) {
        String url = String.format(
                "%s?lat=%.6f&lon=%.6f&units=metric&lang=es&appid=%s",
                baseUrl, lat, lon, apiKey
        );
        Request req = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Accept", "application/json")
                .build();

        try (Response resp = client.newCall(req).execute()) {
            if (!resp.isSuccessful() || resp.body() == null) {
                return Optional.empty();
            }
            JsonObject root = gson.fromJson(resp.body().string(), JsonObject.class);
            return Optional.of(WeatherConverter.fromJson(root));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    /**
     * Devuelve la lista de coordenadas manejadas.
     */
    public List<double[]> getCoords() {
        return coords;
    }
}