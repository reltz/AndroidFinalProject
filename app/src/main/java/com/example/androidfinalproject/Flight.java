package com.example.androidfinalproject;
/***********************************************************************************
 Class:  Flight
 Purpose:  This class will model the data and actions needed for a flight
 Author:   Gustavo Adami
 Course:   Android
 Data members:   flightStatus : String, flightNumber : String
 Methods: getFlightStatus() : String, getFlightNumber() : String
 *************************************************************************************/

public class Flight {
    private int id;
    private String flightNumber;
    private String flightStatus;
    private String flightSpeedHorizontal;
    private boolean flightSpeedIsGround;
    private String flightSpeedVertical;
    private String flightAltitude;


    public Flight(int id, String flightNumber, String flightStatus, String flightSpeedHorizontal, boolean flightSpeedIsGround, String flightSpeedVertical, String flightAltitude){
        this.id = id;
        this.flightNumber = flightNumber;
        this.flightStatus = flightStatus;
        this.flightSpeedHorizontal = flightSpeedHorizontal;
        this.flightSpeedIsGround = flightSpeedIsGround;
        this.flightSpeedVertical = flightSpeedVertical;
        this.flightAltitude = flightAltitude;
    }

    public Flight(){

    }

    public int getId() {
        return id;
    }

    public String getFlightNumber(){
        return flightNumber;
    }

    public String getFlightStatus(){
        return flightStatus;
    }

    public String getFlightSpeedHorizontal() {
        return flightSpeedHorizontal;
    }

    public boolean getFlightSpeedIsGround() {
        return flightSpeedIsGround;
    }

    public String getFlightSpeedVertical() {
        return flightSpeedVertical;
    }

    public String getFlightAltitude() {
        return flightAltitude;
    }
}
