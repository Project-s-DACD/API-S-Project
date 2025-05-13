package org.example.OWM.infrastructure.adapter;

import com.google.gson.JsonObject;
import org.example.OWM.domain.Weather;

public class WeatherConverter {
    public static Weather fromJson(JsonObject root) {
        Weather w = new Weather();
        var main   = root.getAsJsonObject("main");
        var wind   = root.getAsJsonObject("wind");
        var clouds = root.getAsJsonObject("clouds");
        var rain   = root.has("rain") ? root.getAsJsonObject("rain") : null;

        w.setTemperature(main.get("temp").getAsDouble());
        w.setHumidity(main.get("humidity").getAsInt());
        w.setVisibility(root.get("visibility").getAsInt());
        w.setWindSpeed(wind.get("speed").getAsDouble());
        w.setPrecipitation(rain != null && rain.has("1h")
                ? rain.get("1h").getAsDouble()
                : 0.0);
        w.setCloudiness(clouds.get("all").getAsInt());
        return w;
    }
}