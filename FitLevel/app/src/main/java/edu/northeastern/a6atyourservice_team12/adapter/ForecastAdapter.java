package edu.northeastern.a6atyourservice_team12.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import edu.northeastern.a6atyourservice_team12.R;
import edu.northeastern.a6atyourservice_team12.model.DailyForecast;
import edu.northeastern.a6atyourservice_team12.util.WeatherIconHelper;

import java.util.ArrayList;
import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {

    private List<DailyForecast> forecastList;
    private Context context;
    private OnForecastClickListener clickListener;
    private int lastAnimatedPosition = -1;

    /**
     * Constructor
     * @param context Activity context
     * @param forecastList List of DailyForecast objects
     */
    public ForecastAdapter(Context context, List<DailyForecast> forecastList) {
        this.context = context;
        this.forecastList = forecastList != null ? forecastList : new ArrayList<>();
    }

    /**
     * Set click listener for forecast items
     * @param listener OnForecastClickListener implementation
     */
    public void setOnForecastClickListener(OnForecastClickListener listener) {
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_forecast, parent, false);
        return new ForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastViewHolder holder, int position) {
        DailyForecast forecast = forecastList.get(position);


        holder.tvDayName.setText(forecast.getDayName());


        holder.tvDate.setText(formatDate(forecast.getDate()));


        int iconRes = WeatherIconHelper.getWeatherIcon(forecast.getWeatherCode());
        holder.ivWeatherIcon.setImageResource(iconRes);


        String weatherDesc = WeatherIconHelper.getWeatherDescription(forecast.getWeatherCode());
        holder.ivWeatherIcon.setContentDescription(weatherDesc);


        holder.tvTempHigh.setText(forecast.getFormattedTempMax());


        holder.tvTempLow.setText(forecast.getFormattedTempMin());


        holder.tvPrecipitation.setText(forecast.getFormattedPrecipitation());


        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onForecastClick(forecast, position);
            }
        });


        if (position > lastAnimatedPosition) {
            setFadeInAnimation(holder.itemView, position);
            lastAnimatedPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return forecastList != null ? forecastList.size() : 0;
    }

    /**
     * Update the forecast list and refresh RecyclerView
     * @param newForecastList New list of DailyForecast objects
     */
    public void updateData(List<DailyForecast> newForecastList) {
        this.forecastList = newForecastList != null ? newForecastList : new ArrayList<>();
        this.lastAnimatedPosition = -1;
        notifyDataSetChanged();
    }

    /**
     * Clear all items from the adapter
     */
    public void clearData() {
        this.forecastList.clear();
        this.lastAnimatedPosition = -1;
        notifyDataSetChanged();
    }

    /**
     * Apply fade-in animation to item
     * @param view The item view to animate
     * @param position Position in the list (used for stagger delay)
     */
    private void setFadeInAnimation(View view, int position) {
        view.setAlpha(0f);
        view.animate()
                .alpha(1f)
                .setDuration(300)
                .setStartDelay(position * 50L) // Stagger effect
                .start();
    }

    /**
     * Format date string from "2024-02-10" to "Feb 10"
     * @param dateString Date in YYYY-MM-DD format
     * @return Formatted date string
     */
    private String formatDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return "";
        }

        try {
            String[] parts = dateString.split("-");
            if (parts.length == 3) {
                int month = Integer.parseInt(parts[1]);
                int day = Integer.parseInt(parts[2]);

                String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
                        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

                return monthNames[month - 1] + " " + day;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dateString;
    }

    /**
     * Reset animation position (call when refreshing data)
     */
    public void resetAnimation() {
        lastAnimatedPosition = -1;
    }

    /**
     * ViewHolder class for forecast items
     * Caches view references for better performance
     */
    public static class ForecastViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView tvDayName;
        TextView tvDate;
        ImageView ivWeatherIcon;
        TextView tvTempHigh;
        TextView tvTempLow;
        TextView tvPrecipitation;

        public ForecastViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardForecast);
            tvDayName = itemView.findViewById(R.id.tvDayName);
            tvDate = itemView.findViewById(R.id.tvDate);
            ivWeatherIcon = itemView.findViewById(R.id.ivWeatherIcon);
            tvTempHigh = itemView.findViewById(R.id.tvTempHigh);
            tvTempLow = itemView.findViewById(R.id.tvTempLow);
            tvPrecipitation = itemView.findViewById(R.id.tvPrecipitation);
        }
    }
}