package com.w3bshark.android_weather;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by w3bshark on 7/3/2015.
 */
public class TenDayForecastHandler {

    private final static String OWMDAILYFORECASTAPI = "/forecast/daily?";
    public ArrayList<Day> get10DayForecast(String postalCode) {

        String forecastResponse = "";

        try {
            String urlStr = OWMBASEURL.concat(OWMVERSION).concat(OWMDAILYFORECASTAPI)
                    .concat("q=" + "94043" + "&mode=json&units=metric&cnt=10")
                    .concat("&"+OWMAPIKEY);
            URL url = new URL (urlStr);

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
        } catch (IOException e) {
            Log.e("OWMRestClient", "Error ", e);
            return null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("OWMRestClient", "Error closing stream", e);
                }
            }
        }

        return parseResponseIntoDays(forecastResponse);
    }

    private ArrayList<Day> parseResponseIntoDays(String restResponse) {
        ArrayList<Day> days = new ArrayList<>();

        return days;
    }
}
