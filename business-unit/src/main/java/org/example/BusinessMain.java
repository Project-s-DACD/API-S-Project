package org.example;

import org.example.batch.EventLoader;
import org.example.batch.FileEventLoader;
import org.example.domain.Flight;
import org.example.serving.BusinessCli;
import org.example.serving.DatamartFlightStore;
import org.example.speed.BusinessSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

public class BusinessMain {

    private static final Logger logger = LoggerFactory.getLogger(BusinessMain.class);

    public static void main(String[] args) {
        try {
            DatamartFlightStore datamart = new DatamartFlightStore(new File("business-unit/datamart.db"));

            logger.info("Event´s path: {}", args[0]);
            EventLoader loader = new FileEventLoader(new File(args[0]));
            List<Flight> vuelosHistoricos = loader.loadEvents();
            List<Flight> vuelosValidos = vuelosHistoricos.stream()
                    .filter(f -> f != null && f.getFlight_date() != null)
                    .toList();

            logger.info("Flights ready to analyze: {}", vuelosValidos.size());
            datamart.executeScriptWithProcessBuilder();
            BusinessSubscriber subscriber = new BusinessSubscriber(datamart);
            subscriber.startRealTimeSubscriber();

            new BusinessCli().startMenuCli();
            logger.info("Starting realtime events...");
            Thread.sleep(Long.MAX_VALUE);

        } catch (Exception e) {
            logger.error("General error: {}", e.getMessage(), e);
        }
    }
}
