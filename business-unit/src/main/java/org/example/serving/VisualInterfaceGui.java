package org.example.serving;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class VisualInterfaceGui {

    private JFrame frame;
    private static final String DB_PATH = "business-unit/datamart.db";

    public VisualInterfaceGui() {
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        frame = new JFrame("Business Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 700);

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel flightsPanel = new JPanel(new BorderLayout());
        DefaultTableModel flightTableModel = new DefaultTableModel();
        flightTableModel.setColumnIdentifiers(new String[]{
                "Ts", "Date", "Status", "Departure", "Airline", "Flight Number", "Delay"
        });
        JTable flightTable = new JTable(flightTableModel);
        flightsPanel.add(new JScrollPane(flightTable), BorderLayout.CENTER);
        loadFlightsFromDatabase(flightTableModel);
        tabbedPane.addTab("Flights", flightsPanel);

        JPanel weatherPanel = new JPanel(new BorderLayout());
        DefaultTableModel weatherTableModel = new DefaultTableModel();
        weatherTableModel.setColumnIdentifiers(new String[]{
                "Ts", "Temperature", "Humidity", "Visibility", "Wind Speed", "Precipitation", "Cloudiness"
        });
        JTable weatherTable = new JTable(weatherTableModel);
        weatherPanel.add(new JScrollPane(weatherTable), BorderLayout.CENTER);
        loadWeatherFromDatabase(weatherTableModel);
        tabbedPane.addTab("Weather", weatherPanel);

        JPanel lastFlightPanel = new JPanel(new BorderLayout());
        DefaultTableModel lastTableModel = new DefaultTableModel();
        lastTableModel.setColumnIdentifiers(new String[]{
                "Flight Ts", "Date", "Flight Number", "Delay",
                "Weather Ts", "Temperature", "Humidity", "Visibility", "Wind Speed", "Precipitation", "Cloudiness"
        });
        JTable lastTable = new JTable(lastTableModel);
        lastFlightPanel.add(new JScrollPane(lastTable), BorderLayout.CENTER);
        loadLastFlightAndWeather(lastTableModel);
        tabbedPane.addTab("Last Flight vs Weather", lastFlightPanel);

        frame.add(tabbedPane);
        frame.setVisible(true);
    }

    private void loadFlightsFromDatabase(DefaultTableModel model) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH)) {
            String query = "SELECT ts, flight_date, flight_status, departure_airport, airline, flightNumber, departure_delay FROM flights";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("ts"));
                row.add(rs.getString("flight_date"));
                row.add(rs.getString("flight_status"));
                row.add(rs.getString("departure_airport"));
                row.add(rs.getString("airline"));
                row.add(rs.getString("flightNumber"));
                row.add(rs.getInt("departure_delay"));
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error loading flights: " + e.getMessage());
        }
    }

    private void loadWeatherFromDatabase(DefaultTableModel model) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH)) {
            String query = "SELECT ts, temperature, humidity, visibility, windSpeed, precipitation, cloudiness FROM weather";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("ts"));
                row.add(rs.getDouble("temperature"));
                row.add(rs.getDouble("humidity"));
                row.add(rs.getDouble("visibility"));
                row.add(rs.getDouble("windSpeed"));
                row.add(rs.getDouble("precipitation"));
                row.add(rs.getInt("cloudiness"));
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error loading weather: " + e.getMessage());
        }
    }

    private void loadLastFlightAndWeather(DefaultTableModel model) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH)) {
            String lastFlightQuery = "SELECT ts, flight_date, flightNumber, departure_delay FROM flights ORDER BY ts DESC LIMIT 1";
            String lastWeatherQuery = "SELECT ts, temperature, humidity, visibility, windSpeed, precipitation, cloudiness FROM weather ORDER BY ts DESC LIMIT 1";

            ResultSet rsFlight = conn.createStatement().executeQuery(lastFlightQuery);
            ResultSet rsWeather = conn.createStatement().executeQuery(lastWeatherQuery);

            if (rsFlight.next() && rsWeather.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rsFlight.getString("ts"));
                row.add(rsFlight.getString("flight_date"));
                row.add(rsFlight.getString("flightNumber"));
                row.add(rsFlight.getInt("departure_delay"));
                row.add(rsWeather.getString("ts"));
                row.add(rsWeather.getDouble("temperature"));
                row.add(rsWeather.getDouble("humidity"));
                row.add(rsWeather.getDouble("visibility"));
                row.add(rsWeather.getDouble("windSpeed"));
                row.add(rsWeather.getDouble("precipitation"));
                row.add(rsWeather.getInt("cloudiness"));
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error loading last flight/weather: " + e.getMessage());
        }
    }
}
