package org.example.OWM.infrastructure.adapter;

import com.google.gson.JsonObject;
import org.example.OWM.domain.LocationWeather;

import java.time.Instant;

public class WeatherConverter {

    public static LocationWeather fromJson(JsonObject root) {
        Instant ts = Instant.now();
        String ss  = "feeder-OWM";

        String city = root.has("name")
                ? root.get("name").getAsString()
                : "unknown";

        JsonObject main   = root.getAsJsonObject("main");
        JsonObject wind   = root.getAsJsonObject("wind");
        JsonObject clouds = root.getAsJsonObject("clouds");
        JsonObject rain   = root.has("rain")
                ? root.getAsJsonObject("rain")
                : null;

        double temperature   = main.get("temp").getAsDouble();
        int    humidity      = main.get("humidity").getAsInt();
        int    visibility    = root.get("visibility").getAsInt();
        double windSpeed     = wind.get("speed").getAsDouble();
        double precipitation = (rain != null && rain.has("1h"))
                ? rain.get("1h").getAsDouble()
                : 0.0;
        int    cloudiness    = clouds.get("all").getAsInt();

        return new LocationWeather(
                ts,
                ss,
                city,
                temperature,
                humidity,
                visibility,
                windSpeed,
                precipitation,
                cloudiness
        );
    }
}
