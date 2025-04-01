package org.example.control;
import org.example.sqlServices.DataStore;
import org.example.apiServices.DataProvider;
import org.example.apiServices.Flight;
import java.io.IOException;
import java.sql.SQLException;

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
            dataStore.insert(apiProvider.flights());
            System.out.println("Vuelos procesados y almacenados exitosamente.");
        } catch (IOException e) {
            System.err.println("ERROR al obtener datos de la API: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("ERROR en la base de datos: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("ERROR inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
