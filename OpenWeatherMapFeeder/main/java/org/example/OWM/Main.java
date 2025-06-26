package org.example.OWM;

import org.example.OWM.infrastructure.adapter.OpenWeatherMapProvider;
import org.example.OWM.infrastructure.adapter.ActiveMqWeatherStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        if (args.length < 3) {
            log.warn("Usage: <baseUrl> <apiKey> <brokerUrl>");
            return;
        }

        String baseUrl   = args[0];          // e.g. https://api.openweathermap.org/data/2.5/weather
        String apiKey    = args[1];          // tu API key de OWM
        String brokerUrl = args[2];          // e.g. tcp://localhost:61616

        // Coordenadas fijas definidas directamente en el código:
        List<double[]> coords = List.of(
                new double[]{27.929655298285752, -15.387054443145411}
        );

        // Creamos el provider y el storage justo con esas coords
        var provider = new OpenWeatherMapProvider(baseUrl, apiKey, coords);
        var storage  = new ActiveMqWeatherStorage(brokerUrl);

        new Controller(provider, storage).execute();
    }
}