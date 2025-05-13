package org.example.OWM;

import org.example.OWM.domain.Weather;
import org.example.OWM.infrastructure.OpenWeatherMapClient;
import org.example.OWM.infrastructure.ports.WeatherProvider;

import java.util.List;
import java.util.Optional;

public class Controller {
    private final WeatherProvider provider;

    /**
     * El constructor recibe la configuración mínima
     * y construye por dentro el cliente HTTP.
     */
    public Controller(String baseUrl, String apiKey) {
        this.provider = new OpenWeatherMapClient(baseUrl, apiKey);
    }

    public void fetchAndPrint(List<String> cities) {
        for (String city : cities) {
            Optional<Weather> w = provider.getCurrentWeather(city);
            System.out.printf("%s → %s%n",
                    city,
                    w.map(Object::toString).orElse("(sin datos)")
            );
        }
    }
}