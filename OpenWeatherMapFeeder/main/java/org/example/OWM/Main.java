package main.java.org.example.OWM;

import main.java.org.example.OWM.infrastructure.OpenWeatherMapClient;
import main.java.org.example.OWM.infrastructure.adapter.ActiveMqWeatherStorage;
import main.java.org.example.OWM.infrastructure.ports.WeatherProvider;
import main.java.org.example.OWM.infrastructure.ports.WeatherStorage;

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