package com.w3bshark.android_weather;

import java.util.Calendar;

/**
 * Created by w3bshark on 6/27/2015.
 */
public class Day {

    Calendar date;
    double tempMax;
    double tempMin;
    String weatherDescription;
    int photoId;

    Day(Calendar date, double tempMax, double tempMin, String weatherDescr, int photoId) {
        this.date = date;
        this.tempMax = tempMax;
        this.tempMin = tempMin;
        this.weatherDescription = weatherDescr;
//        this.tempCelcius = Util.convertFahrenheitToCelcius(tempFahrenheit);
        this.photoId = photoId;
    }
}
