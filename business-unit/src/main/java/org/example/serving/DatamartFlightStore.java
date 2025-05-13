package org.example.serving;

import org.example.domain.Flight;
import org.example.infrastructure.persistence.AviationFlightStore;
import org.example.infrastructure.ports.DataStore;
import org.rosuda.REngine.Rserve.RConnection;

import java.io.File;
import java.sql.SQLException;

public class DatamartFlightStore extends AviationFlightStore implements DataStore<Flight> {
    public DatamartFlightStore(File file) throws SQLException {
        super(file);
    }

    public void executeScriptR() {
        try {
            RConnection c = new RConnection();
            c.eval("source('business-unit/graficos/generarGraficos.R')");
            c.eval("generarGraficos()");
            System.out.println("Script R ejecutado correctamente con Rserve.");
            c.close();
        } catch (Exception e) {
            System.err.println("Error al ejecutar el script R con Rserve: " + e.getClass());
        }
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
        executeScriptR();


    }

}
