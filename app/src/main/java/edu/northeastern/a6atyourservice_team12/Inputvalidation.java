package edu.northeastern.a6atyourservice_team12;

public class Inputvalidation {
    public static boolean isValidCityName(String cityName) {
        if (cityName == null || cityName.trim().isEmpty()) {
            return false;
        }
        String trimmed = cityName.trim();
        if (trimmed.length() < 2) {
            return false;
        }

        return trimmed.matches("[a-zA-Z][a-zA-Z\\s\\-']*");
    }

    public static boolean isValidForecastDays(int days) {
        return days == 3 || days == 5 || days == 7;
    }

    public static boolean isValidTemperatureUnit(String unit) {
        return unit != null && (unit.equals("celsius") || unit.equals("fahrenheit"));
    }

    public static boolean isValidWindSpeedUnit(String unit) {
        return unit != null && (unit.equals("kmh") || unit.equals("mph"));
    }

    public static String getCityNameError(String cityName) {
        if (cityName == null || cityName.trim().isEmpty()) {
            return "Please enter a city name";
        }
        if (cityName.trim().length() < 2) {
            return "City name must be at least 2 characters";
        }
        if (!cityName.trim().matches("[a-zA-Z][a-zA-Z\\s\\-']*")) {
            return "City name can only contain letters, spaces, hyphens, and apostrophes";
        }
        return null;
    }
}