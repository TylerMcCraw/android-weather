package com.w3bshark.android_weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by w3bshark on 7/3/2015.
 */
public class OWMDataParser {

    // These are the names of the JSON objects that need to be extracted.
    private final static String OWM_LIST = "list";
    private final static String OWM_WEATHER = "weather";
    private final static String OWM_TEMPERATURE = "temp";
    private final static String OWM_MAX = "max";
    private final static String OWM_MIN = "min";
    private final static String OWM_DESCRIPTION = "main";
    private final static String OWM_ICON = "icon";

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

            // Temperatures are in a child object called "temp".  Try not to name variables
            // "temp" when working with temperature.  It confuses everybody.
            JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);

            int iconID;
            if (i == 0) {
                switch (icon)
                {
                    case "01d":
                        iconID = R.drawable.weather_sunny_white;
                        break;
                    case "01n":
                        iconID = R.drawable.weather_night_white;
                        break;
                    case "02d":
                    case "02n":
                        iconID = R.drawable.weather_partlycloudy_white;
                        break;
                    case "03d":
                    case "03n":
                        iconID = R.drawable.weather_cloudy_white;
                        break;
                    case "04d":
                    case "04n":
                        iconID = R.drawable.weather_partlycloudy_white;
                        break;
                    case "09d":
                    case "09n":
                        iconID = R.drawable.weather_pouring_white;
                        break;
                    case "10d":
                    case "10n":
                        iconID = R.drawable.weather_rainy_white;
                        break;
                    case "11d":
                    case "11n":
                        iconID = R.drawable.weather_lightning_white;
                        break;
                    case "13d":
                    case "13n":
                        iconID = R.drawable.weather_snowy_white;
                        break;
                    case "50d":
                    case "50n":
                        iconID = R.drawable.weather_rainy_white;
                        break;
                    default:
                        iconID = R.drawable.weather_sunny_white;
                        break;
                }
            }
            else {
                switch (icon) {
                    case "01d":
                        iconID = R.drawable.weather_sunny;
                        break;
                    case "01n":
                        iconID = R.drawable.weather_night;
                        break;
                    case "02d":
                    case "02n":
                        iconID = R.drawable.weather_partlycloudy;
                        break;
                    case "03d":
                    case "03n":
                        iconID = R.drawable.weather_cloudy;
                        break;
                    case "04d":
                    case "04n":
                        iconID = R.drawable.weather_partlycloudy;
                        break;
                    case "09d":
                    case "09n":
                        iconID = R.drawable.weather_pouring;
                        break;
                    case "10d":
                    case "10n":
                        iconID = R.drawable.weather_rainy;
                        break;
                    case "11d":
                    case "11n":
                        iconID = R.drawable.weather_lightning;
                        break;
                    case "13d":
                    case "13n":
                        iconID = R.drawable.weather_snowy;
                        break;
                    case "50d":
                    case "50n":
                        iconID = R.drawable.weather_rainy;
                        break;
                    default:
                        iconID = R.drawable.weather_sunny;
                        break;
                }
            }

            Day currDay = new Day(gc, temperatureObject.getDouble(OWM_MAX), temperatureObject.getDouble(OWM_MIN), description, iconID);
            forecastDays.add(currDay);
        }

        return forecastDays;
    }
}
