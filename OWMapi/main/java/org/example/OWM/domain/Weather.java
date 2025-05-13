// src/main/java/org/example/owm/domain/Weather.java
package org.example.OWM.domain;

public class Weather {
    private double temperature;
    private int    humidity;
    private int    visibility;
    private double windSpeed;
    private double precipitation;
    private int    cloudiness;

    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }

    public int getHumidity() { return humidity; }
    public void setHumidity(int humidity) { this.humidity = humidity; }

    public int getVisibility() { return visibility; }
    public void setVisibility(int visibility) { this.visibility = visibility; }

    public double getWindSpeed() { return windSpeed; }
    public void setWindSpeed(double windSpeed) { this.windSpeed = windSpeed; }

    public double getPrecipitation() { return precipitation; }
    public void setPrecipitation(double precipitation) { this.precipitation = precipitation; }

    public int getCloudiness() { return cloudiness; }
    public void setCloudiness(int cloudiness) { this.cloudiness = cloudiness; }

    @Override
    public String toString() {
        return String.format(
                "Weather[temp=%.1f°C, humidity=%d%%, visibility=%dm, wind=%.1fm/s, precipitation=%.1fmm, clouds=%d%%]",
                temperature, humidity, visibility, windSpeed, precipitation, cloudiness
        );
    }
}