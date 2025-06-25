// src/main/java/org/example/OWM/infrastructure/ports/WeatherProvider.java
package main.java.org.example.OWM.infrastructure.ports;

import main.java.org.example.OWM.domain.LocationWeather;
import java.util.List;

public interface WeatherProvider {
    /**
     * Devuelve el clima actual de todas las ciudades configuradas.
     */
    List<LocationWeather> provide();
}