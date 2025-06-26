package org.example.domain;

public class Flight {
    private String flight_date;
    private String flight_status;
    private String departure_airport;
    private String arrival_airport;
    private String airline;
    private int id;
    private Integer departure_delay;


    public Flight() {
    }

    public Flight(String flight_date, String flight_status, String departure_airport,
                  String arrival_airport, String airline, String flight_number, int id, Integer departure_delay) {
        this.flight_date = flight_date;
        this.flight_status = flight_status;
        this.departure_airport = departure_airport;
        this.arrival_airport = arrival_airport;
        this.airline = airline;
        this.id = id;
        this.departure_delay = departure_delay;

    }

    public String getFlight_date() {
        return flight_date;
    }

    public String getFlight_status() {
        return flight_status;
    }

    public String getDeparture_airport() {
        return departure_airport;
    }

    public String getArrival_airport() {
        return arrival_airport;
    }

    public String getAirline() {
        return airline;
    }


    public int getId() {
        return id;
    }

    public int getDeparture_delay() {
        return departure_delay;
    }

    public int getDepartureDelayOrZero() {
        return departure_delay != null ? departure_delay : 0;
    }

}
