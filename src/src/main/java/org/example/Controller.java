package org.example;

import org.example.domain.Flight;
import org.example.infrastructure.AviationAPI;
import org.example.infrastructure.AviationFlightStore;
import org.example.infrastructure.FlightPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class Controller {

    private static final Logger log = LoggerFactory.getLogger(Controller.class);

    private final AviationAPI api;
    private final AviationFlightStore flightStore;
    private final FlightPublisher publisher;

    public Controller(String apiUrl) throws SQLException {
        this.api = new AviationAPI(apiUrl);
        this.flightStore = new AviationFlightStore(new File("flights.db"));
        this.publisher = new FlightPublisher();
    }

    public void run() {
        try {
            List<Flight> flights = api.fetchDataFromApi().flights();
            log.info("Flights ready to insert: {}", flights.size());

            for (Flight flight : flights) {
                publisher.publishEventInBroker(flight);
            }

            flightStore.insertFlightsIntoDatabase(flights);
            log.info("Data processed and published successfully.");
        } catch (Exception e) {
            log.error("Error during flight data processing: {}", e.getMessage(), e);
        }
    }
}
