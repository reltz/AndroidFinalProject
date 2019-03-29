package com.example.androidfinalproject;

public class Flight {
    private String flightName;
    private String flightNumber;

    public Flight(String flightName, String flightNumber){
        this.flightName = flightName;
        this.flightNumber = flightNumber;
    }

    public Flight(){

    }

    public String getFlightName(){
        return flightName;
    }

    public String getFlightNumber(){
        return flightNumber;
    }
}
