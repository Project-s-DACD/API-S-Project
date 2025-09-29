package org.example.serving;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class VisualInterfaceGui {

    private JFrame frame;
    private static final String DB_PATH = "business-unit/datamart.db";

    public VisualInterfaceGui() {
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        frame = new JFrame("Business Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 900);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel mainTitle = new JLabel("Llegadas al aeropuerto de Gran Canaria");
        mainTitle.setFont(new Font("Arial", Font.BOLD, 28));
        titlePanel.add(mainTitle);
        mainPanel.add(titlePanel);

        JPanel kpiContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel kpiPanel = new JPanel();
        kpiPanel.setLayout(new BoxLayout(kpiPanel, BoxLayout.Y_AXIS));
        kpiPanel.setBorder(BorderFactory.createTitledBorder("Indicadores"));

        addKpiLabel(kpiPanel, "Aeropuerto con más retrasos: " + getAirportWithMostDelays());
        addKpiLabel(kpiPanel, "Retraso promedio: " + getAverageDelay() + " minutos");
        addKpiLabel(kpiPanel, "Vuelo más retrasado: " + getMaxDelayFlight());
        addKpiLabel(kpiPanel, "Día con más retrasos: " + getDayWithMostDelays());
        addKpiLabel(kpiPanel, "Aerolínea con más retrasos: " + getAirlineWithMostDelays());
        addKpiLabel(kpiPanel, "Aeropuerto con mayor retraso promedio: " + getAirportWithHighestAvgDelay());
        addKpiLabel(kpiPanel, "Retrasos acumulados este mes: " + getMonthlyTotalDelay() + " minutos");

        addKpiLabel(kpiPanel, "Clima promedio este mes: " + getMonthlyWeatherAvg());
        addKpiLabel(kpiPanel, "Clima promedio en días con retrasos: " + getWeatherAvgOnDelayDays());

        kpiContainer.add(kpiPanel);
        mainPanel.add(kpiContainer);

        mainPanel.add(createSectionPanel("Flights Information", "business-unit/graficos/grafico_test2.png"));
        mainPanel.add(createSectionPanel("Weather Conditions", "business-unit/graficos/grafico_test.png"));
        mainPanel.add(createSectionPanel("Last Flight vs Weather", "business-unit/graficos/grafico_weather.png"));
        mainPanel.add(createSectionPanel("Weather vs Flight Delay", "business-unit/graficos/grafico_weathervsdelay.png"));

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        frame.add(scrollPane);

        frame.setVisible(true);
    }

    private void addKpiLabel(JPanel panel, String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 18));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
    }

    private JPanel createSectionPanel(String title, String imagePath) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel label = new JLabel(title, SwingConstants.LEFT);
        label.setFont(new Font("Arial", Font.BOLD, 22));
        panel.add(label, BorderLayout.NORTH);

        ImageIcon icon = new ImageIcon(imagePath);
        Image img = icon.getImage();
        int newWidth = 750;
        int newHeight = (int) ((double) img.getHeight(null) / img.getWidth(null) * newWidth);
        Image scaledImg = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        JLabel imgLabel = new JLabel(new ImageIcon(scaledImg));
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(imgLabel, BorderLayout.CENTER);
        return panel;
    }

    private String getAirportWithMostDelays() {
        return querySingleResult(
                "SELECT departure_airport, COUNT(*) as total " +
                        "FROM flights WHERE departure_delay > 0 " +
                        "GROUP BY departure_airport ORDER BY total DESC LIMIT 1",
                "departure_airport", "total", " retrasos"
        );
    }

    private double getAverageDelay() {
        double avg = 0;
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH)) {
            ResultSet rs = conn.createStatement().executeQuery(
                    "SELECT AVG(departure_delay) as avg_delay FROM flights WHERE departure_delay > 0"
            );
            if (rs.next()) avg = rs.getDouble("avg_delay");
        } catch (SQLException ignored) {}
        return Math.round(avg * 10.0) / 10.0;
    }

    private String getMaxDelayFlight() {
        return querySingleResult(
                "SELECT flightNumber, MAX(departure_delay) as max_delay " +
                        "FROM flights",
                "flightNumber", "max_delay", " min"
        );
    }

    private String getDayWithMostDelays() {
        return querySingleResult(
                "SELECT flight_date, COUNT(*) as total " +
                        "FROM flights WHERE departure_delay > 0 " +
                        "GROUP BY flight_date ORDER BY total DESC LIMIT 1",
                "flight_date", "total", " retrasos"
        );
    }

    private String getAirlineWithMostDelays() {
        return querySingleResult(
                "SELECT airline, COUNT(*) as total " +
                        "FROM flights WHERE departure_delay > 0 " +
                        "GROUP BY airline ORDER BY total DESC LIMIT 1",
                "airline", "total", " retrasos"
        );
    }

    private String getAirportWithHighestAvgDelay() {
        return querySingleResult(
                "SELECT departure_airport, AVG(departure_delay) as avg_delay " +
                        "FROM flights WHERE departure_delay > 0 " +
                        "GROUP BY departure_airport ORDER BY avg_delay DESC LIMIT 1",
                "departure_airport", "avg_delay", " min promedio"
        );
    }

    private int getMonthlyTotalDelay() {
        int total = 0;
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH)) {
            String month = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
            ResultSet rs = conn.createStatement().executeQuery(
                    "SELECT SUM(departure_delay) as total_delay " +
                            "FROM flights WHERE substr(flight_date,1,7) = '" + month + "'"
            );
            if (rs.next()) total = rs.getInt("total_delay");
        } catch (SQLException ignored) {}
        return total;
    }


    private String getMonthlyWeatherAvg() {
        String result = "No data";
        String month = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH)) {
            ResultSet rs = conn.createStatement().executeQuery(
                    "SELECT AVG(temperature) as t, AVG(humidity) as h, AVG(precipitation) as p, AVG(windSpeed) as w " +
                            "FROM weather WHERE substr(ts,1,7) = '" + month + "'"
            );
            if (rs.next()) {
                result = String.format("Temp %.1f°C, Hum %.1f%%, Prec %.1f mm, Viento %.1f km/h",
                        rs.getDouble("t"), rs.getDouble("h"),
                        rs.getDouble("p"), rs.getDouble("w"));
            }
        } catch (SQLException ignored) {}
        return result;
    }

    private String getWeatherAvgOnDelayDays() {
        String result = "No data";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH)) {
            ResultSet rs = conn.createStatement().executeQuery(
                    "SELECT AVG(w.temperature) as t, AVG(w.humidity) as h, AVG(w.precipitation) as p, AVG(w.windSpeed) as w " +
                            "FROM weather w " +
                            "JOIN flights f ON DATE(w.ts) = f.flight_date " +
                            "WHERE f.departure_delay > 0"
            );
            if (rs.next()) {
                result = String.format("Temp %.1f°C, Hum %.1f%%, Prec %.1f mm, Viento %.1f km/h",
                        rs.getDouble("t"), rs.getDouble("h"),
                        rs.getDouble("p"), rs.getDouble("w"));
            }
        } catch (SQLException ignored) {}
        return result;
    }

    private String querySingleResult(String query, String col1, String col2, String suffix) {
        String result = "No data";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH)) {
            ResultSet rs = conn.createStatement().executeQuery(query);
            if (rs.next()) result = rs.getString(col1) + " (" + rs.getString(col2) + suffix + ")";
        } catch (SQLException ignored) {}
        return result;
    }

}
