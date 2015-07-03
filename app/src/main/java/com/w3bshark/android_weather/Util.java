package com.w3bshark.android_weather;

/**
 * Created by w3bshark on 6/27/2015.
 */
public final class Util {

    public enum WeatherTypes {
        SUNNY("Sunny"),
        CLOUDY("Cloudy"),
        FOGGY("Foggy"),
        RAINY("Rainy");

        private final String text;

        WeatherTypes(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    };

    public static double convertFahrenheitToCelcius(double degreesInFahrenheit) {
        return (degreesInFahrenheit - 32) * 5/9;
    }

    public static double convertCelciusToFahrenheit(double degreesInCelcius) {
        return degreesInCelcius * 9/5 + 32;
    }
}
