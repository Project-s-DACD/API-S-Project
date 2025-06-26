package org.example.OWM;

import org.example.OWM.infrastructure.OpenWeatherMapClient;
import org.example.OWM.infrastructure.adapter.ActiveMqWeatherStorage;
import org.example.OWM.infrastructure.ports.WeatherProvider;
import org.example.OWM.infrastructure.ports.WeatherStorage;
import org.example.OWM.Controller;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        String apiKey  = args[0];
        String baseUrl = args[1];
        List<String> cities = List.of(
                "Madrid,ES",
                "London,UK",
                "Paris,FR");

        WeatherProvider provider = new OpenWeatherMapClient(baseUrl, apiKey, cities);
        WeatherStorage  storage  = new ActiveMqWeatherStorage(
                "ConnectionFactory",
                "weather.queue"
        );

        Controller controller = new Controller(provider, storage);
        controller.execute();
    }
}