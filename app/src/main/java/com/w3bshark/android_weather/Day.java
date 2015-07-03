package com.w3bshark.android_weather;

import java.util.Calendar;

/**
 * Created by w3bshark on 6/27/2015.
 */
public class Day {

    Calendar date;
    double tempFahrenheit;
    double tempCelcius;
    Util.WeatherTypes weatherType;
    int photoId;

    Day(Calendar date, double tempFahrenheit, Util.WeatherTypes weatherType, int photoId) {
        this.date = date;
        this.tempFahrenheit = tempFahrenheit;
        this.tempCelcius = Util.convertFahrenheitToCelcius(tempFahrenheit);
        this.weatherType = weatherType;
        this.photoId = photoId;
    }
}
