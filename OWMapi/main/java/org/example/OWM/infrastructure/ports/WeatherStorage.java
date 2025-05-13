package org.example.OWM.infrastructure.ports;

import org.example.OWM.domain.Weather;
import org.example.OWM.domain.Weather;

public interface WeatherStorage {
    /** Almacena el clima; maneja internamente errores. */
    void save(String city, Weather weather);
}