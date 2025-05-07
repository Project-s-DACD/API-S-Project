package org.example.weather.dao;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidad que representa un registro de datos meteorológicos.
 */
public class WeatherData {
    private Long id;                  // Generado automáticamente por la BD
    private String city;
    private LocalDateTime timestamp;
    private double temp;
    private double humidity;
    private double pressure;
    private double windSpeed;

    /** Constructor por defecto (requerido por frameworks de persistencia). */
    public WeatherData() {}

    /**
     * Constructor para crear instancias antes de persistir (sin id).
     */
    public WeatherData(String city, LocalDateTime timestamp,
                       double temp, double humidity,
                       double pressure, double windSpeed) {
        this.city = city;
        this.timestamp = timestamp;
        this.temp = temp;
        this.humidity = humidity;
        this.pressure = pressure;
        this.windSpeed = windSpeed;
    }

    /**
     * Constructor completo (incluye id), útil al leer desde la BD.
     */
    public WeatherData(Long id, String city, LocalDateTime timestamp,
                       double temp, double humidity,
                       double pressure, double windSpeed) {
        this(city, timestamp, temp, humidity, pressure, windSpeed);
        this.id = id;
    }

    // Getters

    public Long getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public double getTemp() {
        return temp;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getPressure() {
        return pressure;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    // Setters

    /**
     * Protegido: sólo usado internamente o por frameworks de persistencia.
     */
    protected void setId(Long id) {
        this.id = id;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    @Override
    public String toString() {
        return "WeatherData{" +
                "id=" + id +
                ", city='" + city + '\'' +
                ", timestamp=" + timestamp +
                ", temp=" + temp +
                ", humidity=" + humidity +
                ", pressure=" + pressure +
                ", windSpeed=" + windSpeed +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WeatherData)) return false;
        WeatherData that = (WeatherData) o;
        return Double.compare(that.temp, temp) == 0
                && Double.compare(that.humidity, humidity) == 0
                && Double.compare(that.pressure, pressure) == 0
                && Double.compare(that.windSpeed, windSpeed) == 0
                && Objects.equals(id, that.id)
                && Objects.equals(city, that.city)
                && Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, city, timestamp, temp, humidity, pressure, windSpeed);
    }
}
