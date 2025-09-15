package org.example.serving;

import org.example.domain.Flight;
import org.example.OWM.domain.LocationWeather;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.List;

public class DatamartStore implements AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(DatamartStore.class);
    private final Connection conn;

    public DatamartStore(File file) throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQLite JDBC driver not found", e);
        }
        this.conn = DriverManager.getConnection("jdbc:sqlite:business-unit/datamart.db");

        try (Statement s = conn.createStatement()) {
            s.executeUpdate("DROP TABLE IF EXISTS flights");
            s.executeUpdate("DROP TABLE IF EXISTS weather");
        }

        createTables();
    }

    private void createTables() throws SQLException {
        try (Statement s = conn.createStatement()) {
            s.executeUpdate("""
                CREATE TABLE IF NOT EXISTS flights (
                    ts TEXT PRIMARY KEY,
                    ss TEXT,
                    id INTEGER,
                    flight_date TEXT,
                    flight_status TEXT,
                    flightNumber TEXT,
                    departure_airport TEXT,
                    arrival_airport TEXT,
                    airline TEXT,
                    departure_delay INTEGER
                )
            """);

            s.executeUpdate("""
                CREATE TABLE IF NOT EXISTS weather (
                    ts TEXT PRIMARY KEY,
                    ss TEXT,
                    city TEXT,
                    temperature REAL,
                    humidity INTEGER,
                    visibility INTEGER,
                    windSpeed REAL,
                    precipitation REAL,
                    cloudiness INTEGER
                )
            """);
        }
    }

    public void insertFlight(Flight f) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT OR IGNORE INTO flights(ts, ss, id, flight_date, flight_status, flightNumber, departure_airport, arrival_airport, airline, departure_delay) VALUES (?,?,?,?,?,?,?,?,?,?)"
        )) {
            ps.setString(1, f.getTs());
            ps.setString(2, f.getSs());
            ps.setInt(3, f.getId());
            ps.setString(4, f.getFlight_date());
            ps.setString(5, f.getFlight_status());
            ps.setString(6, "");
            ps.setString(7, f.getDeparture_airport());
            ps.setString(8, f.getArrival_airport());
            ps.setString(9, f.getAirline());
            ps.setInt(10, f.getDepartureDelayOrZero());
            ps.executeUpdate();
        }
    }

    public void insertFlights(List<Flight> flights) throws SQLException {
        for (Flight f : flights) {
            if (f != null && f.getFlight_date() != null) {
                insertFlight(f);
            }
        }
    }

    public void insertWeather(LocationWeather w) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT OR IGNORE INTO weather(ts, ss, city, temperature, humidity, visibility, windSpeed, precipitation, cloudiness) VALUES(?,?,?,?,?,?,?,?,?)"
        )) {
            ps.setString(1, w.getTs().toString());
            ps.setString(2, w.getSs());
            ps.setString(3, w.getCity());
            ps.setDouble(4, w.getTemperature());
            ps.setInt(5, w.getHumidity());
            ps.setInt(6, w.getVisibility());
            ps.setDouble(7, w.getWindSpeed());
            ps.setDouble(8, w.getPrecipitation());
            ps.setInt(9, w.getCloudiness());
            ps.executeUpdate();
        }
    }

    public void executeScriptWithProcessBuilder() {
        try {
            String scriptPath = "business-unit/graficos/generarGraficos.R";
            ProcessBuilder pb = new ProcessBuilder(
                    "C:\\PROGRA~1\\R\\R-44~1.1\\bin\\x64\\Rscript.exe",
                    scriptPath
            );
            pb.redirectErrorStream(true);
            Process process = pb.start();
            new BufferedReader(new InputStreamReader(process.getInputStream()))
                    .lines().forEach(logger::info);
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                logger.info("Script executed successfully.");
            } else {
                logger.error("Script finished with exit code: {}", exitCode);
            }
        } catch (Exception e) {
            logger.error("Failed to execute R script: {}", e.getMessage(), e);
        }
    }

    @Override
    public void close() throws SQLException {
        conn.close();
    }
}
