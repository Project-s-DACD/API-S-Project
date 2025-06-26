package org.example.OWM.domain;

import java.time.Instant;

public class LocationWeather {
    private final Instant ts;
    private final String ss;
    private final String city;

    private final double temperature;
    private final int    humidity;
    private final int    visibility;
    private final double windSpeed;
    private final double precipitation;
    private final int    cloudiness;

    public LocationWeather(
            Instant ts,
            String ss,
            String city,
            double temperature,
            int humidity,
            int visibility,
            double windSpeed,
            double precipitation,
            int cloudiness
    ) {
        this.ts            = ts;
        this.ss            = ss;
        this.city          = city;
        this.temperature   = temperature;
        this.humidity      = humidity;
        this.visibility    = visibility;
        this.windSpeed     = windSpeed;
        this.precipitation = precipitation;
        this.cloudiness    = cloudiness;
    }

    public Instant getTs()            { return ts; }
    public String  getSs()            { return ss; }
    public String  getCity()          { return city; }
    public double  getTemperature()   { return temperature; }
    public int     getHumidity()      { return humidity; }
    public int     getVisibility()    { return visibility; }
    public double  getWindSpeed()     { return windSpeed; }
    public double  getPrecipitation() { return precipitation; }
    public int     getCloudiness()    { return cloudiness; }
}