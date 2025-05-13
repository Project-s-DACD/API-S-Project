package org.example.weather.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.example.weather.model.WeatherResponse;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class WeatherApiClient {
    private static final String API_KEY = System.getenv("OWM_API_KEY");
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    private final OkHttpClient client;
    private final ObjectMapper mapper;

    public WeatherApiClient() {
        if (API_KEY == null || API_KEY.isBlank()) {
            throw new IllegalStateException("La variable de entorno OWM_API_KEY no está definida");
        }
        this.client = new OkHttpClient();
        this.mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public WeatherResponse getCurrentWeather(String city) throws Exception {
        String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8);
        String url = String.format("%s?q=%s&appid=%s&units=metric",
                BASE_URL, encodedCity, API_KEY);

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Error HTTP " + response.code() + ": " + response.message());
            }
            String json = response.body().string();
            return mapper.readValue(json, WeatherResponse.class);
        }
    }
}
