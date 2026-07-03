package edu.northeastern.a6atyourservice_team12.model;

public class DailyForecast {

    private String date;
    private String dayName;
    private double tempMax;
    private double tempMin;
    private int weatherCode;
    private double precipitation;
    private String temperatureUnit;


    public DailyForecast(String date, String dayName, double tempMax, double tempMin,
                         int weatherCode, double precipitation, String temperatureUnit) {
        this.date = date;
        this.dayName = dayName;
        this.tempMax = tempMax;
        this.tempMin = tempMin;
        this.weatherCode = weatherCode;
        this.precipitation = precipitation;
        this.temperatureUnit = temperatureUnit;
    }


    public DailyForecast() {
    }


    public String getDate() {
        return date;
    }

    public String getDayName() {
        return dayName;
    }

    public double getTempMax() {
        return tempMax;
    }

    public double getTempMin() {
        return tempMin;
    }

    public int getWeatherCode() {
        return weatherCode;
    }

    public double getPrecipitation() {
        return precipitation;
    }

    public String getTemperatureUnit() {
        return temperatureUnit;
    }


    public void setDate(String date) {
        this.date = date;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public void setTempMax(double tempMax) {
        this.tempMax = tempMax;
    }

    public void setTempMin(double tempMin) {
        this.tempMin = tempMin;
    }

    public void setWeatherCode(int weatherCode) {
        this.weatherCode = weatherCode;
    }

    public void setPrecipitation(double precipitation) {
        this.precipitation = precipitation;
    }

    public void setTemperatureUnit(String temperatureUnit) {
        this.temperatureUnit = temperatureUnit;
    }


    public String getFormattedTempMax() {
        return Math.round(tempMax) + temperatureUnit;
    }


    public String getFormattedTempMin() {
        return Math.round(tempMin) + temperatureUnit;
    }


    public String getFormattedPrecipitation() {
        if (precipitation == 0) {
            return "0 mm";
        }
        return String.format("%.1f mm", precipitation);
    }
}