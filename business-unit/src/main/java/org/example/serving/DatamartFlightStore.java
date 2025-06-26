package org.example.serving;

import org.example.domain.Flight;
import org.example.infrastructure.AviationFlightStore;
import org.example.infrastructure.ports.DataStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.sql.SQLException;

public class DatamartFlightStore extends AviationFlightStore implements DataStore<Flight> {
    private static final Logger logger = LoggerFactory.getLogger(DatamartFlightStore.class);

    public DatamartFlightStore(File file) throws SQLException {
        super(file);
    }

    public void executeScriptWithProcessBuilder() {
        try {
            String rutaScript = "C:/Users/agust/OneDrive/Escritorio/Uni/2º año/DAPC/Api´s project/business-unit/graficos/generarGraficos.R";

            ProcessBuilder pb = new ProcessBuilder(
                    "C:\\PROGRA~1\\R\\R-44~1.1\\bin\\x64\\Rscript.exe",
                    rutaScript
            );

            pb.redirectErrorStream(true);
            Process process = pb.start();

            new BufferedReader(new InputStreamReader(process.getInputStream()))
                    .lines()
                    .forEach(logger::info);

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                logger.info("Script R is executing correctly.");
            } else {
                logger.error("Script R already finished: {}", exitCode);
            }

        } catch (Exception e) {
            logger.error("Couldn´t execute Rscript: {}", e.getMessage());
        }
    }

    @Override
    public void insertFlightsIntoDatabase(java.util.List<Flight> flights) throws SQLException {
        java.util.List<Flight> validos = flights.stream()
                .filter(f -> f != null && f.getFlight_date() != null)
                .toList();

        if (validos.isEmpty()) {
            logger.info("There are not flights to save.");
            return;
        }

        saveFlightsToDatabase(validos);
    }
}
