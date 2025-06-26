package org.example;

import org.example.domain.Flight;
import org.example.infrastructure.AviationAPI;
import org.example.infrastructure.AviationFlightStore;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class Controller {
    private final AviationAPI api;
    private final AviationFlightStore flightStore;

    public Controller(String apiUrl) throws SQLException {
        this.api = new AviationAPI(apiUrl);
        this.flightStore = new AviationFlightStore(new File("flights.db"));
    }

    public void run() {
        try {
            List<Flight> flights = api.fetchDataFromApi().flights();
            System.out.println("Flights ready to insert: " + flights.size());
            flightStore.insertFlightsIntoDatabase(flights);
            System.out.println("Data processed successfully.");
        } catch (Exception e) {
            System.err.println("Error during flight data processing: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
