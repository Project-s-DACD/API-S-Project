package org.example.control;

import org.example.messaging.FlightPublisher;
import org.example.sqlServices.DataStore;
import org.example.apiServices.DataProvider;
import org.example.apiServices.Flight;

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
            apiProvider.fetch();
            List<Flight> vuelos = apiProvider.flights();
            dataStore.insert(vuelos);
            System.out.println("Vuelos procesados y almacenados exitosamente.");

            FlightPublisher publisher = new FlightPublisher();
            for (Flight vuelo : vuelos) {
                publisher.publish(vuelo);
            }

        } catch (IOException e) {
            System.err.println("ERROR al obtener datos de la API: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("ERROR en la base de datos: " + e.getMessage());
        } catch (JMSException e) {
            System.err.println("ERROR al publicar evento en ActiveMQ: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("ERROR inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
