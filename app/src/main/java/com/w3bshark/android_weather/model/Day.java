/*
 * Copyright (c) 2015. Tyler McCraw
 */

package com.w3bshark.android_weather.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

public class Day implements Parcelable {

    long dateInMillis;
    double tempMax;
    double tempMin;
    String weatherDescription;
    String iconCode;
    double humidity;
    double pressure;
    double wind;
    String windDirection;

    public Day() {}

    Day(Parcel in) {
        this.dateInMillis = in.readLong();
        this.tempMax = in.readDouble();
        this.tempMin = in.readDouble();
        this.weatherDescription = in.readString();
//        this.tempCelcius = Util.convertFahrenheitToCelcius(tempFahrenheit);
        this.iconCode = in.readString();
        this.humidity = in.readDouble();
        this.pressure = in.readDouble();
        this.wind = in.readDouble();
        this.windDirection = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(dateInMillis);
        dest.writeDouble(tempMax);
        dest.writeDouble(tempMin);
        dest.writeString(weatherDescription);
        dest.writeString(iconCode);
        dest.writeDouble(humidity);
        dest.writeDouble(pressure);
        dest.writeDouble(wind);
        dest.writeString(windDirection);
    }

    public static final Parcelable.Creator<Day> CREATOR
            = new Parcelable.Creator<Day>() {

        public Day createFromParcel(Parcel in) {
            return new Day(in);
        }

        public Day[] newArray(int size) {
            return new Day[size];
        }
    };

    public Calendar getDate() {
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(dateInMillis);
        return date;
    }

    public long getDateInMillis() {
        return dateInMillis;
    }

    public void setDateInMillis(long dateInMillis) {
        this.dateInMillis = dateInMillis;
    }

    public double getTempMax() {
        return tempMax;
    }

    public void setTempMax(double tempMax) {
        this.tempMax = tempMax;
    }

    public double getTempMin() {
        return tempMin;
    }

    public void setTempMin(double tempMin) {
        this.tempMin = tempMin;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    public String getIconCode() {
        return iconCode;
    }

    public void setIconCode(String iconCode) {
        this.iconCode = iconCode;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getWind() {
        return wind;
    }

    public void setWind(double wind) {
        this.wind = wind;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }
}
