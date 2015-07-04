package com.w3bshark.android_weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * Created by w3bshark on 7/3/2015.
 */
public class OWMDataParser {

    private final static String LOG_TAG = OWMDataParser.class.getSimpleName();

    // These are the names of the JSON objects that need to be extracted.
    private final static String OWM_LIST = "list";
    private final static String OWM_WEATHER = "weather";
    private final static String OWM_TEMPERATURE = "temp";
    private final static String OWM_MAX = "max";
    private final static String OWM_MIN = "min";
    private final static String OWM_DESCRIPTION = "main";

    /**
     * Take the String representing the complete forecast in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     *
     * Fortunately parsing is easy: constructor takes the JSON string and converts it
     * into an Object hierarchy for us.
     */
    public static ArrayList<Day> getWeatherDataFromJson(String forecastJsonStr, int numDays)
            throws JSONException {

        JSONObject forecastJson = new JSONObject(forecastJsonStr);
        JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);

        // OWM returns daily forecasts based upon the local time of the city that is being
        // asked for, which means that we need to know the GMT offset to translate this data
        // properly.

        // Since this data is also sent in-order and the first day is always the
        // current day, we're going to take advantage of that to get a nice
        // normalized UTC date for all of our weather.

        GregorianCalendar gc = new GregorianCalendar();

        ArrayList<Day> forecastDays = new ArrayList<>();
        for(int i = 0; i < weatherArray.length(); i++) {
            String description;

            // Get the JSON object representing the day
            JSONObject dayForecast = weatherArray.getJSONObject(i);

            gc.add(GregorianCalendar.DAY_OF_YEAR, i);

            // description is in a child array called "weather", which is 1 element long.
            JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
            description = weatherObject.getString(OWM_DESCRIPTION);

            // Temperatures are in a child object called "temp".  Try not to name variables
            // "temp" when working with temperature.  It confuses everybody.
            JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
            double high = temperatureObject.getDouble(OWM_MAX);
//            double low = temperatureObject.getDouble(OWM_MIN);

            Day currDay = new Day(gc, high, Util.WeatherTypes.CLOUDY, 0);
            forecastDays.add(currDay);
        }

        return forecastDays;

    }
}
