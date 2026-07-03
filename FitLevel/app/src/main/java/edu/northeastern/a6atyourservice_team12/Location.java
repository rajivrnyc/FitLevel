package edu.northeastern.a6atyourservice_team12;

public class Location {
    private String name;
    private double latitude;
    private double longitude;
    private String country;
    private String admin1;

    public Location(String name, double latitude, double longitude, String country, String admin1) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.admin1 = admin1;
    }
    public String getName() {
        return name;
    }
    public double getLatitude() {
        return latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public String getCountry() {
        return country;
    }
    public String getAdmin1() {
        return admin1;
    }
    public String getFullLocationName() {
        if (admin1 != null && !admin1.isEmpty()) {
            return name + ", " + admin1;
        }
        return name + ", " + country;
    }
}