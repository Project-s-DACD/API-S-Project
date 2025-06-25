package org.example;

import org.example.infrastructure.FlightPublisher;
import org.example.infrastructure.ports.DataStore;
import org.example.infrastructure.ports.DataProvider;
import org.example.domain.Flight;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.jms.JMSException;

public class DataManager {
    private final DataProvider apiProvider;
    private final DataStore<Flight> dataStore;

    public DataManager(DataProvider apiProvider, DataStore<Flight> dataStore) {
        this.apiProvider = apiProvider;
        this.dataStore = dataStore;
    }

    public void processData() {
        try {
            apiProvider.fetchDataFromApi();
            List<Flight> vuelos = apiProvider.flights();
            dataStore.insertFlightsIntoDatabase(vuelos);
            System.out.println("Flights correctly saved.");

            FlightPublisher publisher = new FlightPublisher();
            for (Flight vuelo : vuelos) {
                publisher.publishEventInBroker(vuelo);
            }

        } catch (IOException e) {
            System.err.println("Error while fetching the data from api: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error in the data base: " + e.getMessage());
        } catch (JMSException e) {
            System.err.println("Error while publishing the event in ActiveMQ: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
