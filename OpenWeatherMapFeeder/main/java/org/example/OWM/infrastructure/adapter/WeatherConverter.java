package org.example.OWM.infrastructure.adapter;

import com.google.gson.JsonObject;
import org.example.OWM.domain.LocationWeather;

public class WeatherConverter {
    public static LocationWeather fromJson(JsonObject root) {
        var main = root.getAsJsonObject("main");
        var wind = root.getAsJsonObject("wind");
        var clouds = root.getAsJsonObject("clouds");
        var rain = root.has("rain") ? root.getAsJsonObject("rain") : null;

        double temperature = main.get("temp").getAsDouble();
        int humidity = main.get("humidity").getAsInt();
        int visibility = root.get("visibility").getAsInt();
        double windSpeed = wind.get("speed").getAsDouble();
        double precipitation = (rain != null && rain.has("1h"))
                ? rain.get("1h").getAsDouble()
                : 0.0;
        int cloudiness = clouds.get("all").getAsInt();

        return new LocationWeather(
                temperature,
                humidity,
                visibility,
                windSpeed,
                precipitation,
                cloudiness
        );
    }
}