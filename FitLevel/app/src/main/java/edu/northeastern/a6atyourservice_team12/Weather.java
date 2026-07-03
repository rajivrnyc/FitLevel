package edu.northeastern.a6atyourservice_team12;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.northeastern.a6atyourservice_team12.adapter.ForecastAdapter;
import edu.northeastern.a6atyourservice_team12.model.DailyForecast;
import edu.northeastern.a6atyourservice_team12.util.WeatherIconHelper;

import java.util.ArrayList;
import java.util.List;

public class Weather extends AppCompatActivity {
    private static final String TAG = "WeatherActivity";

    private NetworkHelper networkHelper;

    private EditText editCity;
    private Spinner spinnerDays;
    private Button btnSearch;
    private RadioGroup radioGroupTemp;
    private Switch switchWind;

    private LinearLayout layoutEmpty, layoutLoading, layoutError;
    private TextView textLoading, textError;
    private Button btnRetry;

    private CardView cardWeather;
    private TextView textCityName, textCurrentTemp, textWeatherDesc, textWindSpeed;
    private ImageView imgCurrentWeather;

    private RecyclerView recyclerForecast;
    private ForecastAdapter forecastAdapter;
    private List<DailyForecast> forecastList = new ArrayList<>();

    private Handler loadingHandler = new Handler(Looper.getMainLooper());
    private int dotCount = 0;
    private boolean isLoading = false;

