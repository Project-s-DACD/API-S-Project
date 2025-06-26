package org.example;

import org.example.batch.FileEventLoader;
import org.example.batch.FileWeatherLoader;
import org.example.domain.Flight;
import org.example.OWM.domain.LocationWeather;
import org.example.serving.BusinessCli;
import org.example.serving.DatamartStore;
import org.example.speed.BusinessSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class BusinessMain {

    private static final Logger logger = LoggerFactory.getLogger(BusinessMain.class);

    public static void main(String[] args) {
        try {
            if (args.length < 2) {
                logger.error("Two arguments required: [flightEventsPath] [weatherEventsPath]");
                return;
            }

            File flightPath = new File(args[0]);
            File weatherPath = new File(args[1]);
            DatamartStore datamart = new DatamartStore();
            FileEventLoader flightLoader = new FileEventLoader(flightPath);
            List<Flight> flights = flightLoader.loadEvents();
            logger.info("Flights loaded: {}", flights.size());
            for (Flight flight : flights) {
                datamart.insertFlight(flight);
            }

            FileWeatherLoader weatherLoader = new FileWeatherLoader(weatherPath);
            List<LocationWeather> weathers = weatherLoader.loadEvents();
            logger.info("Weather events loaded: {}", weathers.size());
            for (LocationWeather weather : weathers) {
                datamart.insertWeather(weather);
            }

            datamart.executeScriptWithProcessBuilder();

            BusinessSubscriber subscriber = new BusinessSubscriber(datamart);
            subscriber.startRealTimeSubscriber();

            new BusinessCli().startMenuCli();
            logger.info("Real-time subscription active...");
            Thread.sleep(Long.MAX_VALUE);

        } catch (SQLException e) {
            logger.error("Database error: {}", e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage(), e);
        }
    }
}
