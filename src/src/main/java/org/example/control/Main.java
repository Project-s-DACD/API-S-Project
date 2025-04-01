package org.example.control;
import org.example.sqlServices.AviationFlightStore;
import org.example.apiServices.AviationAPI;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        try {
            AviationAPI api = new AviationAPI(System.getenv("apiKey"));
            AviationFlightStore flightStore = new AviationFlightStore(new File("flights.db"));
            DataManager dataManager = new DataManager(api, flightStore);
            dataManager.processData();
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
