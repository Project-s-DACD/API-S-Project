package org.example.OWM.domain;

public class LocationWeather {
    private final String city;
    private final double temperature;
    private final int    humidity;
    private final int    visibility;
    private final double windSpeed;
    private final double precipitation;
    private final int    cloudiness;

    public LocationWeather(
            String city,
            double temperature,
            int humidity,
            int visibility,
            double windSpeed,
            double precipitation,
            int cloudiness
    ) {
        this.city          = city;
        this.temperature   = temperature;
        this.humidity      = humidity;
        this.visibility    = visibility;
        this.windSpeed     = windSpeed;
        this.precipitation = precipitation;
        this.cloudiness    = cloudiness;
    }

    public String getCity()        { return city; }
    public double getTemperature() { return temperature; }
    public int    getHumidity()    { return humidity; }
    public int    getVisibility()  { return visibility; }
    public double getWindSpeed()   { return windSpeed; }
    public double getPrecipitation(){ return precipitation; }
    public int    getCloudiness()  { return cloudiness; }

    @Override
    public String toString() {
        return String.format(
                "%s → Weather[temp=%.1f°C, humidity=%d%%, visibility=%dm, wind=%.1fm/s, precipitation=%.1fmm, clouds=%d%%]",
                city, temperature, humidity, visibility, windSpeed, precipitation, cloudiness
        );
    }
}