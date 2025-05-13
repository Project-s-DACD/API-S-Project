package org.example.OWM;

import org.example.OWM.Controller;
import org.example.OWM.infrastructure.OpenWeatherMapClient;
import org.example.OWM.infrastructure.ports.WeatherProvider;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        String apiKey  = args[0];
        String baseUrl = args[1];

        // Aquí el controller crea su propio OpenWeatherMapClient
        var controller = new Controller(baseUrl, apiKey);

        List<String> cities = List.of("Madrid,ES", "London,UK", "Paris,FR");
        controller.fetchAndPrint(cities);
    }
}