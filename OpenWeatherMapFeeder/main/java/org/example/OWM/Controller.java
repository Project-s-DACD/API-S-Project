package main.java.org.example.OWM;

import main.java.org.example.OWM.domain.LocationWeather;
import main.java.org.example.OWM.infrastructure.ports.WeatherProvider;
import main.java.org.example.OWM.infrastructure.ports.WeatherStorage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Controller {
    private static final Logger log = LoggerFactory.getLogger(Controller.class);

    private final WeatherProvider provider;
    private final WeatherStorage  storage;
    private final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor();

    public Controller(WeatherProvider provider,
                      WeatherStorage storage) {
        this.provider = provider;
        this.storage  = storage;
    }

    /**
     * Arranca el ciclo: cada hora obtiene el clima para todas
     * las ciudades que gestiona el provider y lo guarda.
     */
    public void execute() {
        log.info("Arrancando fetch periódico de clima cada hora");
        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(() -> {
                            // Solo llamamos a provide() una vez
                            List<LocationWeather> all = provider.provide();
                            for (LocationWeather w : all) {
                                // w.getCity() viene de tu POJO, que ahora lleva el campo city
                                storage.save(w);
                                log.info("Saved weather for {}: {}", w.getCity(), w);
                            }
                        },
                        0,               // initial delay
                        1,               // period
                        TimeUnit.HOURS
                );
    }
}
