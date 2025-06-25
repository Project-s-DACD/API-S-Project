package org.example;

import org.example.batch.EventLoader;
import org.example.batch.FileEventLoader;
import org.example.domain.Flight;
import org.example.serving.BusinessCli;
import org.example.serving.DatamartFlightStore;
import org.example.speed.BusinessSubscriber;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class BusinessMain {

    public static void main(String[] args) {
        try {

            DatamartFlightStore datamart = new DatamartFlightStore(new File("business-unit/datamart.db"));

            System.out.println("Event´s path: " + args[0]);
            EventLoader loader = new FileEventLoader(new File(args[0]));
            List<Flight> vuelosHistoricos = loader.loadEvents();
            List<Flight> vuelosValidos = vuelosHistoricos.stream()
                    .filter(f -> f != null && f.getFlight_date() != null)
                    .toList();

            System.out.println("Flights ready to insert: " + vuelosValidos.size());
            datamart.insertFlightsIntoDatabase(vuelosValidos);
            datamart.executeScriptWithProcessBuilder();
            BusinessSubscriber subscriber = new BusinessSubscriber(datamart);
            subscriber.startRealTimeSubscriber();

            new BusinessCli().startMenuCli();
            System.out.println("Starting realtime events...");
            Thread.sleep(Long.MAX_VALUE);

        } catch (SQLException e) {
            System.err.println("Error in database: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("General error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
