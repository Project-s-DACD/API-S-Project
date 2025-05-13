package org.example.weather.scheduler;

import org.example.weather.client.WeatherApiClient;
import org.example.weather.dao.WeatherData;
import org.example.weather.dao.WeatherDataDao;
import org.example.weather.model.WeatherResponse;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WeatherScheduler {
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private final WeatherApiClient client = new WeatherApiClient();
    private final WeatherDataDao dao;
    private final String city;

    public WeatherScheduler(String city) throws Exception {
        this.city = city;
        this.dao = new WeatherDataDao();
    }

    /** Arranca el scheduler para ejecutar la tarea inmediatamente y luego cada hora. */
    public void start() {
        executor.scheduleAtFixedRate(() -> {
            try {
                WeatherResponse resp = client.getCurrentWeather(city);

                WeatherData data = new WeatherData(
                        resp.getName(),
                        LocalDateTime.now(),
                        resp.getMain().getTemp(),
                        resp.getMain().getHumidity(),
                        resp.getMain().getPressure(),
                        resp.getWind().getSpeed()
                );

                dao.save(data);
                System.out.println("Guardado: " + data);
            } catch (Exception e) {
                System.err.println("Error al obtener/guardar datos: " + e.getMessage());
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.HOURS);
    }

    /** Detiene el scheduler de forma ordenada. */
    public void stop() {
        executor.shutdown();
    }
}