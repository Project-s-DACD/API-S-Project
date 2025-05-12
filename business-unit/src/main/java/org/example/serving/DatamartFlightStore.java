package org.example.serving;

import org.example.domain.Flight;
import org.example.infrastructure.persistence.AviationFlightStore;
import org.example.infrastructure.persistence.DataStore;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.sql.SQLException;

public class DatamartFlightStore extends AviationFlightStore implements DataStore<Flight> {
    public DatamartFlightStore(File file) throws SQLException {
        super(file);
    }

    @Override
    public void insertFlightsIntoDatabase(java.util.List<Flight> flights) throws SQLException {
        java.util.List<Flight> validos = flights.stream()
                .filter(f -> f != null && f.getFlight_date() != null)
                .toList();

        if (validos.isEmpty()) {
            System.out.println("There are not flights to save.");
            return;
        }

        saveFlightsToDatabase(validos);
        /*try {
            ProcessBuilder pb = new ProcessBuilder(
                    "C:\\PROGRA~1\\R\\R-44~1.1\\bin\\x64\\Rscript.exe",
                    "graficos/generartorGraficos.R"
            );
            pb.redirectErrorStream(true);
            Process process = pb.startRealTimeSubscriber();

            new BufferedReader(new InputStreamReader(process.getInputStream()))
                    .lines()
                    .forEach(System.out::println);

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Script R ejecutado correctamente.");
            } else {
                System.err.println("El script R finalizó con código: " + exitCode);
            }
        } catch (Exception e) {
            System.err.println("No se pudo ejecutar Rscript: " + e.getMessage());
        }*/


    }

}
