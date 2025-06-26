// src/main/java/org/example/OWM/infrastructure/ports/WeatherProvider.java
package org.example.OWM.infrastructure.ports;

import org.example.OWM.domain.LocationWeather;
import java.util.List;

public interface WeatherProvider {
    /**
     * Devuelve el clima actual de todas las ciudades configuradas.
     */
    List<LocationWeather> provide();
}