package org.example.infrastructure.persistence;

import org.example.domain.Flight;
import org.example.infrastructure.ports.DataStore;

import java.io.File;
import java.sql.*;
import java.util.List;

public class AviationFlightStore implements DataStore<Flight> {


    private static final String insertFlightSql = "INSERT OR IGNORE INTO flights(" +
            "flight_date, flight_status, departure_airport, arrival_airport, airline, flight_number" +
            ") VALUES(?, ?, ?, ?, ?, ?)";

    private final File file;

    public AviationFlightStore(File file) throws SQLException {
        this.file = file;
        createDatabase();
    }

    @Override
    public void insertFlightsIntoDatabase(List<Flight> flights) throws SQLException {
        if (flights.isEmpty()) {
            System.out.println("No hay vuelos para insertar.");
            return;
        }
        saveFlightsToDatabase(flights);
    }


    protected void createDatabase() throws SQLException {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath())) {
            if (conn != null) {
                String createTableSQL = "CREATE TABLE IF NOT EXISTS flights (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "flight_date TEXT," +
                        "flight_status TEXT," +
                        "departure_airport TEXT," +
                        "arrival_airport TEXT," +
                        "airline TEXT," +
                        "flight_number TEXT," +
                        "UNIQUE(flight_date, flight_number, airline)" +
                        ");";
                conn.createStatement().execute(createTableSQL);
                System.out.println("Base de datos y tabla creadas correctamente.");
            }
        }
    }

    protected void saveFlightsToDatabase(List<Flight> flights) throws SQLException {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
             PreparedStatement pstmt = conn.prepareStatement(insertFlightSql)) {

            for (Flight flight : flights) {
                pstmt.setString(1, flight.getFlight_date());
                pstmt.setString(2, flight.getFlight_status());
                pstmt.setString(3, flight.getDeparture_airport());
                pstmt.setString(4, flight.getArrival_airport());
                pstmt.setString(5, flight.getAirline());
                pstmt.setString(6, flight.getFlight_number());
                pstmt.addBatch();
            }

            pstmt.executeBatch();
            System.out.println("Vuelos insertados en la base de datos (sin duplicados).");
        }
    }
}
