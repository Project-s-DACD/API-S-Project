package org.example.apiServices;

public class Flight {
    private String flight_date;
    private String flight_status;
    private String departure_airport;
    private String arrival_airport;
    private String airline;
    private String flight_number;
    private int id;

    public Flight(String flight_date, String flight_status, String departure_airport, String arrival_airport, String airline, String flight_number, int id) {
        this.flight_date = flight_date;
        this.flight_status = flight_status;
        this.departure_airport = departure_airport;
        this.arrival_airport = arrival_airport;
        this.airline = airline;
        this.flight_number = flight_number;
        this.id = id;
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

    public String getFlight_number() {
        return flight_number;
    }

    public int getId() {
        return id;
    }
}
