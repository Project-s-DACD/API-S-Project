package org.example;
import org.example.infrastructure.persistence.AviationFlightStore;
import org.example.infrastructure.apiServices.AviationAPI;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        try {
            if (args.length == 0) {
                throw new IllegalArgumentException("API Key is required as the first argument.");
            }

            AviationAPI api = new AviationAPI(args[0]);
            AviationFlightStore flightStore = new AviationFlightStore(new File("flights.db"));
            DataManager dataManager = new DataManager(api, flightStore);


            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(() -> {
                try {
                    dataManager.processData();
                    System.out.println("Data processed successfully.");
                } catch (Exception e) {
                    System.err.println("Error processing data: " + e.getMessage());
                    e.printStackTrace();
                }
            }, 0, 7, TimeUnit.DAYS);

        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}