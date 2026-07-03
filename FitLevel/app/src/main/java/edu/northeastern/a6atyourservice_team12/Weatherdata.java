package edu.northeastern.a6atyourservice_team12;

import java.util.List;

public class Weatherdata {
    private Location location;
    private double currentTemp;
    private int currentWeatherCode;
    private double currentWindSpeed;
    private List<Dailyforecast> dailyForecasts;
    private String temperatureUnit;
    private String windSpeedUnit;
    public Weatherdata(Location location, double currentTemperature, int currentWeatherCode,
                       double currentWindSpeed, List<Dailyforecast> dailyForecasts,
                       String temperatureUnit, String windSpeedUnit) {
        this.location = location;
        this.currentTemp = currentTemperature;
        this.currentWeatherCode = currentWeatherCode;
        this.currentWindSpeed = currentWindSpeed;
        this.dailyForecasts = dailyForecasts;
        this.temperatureUnit = temperatureUnit;
        this.windSpeedUnit = windSpeedUnit;
    }
    public Location getLocation() {
        return location;
    }
    public double getCurrentTemperature() {
        return currentTemp;
    }
    public int getCurrentWeatherCode() {
        return currentWeatherCode;
    }
    public double getCurrentWindSpeed() {
        return currentWindSpeed;
    }
    public List<Dailyforecast> getDailyForecasts() {
        return dailyForecasts;
    }
    public String getTemperatureUnit() {
        return temperatureUnit;
    }
    public String getWindSpeedUnit() {
        return windSpeedUnit;
    }
    public String getTemperatureDisplay() {
        return String.format("%.0f°%s", currentTemp,
                temperatureUnit.equals("fahrenheit") ? "F" : "C");
    }
    public String getWindSpeedDisplay() {
        return String.format("%.1f %s", currentWindSpeed, windSpeedUnit);
    }
}