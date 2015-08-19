/*
 * Copyright (c) 2015. Tyler McCraw
 */

package com.w3bshark.android_weather;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

/**
 * Created by w3bshark on 6/27/2015.
 */
public class Day implements Parcelable {

    long dateInMillis;
    double tempMax;
    double tempMin;
    String weatherDescription;
    String iconCode;

    Day() {}

    Day(Parcel in) {
        this.dateInMillis = in.readLong();
        this.tempMax = in.readDouble();
        this.tempMin = in.readDouble();
        this.weatherDescription = in.readString();
//        this.tempCelcius = Util.convertFahrenheitToCelcius(tempFahrenheit);
        this.iconCode = in.readString();
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
}
