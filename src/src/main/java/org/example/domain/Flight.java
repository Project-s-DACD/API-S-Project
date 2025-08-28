package org.example.domain;

public class Flight {
    private String flight_date;
    private String flight_status;
    private String departure_airport;
    private String arrival_airport;
    private String airline;
    private String flightNumber;
    private int id;
    private Integer departure_delay;
    private String ts;
    private String ss;

    public Flight() {
    }

    // CONSTRUCTOR COMPLETO CORREGIDO
    public Flight(String flight_date, String flight_status, String departure_airport,
                  String arrival_airport, String airline, String flightNumber,
                  int id, Integer departure_delay, String ts, String ss) {
        this.flight_date = flight_date;
        this.flight_status = flight_status;
        this.departure_airport = departure_airport;
        this.arrival_airport = arrival_airport;
        this.airline = airline;
        this.flightNumber = flightNumber;
        this.id = id;
        this.departure_delay = departure_delay;
        this.ts = ts;
        this.ss = ss;
    }

    public String getFlight_date() { return flight_date; }
    public String getFlight_status() { return flight_status; }
    public String getDeparture_airport() { return departure_airport; }
    public String getArrival_airport() { return arrival_airport; }
    public String getAirline() { return airline; }
    public String getFlightNumber() { return flightNumber; } // CAMBIO: getter para flightNumber
    public int getId() { return id; }
    public int getDeparture_delay() { return departure_delay != null ? departure_delay : 0; } // mejor
    public int getDepartureDelayOrZero() { return departure_delay != null ? departure_delay : 0; }
    public String getTs() { return ts; }
    public String getSs() { return ss; }
}
