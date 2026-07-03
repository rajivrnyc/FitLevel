package edu.northeastern.a6atyourservice_team12.adapter;

import edu.northeastern.a6atyourservice_team12.model.DailyForecast;

public interface OnForecastClickListener {

    /**
     * Called when a forecast item is clicked
     * @param forecast The DailyForecast object that was clicked
     * @param position The position in the adapter
     */
    void onForecastClick(DailyForecast forecast, int position);
}