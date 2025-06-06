// src/main/java/org/example/owm/ports/WeatherProvider.java
package org.example.OWM.infrastructure.ports;

import org.example.OWM.domain.LocationWeather;
import java.util.Optional;

public interface WeatherProvider {
    /** Devuelve el clima actual de la ciudad, o empty() en caso de error. */
    Optional<LocationWeather> getCurrentWeather(String city);
}