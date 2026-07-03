package edu.northeastern.a6atyourservice_team12;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkHelper {
    private static final String GEOCODING_URL = "https://geocoding-api.open-meteo.com/v1/search";
    private static final String WEATHER_URL = "https://api.open-meteo.com/v1/forecast";
    private static final int MAX_RETRIES = 3;
    private static final int TIMEOUT_ms = 10000;

    private final ExecutorService executorService;

    public NetworkHelper() {
        this.executorService = Executors.newSingleThreadExecutor();
    }
    public interface LocationCallback {
        void onSuccess(Location location);
        void onError(String errorMessage);
    }

    public interface WeatherCallback {
        void onSuccess(Weatherdata weatherData);
        void onError(String errorMessage);
    }

    public void fetchLocation(String cityName, LocationCallback callback) {
        executorService.execute(() -> {
            Location location = fetchLocationWithRetry(cityName, MAX_RETRIES);

            if (location != null) {
                callback.onSuccess(location);
            } else {
                callback.onError("City not found or network error");
            }
        });
    }

    public void fetchWeather(Location location, int forecastDays, String temperatureUnit,
                             String windSpeedUnit, WeatherCallback callback) {
        executorService.execute(() -> {
            Weatherdata weatherData = fetchWeatherWithRetry(location, forecastDays,
                    temperatureUnit, windSpeedUnit, MAX_RETRIES);

            if (weatherData != null) {
                callback.onSuccess(weatherData);
            } else {
                callback.onError("Failed to fetch weather data");
            }
        });
    }

    private Location fetchLocationWithRetry(String cityName, int retriesLeft) {
        try {
            String encodedCity = URLEncoder.encode(cityName, "UTF-8");
            String urlString = GEOCODING_URL + "?name=" + encodedCity + "&count=1&language=en&format=json";

            WeatherLog.logApiRequest(urlString);
            String response = makeHttpRequest(urlString);

            if (response == null) {
                if (retriesLeft > 0) {
                    WeatherLog.w("Network", "Retrying location fetch... (" + retriesLeft + " retries left)");
                    Thread.sleep(1000);
                    return fetchLocationWithRetry(cityName, retriesLeft - 1);
                }
                return null;
            }

            return parseLocationResponse(response);

        } catch (Exception e) {
            WeatherLog.e("Network", "Error fetching location", e);
            if (retriesLeft > 0) {
                try {
                    Thread.sleep(1000);
                    return fetchLocationWithRetry(cityName, retriesLeft - 1);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
            return null;
        }
    }

    private Weatherdata fetchWeatherWithRetry(Location location, int forecastDays,
                                              String temperatureUnit, String windSpeedUnit,
                                              int retriesLeft) {
        try {
            String urlString = buildWeatherUrl(location, forecastDays, temperatureUnit, windSpeedUnit);

            WeatherLog.logApiRequest(urlString);
            String response = makeHttpRequest(urlString);

            if (response == null) {
                if (retriesLeft > 0) {
                    WeatherLog.w("Network", "Retrying weather fetch... (" + retriesLeft + " retries left)");
                    Thread.sleep(1000);
                    return fetchWeatherWithRetry(location, forecastDays, temperatureUnit,
                            windSpeedUnit, retriesLeft - 1);
                }
                return null;
            }

            return parseWeatherResponse(response, location, temperatureUnit, windSpeedUnit);

        } catch (Exception e) {
            WeatherLog.e("Network", "Error fetching weather", e);
            if (retriesLeft > 0) {
                try {
                    Thread.sleep(1000);
                    return fetchWeatherWithRetry(location, forecastDays, temperatureUnit,
                            windSpeedUnit, retriesLeft - 1);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
            return null;
        }
    }

    private String makeHttpRequest(String urlString) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(TIMEOUT_ms);
            connection.setReadTimeout(TIMEOUT_ms);

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                return response.toString();
            } else {
                WeatherLog.e("Network", "HTTP error code: " + responseCode);
                return null;
            }

        } catch (Exception e) {
            WeatherLog.e("Network", "HTTP request failed", e);
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    WeatherLog.e("Network", "Error closing reader", e);                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private String buildWeatherUrl(Location location, int forecastDays,
                                   String temperatureUnit, String windSpeedUnit) {
        return WEATHER_URL +
                "?latitude=" + location.getLatitude() +
                "&longitude=" + location.getLongitude() +
                "&current_weather=true" +
                "&daily=temperature_2m_max,temperature_2m_min,weathercode,precipitation_sum" +
                "&forecast_days=" + forecastDays +
                "&temperature_unit=" + temperatureUnit +
                "&wind_speed_unit=" + windSpeedUnit +
                "&timezone=auto";
    }

    private Location parseLocationResponse(String jsonResponse) {
        try {
            JSONObject root = new JSONObject(jsonResponse);

            if (!root.has("results") || root.getJSONArray("results").length() == 0) {
                WeatherLog.e("Network", "No results found in geocoding response");
                return null;
            }

            JSONArray results = root.getJSONArray("results");
            JSONObject firstResult = results.getJSONObject(0);

            String name = firstResult.getString("name");
            double latitude = firstResult.getDouble("latitude");
            double longitude = firstResult.getDouble("longitude");
            String country = firstResult.optString("country", "");
            String admin1 = firstResult.optString("admin1", "");

            return new Location(name, latitude, longitude, country, admin1);

        } catch (Exception e) {
            WeatherLog.e("Network", "Error parsing location response", e);
            return null;
        }
    }

    private Weatherdata parseWeatherResponse(String jsonResponse, Location location,
                                             String temperatureUnit, String windSpeedUnit) {
        try {
            JSONObject root = new JSONObject(jsonResponse);
            JSONObject currentWeather = root.getJSONObject("current_weather");
            double currentTemp = currentWeather.getDouble("temperature");
            int currentWeatherCode = currentWeather.getInt("weathercode");
            double currentWindSpeed = currentWeather.getDouble("windspeed");
            JSONObject daily = root.getJSONObject("daily");
            JSONArray times = daily.getJSONArray("time");
            JSONArray tempMaxArray = daily.getJSONArray("temperature_2m_max");
            JSONArray tempMinArray = daily.getJSONArray("temperature_2m_min");
            JSONArray weatherCodeArray = daily.getJSONArray("weathercode");
            JSONArray precipitationArray = daily.getJSONArray("precipitation_sum");

            List<Dailyforecast> forecasts = new ArrayList<>();

            for (int i = 0; i < times.length(); i++) {
                String date = times.getString(i);
                double tempMax = tempMaxArray.getDouble(i);
                double tempMin = tempMinArray.getDouble(i);
                int weatherCode = weatherCodeArray.getInt(i);
                double precipitation = precipitationArray.getDouble(i);

                forecasts.add(new Dailyforecast(date, tempMax, tempMin, weatherCode, precipitation));
            }

            return new Weatherdata(location, currentTemp, currentWeatherCode, currentWindSpeed,
                    forecasts, temperatureUnit, windSpeedUnit);

        } catch (Exception e) {
            WeatherLog.e("Network", "Error parsing weather response", e);
            return null;
        }
    }

    public void shutdown() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}