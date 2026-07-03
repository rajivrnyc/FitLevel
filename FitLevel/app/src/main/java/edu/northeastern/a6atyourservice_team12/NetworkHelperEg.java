package edu.northeastern.a6atyourservice_team12;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

public class NetworkHelperEg {
    private static final String TAG = "NetworkExample";
    private NetworkHelper networkHelper;
    private Handler mainHandler;

    public NetworkHelperEg() {
        this.networkHelper = new NetworkHelper();
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    public void fetchWeatherExample(String cityName, int forecastDays,
                                    String temperatureUnit, String windSpeedUnit) {

        Log.d(TAG, "Starting weather fetch for: " + cityName);

        networkHelper.fetchLocation(cityName, new NetworkHelper.LocationCallback() {
            @Override
            public void onSuccess(Location location) {
                Log.d(TAG, "Location found: " + location.getFullLocationName());

                networkHelper.fetchWeather(location, forecastDays, temperatureUnit,
                        windSpeedUnit, new NetworkHelper.WeatherCallback() {

                            @Override
                            public void onSuccess(Weatherdata weatherData) {
                                mainHandler.post(() -> {
                                    handleWeatherSuccess(weatherData);
                                });
                            }

                            @Override
                            public void onError(String errorMessage) {
                                mainHandler.post(() -> {
                                    handleWeatherError(errorMessage);
                                });
                            }
                        });
            }

            @Override
            public void onError(String errorMessage) {
                mainHandler.post(() -> {
                    handleLocationError(errorMessage);
                });
            }
        });
    }

    private void handleWeatherSuccess(Weatherdata weatherData) {
        Log.d(TAG, "=== WEATHER DATA RECEIVED ===");
        Log.d(TAG, "Location: " + weatherData.getLocation().getFullLocationName());
        Log.d(TAG, "Current Temp: " + weatherData.getTemperatureDisplay());
        Log.d(TAG, "Wind Speed: " + weatherData.getWindSpeedDisplay());
        Log.d(TAG, "Weather Code: " + weatherData.getCurrentWeatherCode());
        Log.d(TAG, "Number of forecasts: " + weatherData.getDailyForecasts().size());

        for (Dailyforecast forecast : weatherData.getDailyForecasts()) {
            Log.d(TAG, String.format("%s: High %.0f° / Low %.0f° - Precipitation: %.1fmm",
                    forecast.getDayName(),
                    forecast.getTempMax(),
                    forecast.getTempMin(),
                    forecast.getPrecipitationSum()));
        }

    }

    private void handleLocationError(String errorMessage) {
        Log.e(TAG, "Location error: " + errorMessage);
    }

    private void handleWeatherError(String errorMessage) {
        Log.e(TAG, "Weather error: " + errorMessage);

    }

    public void cleanup() {
        if (networkHelper != null) {
            networkHelper.shutdown();
        }
    }
}