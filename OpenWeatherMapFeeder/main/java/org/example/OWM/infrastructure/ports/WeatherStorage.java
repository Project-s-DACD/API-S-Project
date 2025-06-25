package main.java.org.example.OWM.infrastructure.ports;

import main.java.org.example.OWM.domain.LocationWeather;

public interface WeatherStorage {
    void save(LocationWeather weather);
}