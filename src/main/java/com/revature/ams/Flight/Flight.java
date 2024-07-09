package com.revature.ams.Flight;

import java.time.LocalDateTime;

/**
 * Flight models holds the information for a lot of relationships between the Airport, Member & Airline models. Along
 * with this we have established a minimum flight requirement of needing a flightNumber, originAirport, destinationAirport
 * and a seatCount for initialization. No args included for future implementation of Javalin.
 */
public class Flight {
    private int flightNumber;
    private String originAirport; // Airport identification number or String
    private String destinationAirport;
    private LocalDateTime timeDeparture;
    private LocalDateTime timeArrival;
    private short seatCount;
    private int pilot;
    private int airline;

    public Flight(){}

    public Flight(int flightNumber, String originAirport, String destinationAirport,
                  LocalDateTime timeDeparture, LocalDateTime timeArrival, short seatCount, int pilot, int airline){
        this.flightNumber = flightNumber;
        this.originAirport = originAirport;
        this.destinationAirport = destinationAirport;
        this.timeDeparture = timeDeparture;
        this.timeArrival = timeArrival;
        this.seatCount = seatCount;
        this.pilot = pilot;
        this.airline = airline;
    }

    public Flight(int flightNumber, String originAirport, String destinationAirport, short seatCount){
        this.flightNumber = flightNumber;
        this.originAirport = originAirport;
        this.destinationAirport = destinationAirport;
        this.seatCount = seatCount;
    }

    public int getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(int flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getOriginAirport() {
        return originAirport;
    }

    public void setOriginAirport(String originAirport) {
        this.originAirport = originAirport;
    }

    public String getDestinationAirport() {
        return destinationAirport;
    }

    public void setDestinationAirport(String destinationAirport) {
        this.destinationAirport = destinationAirport;
    }

    public LocalDateTime getTimeDeparture() {
        return timeDeparture;
    }

    public void setTimeDeparture(LocalDateTime timeDeparture) {
        this.timeDeparture = timeDeparture;
    }

    public LocalDateTime getTimeArrival() {
        return timeArrival;
    }

    public void setTimeArrival(LocalDateTime timeArrival) {
        this.timeArrival = timeArrival;
    }

    public short getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(short seatCount) {
        this.seatCount = seatCount;
    }

    public int getPilot() {
        return pilot;
    }

    public void setPilot(int pilot) {
        this.pilot = pilot;
    }

    public int getAirline() {
        return airline;
    }

    public void setAirline(int airline) {
        this.airline = airline;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "flightNumber=" + flightNumber +
                ", originAirport='" + originAirport + '\'' +
                ", destinationAirport='" + destinationAirport + '\'' +
                ", timeDeparture=" + timeDeparture +
                ", timeArrival=" + timeArrival +
                ", seatCount=" + seatCount +
                ", airline=" + airline +
                '}';
    }
}