    private Runnable loadingRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isLoading) return;
            dotCount = (dotCount % 3) + 1;
            String dots = new String(new char[dotCount]).replace('\0', '.');
            textLoading.setText("Fetching weather" + dots);
            loadingHandler.postDelayed(this, 500);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_weather);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize network helper
        networkHelper = new NetworkHelper();

        initViews();
        setupSpinner();
        setupRecyclerView();
        setupListeners();
        showState(AppState.EMPTY);
    }

    private void initViews() {

        editCity = findViewById(R.id.editCity);
        spinnerDays = findViewById(R.id.spinnerDays);
        btnSearch = findViewById(R.id.btnSearch);
        radioGroupTemp = findViewById(R.id.radioGroupTemp);
        switchWind = findViewById(R.id.switchWind);

        layoutEmpty = findViewById(R.id.searchEmpty);
        layoutLoading = findViewById(R.id.layoutLoading);
        layoutError = findViewById(R.id.layoutError);
        textLoading = findViewById(R.id.textLoading);
        textError = findViewById(R.id.textError);
        btnRetry = findViewById(R.id.btnRetry);


        cardWeather = findViewById(R.id.cardWeather);
        textCityName = findViewById(R.id.textCityName);
        textCurrentTemp = findViewById(R.id.textCurrentTemp);
        textWeatherDesc = findViewById(R.id.textWeatherDesc);
        textWindSpeed = findViewById(R.id.textWindSpeed);
        imgCurrentWeather = findViewById(R.id.imgCurrentWeather);


        recyclerForecast = findViewById(R.id.recyclerForecast);
    }

    private void setupSpinner() {
        String[] forecastDays = {"3 days", "5 days", "7 days"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item,
                forecastDays);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDays.setAdapter(adapter);
        spinnerDays.setSelection(2);
    }


    private void setupRecyclerView() {
        forecastAdapter = new ForecastAdapter(this, forecastList);
        recyclerForecast.setLayoutManager(new LinearLayoutManager(this));
        recyclerForecast.setAdapter(forecastAdapter);
    }

    private void setupListeners() {
        btnSearch.setOnClickListener(v -> onSearchClicked());
        btnRetry.setOnClickListener(v -> onSearchClicked());
    }

    private void onSearchClicked() {
        String city = editCity.getText().toString().trim();

        if (city.isEmpty()) {
            Toast.makeText(this, "Please enter a city", Toast.LENGTH_SHORT).show();
            editCity.requestFocus();
            return;
        }

        int forecastDays = getSelectedForecastDays();
        String tempUnit = getSelectedTempUnit();
        String windUnit = getSelectedWindUnit();

        Log.d(TAG, "Searching: city=" + city + " days=" + forecastDays +
                " temp=" + tempUnit + " wind=" + windUnit);

        showState(AppState.LOADING);


        fetchWeatherData(city, forecastDays, tempUnit, windUnit);
    }

    private void fetchWeatherData(String cityName, int forecastDays,
                                  String tempUnit, String windUnit) {

        networkHelper.fetchLocation(cityName, new NetworkHelper.LocationCallback() {
            @Override
            public void onSuccess(Location location) {
                Log.d(TAG, "Location found: " + location.getFullLocationName());


                networkHelper.fetchWeather(location, forecastDays, tempUnit, windUnit,
                        new NetworkHelper.WeatherCallback() {
                            @Override
                            public void onSuccess(Weatherdata weatherData) {
                                // Update UI on main thread
                                runOnUiThread(() -> {
                                    displayWeatherData(weatherData, tempUnit);
                                });
                            }

                            @Override
                            public void onError(String errorMessage) {
                                runOnUiThread(() -> {
                                    showError(errorMessage);
                                });
                            }
                        });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> {
                    showError("City not found: " + cityName);
                });
            }
        });
    }

    private void displayWeatherData(Weatherdata weatherData, String tempUnit) {
        Log.d(TAG, "Displaying weather data");


        textCityName.setText(weatherData.getLocation().getFullLocationName());
        textCurrentTemp.setText(weatherData.getTemperatureDisplay());
        textWeatherDesc.setText(WeatherIconHelper.getWeatherDescription(weatherData.getCurrentWeatherCode()));
        textWindSpeed.setText("Wind: " + weatherData.getWindSpeedDisplay());


        int iconRes = WeatherIconHelper.getWeatherIcon(weatherData.getCurrentWeatherCode());
        imgCurrentWeather.setImageResource(iconRes);

        String unitSymbol = tempUnit.equals("celsius") ? "°C" : "°F";
        List<DailyForecast> forecasts = new ArrayList<>();

        for (Dailyforecast df : weatherData.getDailyForecasts()) {
            DailyForecast forecast = new DailyForecast(
                    df.getDate(),
                    df.getDayName(),
                    df.getTempMax(),
                    df.getTempMin(),
                    df.getWeatherCode(),
                    df.getPrecipitationSum(),
                    unitSymbol
            );
            forecasts.add(forecast);
        }


        forecastAdapter.updateData(forecasts);


        showState(AppState.SUCCESS);
    }

    public int getSelectedForecastDays() {
        String selected = spinnerDays.getSelectedItem().toString();
        switch (selected) {
            case "3 days": return 3;
            case "5 days": return 5;
            default: return 7;
        }
    }

    public String getSelectedTempUnit() {
        int checkedId = radioGroupTemp.getCheckedRadioButtonId();
        if (checkedId == R.id.radioCelsius) {
            return "celsius";
        }
        return "fahrenheit";
    }

    public String getSelectedWindUnit() {
        return switchWind.isChecked() ? "mph" : "kmh";
    }

    enum AppState {
        EMPTY, LOADING, SUCCESS, ERROR
    }

    void showState(AppState state) {
        layoutEmpty.setVisibility(View.GONE);
        layoutLoading.setVisibility(View.GONE);
        layoutError.setVisibility(View.GONE);
        cardWeather.setVisibility(View.GONE);
        recyclerForecast.setVisibility(View.GONE);

        stopLoadingAnimation();

        switch (state) {
            case EMPTY:
                layoutEmpty.setVisibility(View.VISIBLE);
                break;
            case LOADING:
                layoutLoading.setVisibility(View.VISIBLE);
                startLoadingAnimation();
                break;
            case SUCCESS:
                cardWeather.setVisibility(View.VISIBLE);
                recyclerForecast.setVisibility(View.VISIBLE);
                break;
            case ERROR:
                layoutError.setVisibility(View.VISIBLE);
                break;
        }
    }

    void showError(String message) {
        textError.setText(message);
        showState(AppState.ERROR);
    }

    private void startLoadingAnimation() {
        isLoading = true;
        dotCount = 0;
        loadingHandler.post(loadingRunnable);
    }

    private void stopLoadingAnimation() {
        isLoading = false;
        loadingHandler.removeCallbacks(loadingRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLoadingAnimation();
        if (networkHelper != null) {
            networkHelper.shutdown();
        }
    }
}