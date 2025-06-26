package org.example.batch;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.example.OWM.domain.LocationWeather;

import java.io.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class FileWeatherLoader {

    private final File baseDir;

    public FileWeatherLoader(File baseDir) {
        this.baseDir = baseDir;
    }

    public List<LocationWeather> loadEvents() {
        List<LocationWeather> result = new ArrayList<>();

        if (!baseDir.exists() || !baseDir.isDirectory()) {
            return result;
        }

        File[] files = baseDir.listFiles((dir, name) -> name.endsWith(".events"));
        if (files == null) return result;

        for (File file : files) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    try {
                        JsonObject event = JsonParser.parseString(line).getAsJsonObject();

                        Instant ts = Instant.parse(event.get("ts").getAsString());
                        String ss = event.get("ss").getAsString();
                        String city = event.get("city").getAsString();
                        double temperature = event.get("temperature").getAsDouble();
                        int humidity = event.get("humidity").getAsInt();
                        int visibility = event.get("visibility").getAsInt();
                        double windSpeed = event.get("windSpeed").getAsDouble();
                        double precipitation = event.get("precipitation").getAsDouble();
                        int cloudiness = event.get("cloudiness").getAsInt();

                        LocationWeather lw = new LocationWeather(ts, ss, city, temperature, humidity, visibility, windSpeed, precipitation, cloudiness);
                        result.add(lw);

                    } catch (Exception ignored) {}
                }
            } catch (IOException ignored) {}
        }

        return result;
    }
}
