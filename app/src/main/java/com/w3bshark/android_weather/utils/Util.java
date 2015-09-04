/*
 * Copyright (c) 2015. Tyler McCraw
 */

package com.w3bshark.android_weather.utils;

import com.w3bshark.android_weather.R;

/**
 * Created by w3bshark on 6/27/2015.
 */
public final class Util {

    public static double convertFahrenheitToCelcius(double degreesInFahrenheit) {
        return (degreesInFahrenheit - 32) * 5/9;
    }

    public static double convertCelciusToFahrenheit(double degreesInCelcius) {
        return degreesInCelcius * 9/5 + 32;
    }

    public static int getFeaturedWeatherIcon(String OWMIconCode) {
        switch (OWMIconCode) {
            case "01d":
                return R.drawable.art_clear;
            case "01n":
                return R.drawable.art_clear;
            case "02d":
            case "02n":
                return R.drawable.art_light_clouds;
            case "03d":
            case "03n":
                return R.drawable.art_clouds;
            case "04d":
            case "04n":
                return R.drawable.art_light_clouds;
            case "09d":
            case "09n":
                return R.drawable.art_rain;
            case "10d":
            case "10n":
                return R.drawable.art_light_rain;
            case "11d":
            case "11n":
                return R.drawable.art_storm;
            case "13d":
            case "13n":
                return R.drawable.art_snow;
            case "50d":
            case "50n":
                return R.drawable.art_fog;
            default:
                return R.drawable.art_clear;
        }
    }

    public static int getItemWeatherIcon(String OWMIconCode) {
        switch (OWMIconCode) {
            case "01d":
                return R.drawable.ic_clear;
            case "01n":
                return R.drawable.ic_clear;
            case "02d":
            case "02n":
                return R.drawable.ic_light_clouds;
            case "03d":
            case "03n":
                return R.drawable.ic_cloudy;
            case "04d":
            case "04n":
                return R.drawable.ic_light_clouds;
            case "09d":
            case "09n":
                return R.drawable.ic_rain;
            case "10d":
            case "10n":
                return R.drawable.ic_light_rain;
            case "11d":
            case "11n":
                return R.drawable.ic_storm;
            case "13d":
            case "13n":
                return R.drawable.ic_snow;
            case "50d":
            case "50n":
                return R.drawable.ic_fog;
            default:
                return R.drawable.ic_clear;
        }
    }
}
