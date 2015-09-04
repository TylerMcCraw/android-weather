/*
 * Copyright (c) 2015. Tyler McCraw
 */

package com.w3bshark.android_weather.rest;

import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.w3bshark.android_weather.utils.OWMDataParser;
import com.w3bshark.android_weather.R;
import com.w3bshark.android_weather.model.Day;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class TenDayForecastHandler extends AsyncTask <Location, Void, ArrayList<Day>> {

    private final static String LOG_TAG = TenDayForecastHandler.class.getSimpleName();
    private Context context;
    ArrayList<Day> tenDayForecast;

    private final static String OWM_BASEURL = "api.openweathermap.org";
    private final static String OWM_VERSION = "2.5";
    private final static String OWM_DATA = "data";
    private final static String OWM_FORECAST = "forecast";
    private final static String OWM_DAILY = "daily";
    private final static String OWM_PARAM_LATITUDE = "lat";
    private final static String OWM_PARAM_LONGITUDE = "lon";
    private final static String OWM_PARAM_MODE = "mode";
    private final static String OWM_PARAM_UNITS = "units";
    private final static String OWM_PARAM_DAYCOUNT = "cnt";
    private final static String OWM_PARAM_APIKEY = "APPID";
    private final static String OWM_APIKEY = "";
    private String dataProtocol = "http";
    private String dataMode = "json";
    private String dataUnits = "imperial";
    private String dataDays = "10";
    private HttpURLConnection urlConnection = null;
    private BufferedReader reader = null;

    public TenDayForecastHandler(Context context) {
        this.context = context;
    }

    @Override
    protected ArrayList<Day> doInBackground(Location... params) {
        tenDayForecast = getTenDayForecast(params[0]);
        return tenDayForecast;
    }

    private ArrayList<Day> getTenDayForecast(Location location) {

        ArrayList<Day> forecastDays = null;
        String forecastResponse;

        try {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme(dataProtocol)
                    .authority(OWM_BASEURL)
                    .appendPath(OWM_DATA)
                    .appendPath(OWM_VERSION)
                    .appendPath(OWM_FORECAST)
                    .appendPath(OWM_DAILY)
                    .appendQueryParameter(OWM_PARAM_LATITUDE, Double.toString(location.getLatitude()))
                    .appendQueryParameter(OWM_PARAM_LONGITUDE, Double.toString(location.getLongitude()))
                    .appendQueryParameter(OWM_PARAM_MODE, dataMode)
                    .appendQueryParameter(OWM_PARAM_UNITS, dataUnits)
                    .appendQueryParameter(OWM_PARAM_DAYCOUNT, dataDays)
                    .appendQueryParameter(OWM_PARAM_APIKEY, OWM_APIKEY);
            String mUrl = builder.build().toString();
            URL url = new URL (mUrl);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            forecastResponse = buffer.toString();

            try {
                forecastDays = OWMDataParser.getWeatherDataFromJson(forecastResponse);
            } catch (JSONException e) {
                //TODO: handle exception appropriately
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, context.getString(R.string.error), e);
            return null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, context.getString(R.string.error_closing_stream), e);
                }
            }
        }

        return forecastDays;
    }

}
