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

        String baseUrl   = args[0];
        String apiKey    = args[1];
        String brokerUrl = args[2];

        List<double[]> coords = List.of(
                new double[]{27.929655298285752, -15.387054443145411}
        );

        var provider = new OpenWeatherMapProvider(baseUrl, apiKey, coords);
        var storage  = new ActiveMqWeatherStorage(brokerUrl);

        new Controller(provider, storage).execute();
    }
}