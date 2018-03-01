package com.example.mac.airnow;

import java.util.List;


public class LocationItem {
    public String city;
    public String lat;
    public String lon;

    public void setLocation(String city, String latitude, String longitude){
        this.city = city;
        this.lat = latitude;
        this.lon = longitude;
    }

    public String getCity() {
        return city;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }
}
