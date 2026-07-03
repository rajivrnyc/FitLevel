package edu.northeastern.a6atyourservice_team12.util;

import edu.northeastern.a6atyourservice_team12.R;

/**
 * Utility class to map Open-Meteo weather codes to drawable icons
 *
 * Person 4 (Sandy)
 *
 * Weather Code Reference (Open-Meteo):
 * 0: Clear sky
 * 1, 2, 3: Mainly clear, partly cloudy, overcast
 * 45, 48: Fog and depositing rime fog
 * 51, 53, 55: Drizzle: Light, moderate, dense
 * 56, 57: Freezing Drizzle: Light, dense
 * 61, 63, 65: Rain: Slight, moderate, heavy
 * 66, 67: Freezing Rain: Light, heavy
 * 71, 73, 75: Snow fall: Slight, moderate, heavy
 * 77: Snow grains
 * 80, 81, 82: Rain showers: Slight, moderate, violent
 * 85, 86: Snow showers slight and heavy
 * 95: Thunderstorm: Slight or moderate
 * 96, 99: Thunderstorm with slight and heavy hail
 */
public class WeatherIconHelper {

    /**
     * Returns the drawable resource ID for a given weather code
     * @param weatherCode The weather code from Open-Meteo API
     * @return Drawable resource ID
     */
    public static int getWeatherIcon(int weatherCode) {
        switch (weatherCode) {
            case 0:
                // Clear sky
                return R.drawable.ic_weather_sunny;

            case 1:
            case 2:
            case 3:
                // Partly cloudy
                return R.drawable.ic_weather_partly_cloudy;

            case 45:
            case 48:
                // Fog
                return R.drawable.ic_weather_fog;

            case 51:
            case 53:
            case 55:
            case 56:
            case 57:
                // Drizzle
                return R.drawable.ic_weather_drizzle;

            case 61:
            case 63:
            case 65:
            case 66:
            case 67:
            case 80:
            case 81:
            case 82:
                // Rain
                return R.drawable.ic_weather_rainy;

            case 71:
            case 73:
            case 75:
            case 77:
            case 85:
            case 86:
                // Snow
                return R.drawable.ic_weather_snowy;

            case 95:
            case 96:
            case 99:
                // Thunderstorm
                return R.drawable.ic_weather_thunderstorm;

            default:
                // Default to cloudy for unknown codes
                return R.drawable.ic_weather_cloudy;
        }
    }

    /**
     * Returns a human-readable weather description for a given weather code
     * @param weatherCode The weather code from Open-Meteo API
     * @return Weather description string
     */
    public static String getWeatherDescription(int weatherCode) {
        switch (weatherCode) {
            case 0:
                return "Clear sky";
            case 1:
                return "Mainly clear";
            case 2:
                return "Partly cloudy";
            case 3:
                return "Overcast";
            case 45:
                return "Fog";
            case 48:
                return "Depositing rime fog";
            case 51:
                return "Light drizzle";
            case 53:
                return "Moderate drizzle";
            case 55:
                return "Dense drizzle";
            case 56:
                return "Light freezing drizzle";
            case 57:
                return "Dense freezing drizzle";
            case 61:
                return "Slight rain";
            case 63:
                return "Moderate rain";
            case 65:
                return "Heavy rain";
            case 66:
                return "Light freezing rain";
            case 67:
                return "Heavy freezing rain";
            case 71:
                return "Slight snow";
            case 73:
                return "Moderate snow";
            case 75:
                return "Heavy snow";
            case 77:
                return "Snow grains";
            case 80:
                return "Slight rain showers";
            case 81:
                return "Moderate rain showers";
            case 82:
                return "Violent rain showers";
            case 85:
                return "Slight snow showers";
            case 86:
                return "Heavy snow showers";
            case 95:
                return "Thunderstorm";
            case 96:
                return "Thunderstorm with slight hail";
            case 99:
                return "Thunderstorm with heavy hail";
            default:
                return "Unknown";
        }
    }
}