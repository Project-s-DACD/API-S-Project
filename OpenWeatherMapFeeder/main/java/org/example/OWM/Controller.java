package org.example.OWM;

import org.example.OWM.domain.LocationWeather;
import org.example.OWM.infrastructure.ports.WeatherProvider;
import org.example.OWM.infrastructure.ports.WeatherStorage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
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

    public void execute() {
        log.info("Arrancando fetch periódico de clima cada hora");
        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(() -> {
                            List<LocationWeather> all = provider.provide();
                            for (LocationWeather w : all) {
                                storage.save(w);
                                log.info("Saved weather for {}: {}", w.getCity(), w);
                            }
                        },
                        0,
                        1,
                        TimeUnit.HOURS
                );
    }
}
