/*
 * Copyright (c) 2015. Tyler McCraw
 */

package com.w3bshark.android_weather.utils;

import com.w3bshark.android_weather.model.Day;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class OWMDataParser {

    // These are the names of the JSON objects that need to be extracted.
    private final static String OWM_LIST = "list";
    private final static String OWM_WEATHER = "weather";
    private final static String OWM_TEMPERATURE = "temp";
    private final static String OWM_MAX = "max";
    private final static String OWM_MIN = "min";
    private final static String OWM_DESCRIPTION = "main";
    private final static String OWM_ICON = "icon";
    private final static String OWM_HUMIDITY = "humidity";
    private final static String OWM_PRESSURE = "pressure";
    private final static String OWM_WIND = "speed";

    /**
     * Take the String representing the complete forecast in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     *
     * Fortunately parsing is easy: constructor takes the JSON string and converts it
     * into an Object hierarchy for us.
     */
    public static ArrayList<Day> getWeatherDataFromJson(String forecastJsonStr)
            throws JSONException {

        JSONObject forecastJson = new JSONObject(forecastJsonStr);
        JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);

        // OWM returns daily forecasts based upon the local time of the city that is being
        // asked for, which means that we need to know the GMT offset to translate this data
        // properly.

        // Since this data is also sent in-order and the first day is always the
        // current day, we're going to take advantage of that to get a nice
        // normalized UTC date for all of our weather.


        ArrayList<Day> forecastDays = new ArrayList<>();
        for (int i = 0; i < weatherArray.length(); i++) {
            // Get the JSON object representing the day
            JSONObject dayForecast = weatherArray.getJSONObject(i);

            GregorianCalendar gc = new GregorianCalendar();
            gc.add(Calendar.DAY_OF_MONTH, i);

            // description is in a child array called "weather", which is 1 element long.
            JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
            String description = weatherObject.getString(OWM_DESCRIPTION);
            String icon = weatherObject.getString(OWM_ICON);

            Double humidity = dayForecast.getDouble(OWM_HUMIDITY);
            Double pressure = dayForecast.getDouble(OWM_PRESSURE);
            Double wind = dayForecast.getDouble(OWM_WIND);

            // Temperatures are in a child object called "temp".  Try not to name variables
            // "temp" when working with temperature.  It confuses everybody.
            JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);

            Day currDay = new Day();
            currDay.setDateInMillis(gc.getTimeInMillis());
            currDay.setTempMax(temperatureObject.getDouble(OWM_MAX));
            currDay.setTempMin(temperatureObject.getDouble(OWM_MIN));
            currDay.setWeatherDescription(description);
            currDay.setIconCode(icon);
            currDay.setHumidity(humidity);
            currDay.setPressure(pressure);
            currDay.setWind(wind);
            forecastDays.add(currDay);
        }

        return forecastDays;
    }
}
