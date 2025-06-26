package org.example.OWM.infrastructure.ports;

import org.example.OWM.domain.LocationWeather;
import java.util.List;

public interface WeatherProvider {
    List<LocationWeather> provide();
}