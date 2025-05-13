package org.example.OWM.infrastructure.ports;

import org.example.OWM.domain.LocationWeather;

public interface WeatherStorage {
    /** Almacena el clima; maneja internamente errores. */
    void save(String city, LocationWeather weather);
}