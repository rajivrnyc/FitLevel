package edu.northeastern.a6atyourservice_team12;

import android.util.Log;

public class WeatherLog {
    private static final String TAG_prefix = "Weather ";
    private static boolean Log_enabled = true;
    public static void d(String tag, String message) {
        if (Log_enabled) {
            Log.d(TAG_prefix + tag, message);
        }
    }
    public static void e(String tag, String message) {
        if (Log_enabled) {
            Log.e(TAG_prefix + tag, message);
        }
    }
    public static void e(String tag, String message, Throwable throwable) {
        if (Log_enabled) {
            Log.e(TAG_prefix + tag, message, throwable);
        }
    }
    public static void w(String tag, String message) {
        if (Log_enabled) {
            Log.w(TAG_prefix + tag, message);
        }
    }
    public static void i(String tag, String message) {
        if (Log_enabled) {
            Log.i(TAG_prefix + tag, message);
        }
    }
    public static void logApiRequest(String url) {
        if (Log_enabled) {
            Log.d(TAG_prefix + "API", "REQUEST: " + url);
        }
    }
    public static void logApiResponse(String endpoint, String response) {
        if (Log_enabled) {
            Log.d(TAG_prefix + "API", "RESPONSE from " + endpoint + ": " +
                    (response.length() > 500 ? response.substring(0, 500) + "..." : response));
        }
    }
    public static void logApiError(String endpoint, String error) {
        if (Log_enabled) {
            Log.e(TAG_prefix + "API", "ERROR from " + endpoint + ": " + error);
        }
    }
}