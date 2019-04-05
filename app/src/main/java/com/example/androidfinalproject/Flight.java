package com.example.androidfinalproject;
/***********************************************************************************
 Class:  Flight
 Purpose:  This class will model the data and actions needed for a flight
 Author:   Gustavo Adami
 Course:   Android
 Data members:   flightName : String, flightNumber : String
 Methods: getFlightName() : String, getFlightNumber() : String
 *************************************************************************************/


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
