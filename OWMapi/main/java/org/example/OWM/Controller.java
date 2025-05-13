package org.example.OWM;

import org.example.OWM.domain.LocationWeather;
import org.example.OWM.infrastructure.OpenWeatherMapClient;
import org.example.OWM.infrastructure.ports.WeatherProvider;

import java.util.Optional;

public class Controller {
    private final WeatherProvider provider;

    /**
     * El constructor recibe solo los parámetros de configuración
     * (baseUrl y apiKey), levanta internamente el client (con su lista
     * de ciudades) y lo deja listo para usarse.
     */
    public Controller(String baseUrl, String apiKey) {
        // Aquí mismo metemos la lista de ciudades en el client:
        this.provider = new OpenWeatherMapClient(
                baseUrl,
                apiKey);
    }

    /**
     * Recorre todas las ciudades cargadas en el provider y las imprime.
     */
    public void fetchAndPrint() {
        // Como nuestro client implementa WeatherProvider y expone getCities():
        for (String city : ((OpenWeatherMapClient) provider).getCities()) {
            Optional<LocationWeather> w = provider.getCurrentWeather(city);
            System.out.printf("%s → %s%n",
                    city,
                    w.map(Object::toString).orElse("(sin datos)")
            );
        }
    }
}