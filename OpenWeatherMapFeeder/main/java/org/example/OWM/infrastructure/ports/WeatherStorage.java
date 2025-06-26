package org.example.OWM.infrastructure.ports;

import org.example.OWM.domain.LocationWeather;

public interface WeatherStorage {
    void save(LocationWeather weather);
}