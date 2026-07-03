package edu.northeastern.a6atyourservice_team12;

public class Dailyforecast {
    private String date;
    private double tempMax;
    private double tempMin;
    private int weatherCode;
    private double precipitationSum;

    public Dailyforecast(String date, double tempMax, double tempMin, int weatherCode, double precipitationSum) {
        this.date = date;
        this.tempMax = tempMax;
        this.tempMin = tempMin;
        this.weatherCode = weatherCode;
        this.precipitationSum = precipitationSum;
    }
    public String getDate() {
        return date;
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
    public double getPrecipitationSum() {
        return precipitationSum;
    }
    public String getDayName() {
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US);
            java.util.Date d = sdf.parse(date);
            java.text.SimpleDateFormat dayFormat = new java.text.SimpleDateFormat("EEEE", java.util.Locale.US);
            return dayFormat.format(d);
        } catch (Exception e) {
            return date;
        }
    }
}