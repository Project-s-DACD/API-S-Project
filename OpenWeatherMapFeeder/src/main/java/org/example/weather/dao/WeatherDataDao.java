package org.example.weather.dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WeatherDataDao {
    private static final String URL = "jdbc:sqlite:weather.db";

    public WeatherDataDao() throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            String sql = """
                CREATE TABLE IF NOT EXISTS weather_data (
                  id INTEGER PRIMARY KEY AUTOINCREMENT,
                  city TEXT NOT NULL,
                  timestamp TEXT NOT NULL,
                  temp REAL,
                  humidity REAL,
                  pressure REAL,
                  wind_speed REAL
                );
            """;
            stmt.execute(sql);
        }
    }

    public void save(WeatherData data) throws SQLException {
        String sql = """
            INSERT INTO weather_data(city, timestamp, temp, humidity, pressure, wind_speed)
            VALUES (?, ?, ?, ?, ?, ?);
        """;
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, data.getCity());
            ps.setString(2, data.getTimestamp().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            ps.setDouble(3, data.getTemp());
            ps.setDouble(4, data.getHumidity());
            ps.setDouble(5, data.getPressure());
            ps.setDouble(6, data.getWindSpeed());
            ps.executeUpdate();
        }
    }
}
