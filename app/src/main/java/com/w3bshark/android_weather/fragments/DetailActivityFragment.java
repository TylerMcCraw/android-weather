/*
 * Copyright (c) 2015. Tyler McCraw
 */

package com.w3bshark.android_weather.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.w3bshark.android_weather.R;
import com.w3bshark.android_weather.utils.Util;
import com.w3bshark.android_weather.activities.DetailActivity;
import com.w3bshark.android_weather.model.Day;

import java.util.Calendar;
import java.util.Locale;

public class DetailActivityFragment extends Fragment {

    private Day selectedDay;
    // Day parcelable key for saving instance state
    private static final String SAVED_DAY = "SAVED_DAY";

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Attempt to restore from savedInstanceState
        if (savedInstanceState != null) {
            selectedDay = savedInstanceState.getParcelable(SAVED_DAY);
        }

        View detailFragment = inflater.inflate(R.layout.fragment_detail, container, false);
        if (getActivity().getIntent() == null || !getActivity().getIntent().hasExtra(DetailActivity.EXTRASCURRENTDAY)) {
            String snackMessage;
            snackMessage = getActivity().getApplicationContext().getString(R.string.error_unexpected);
            if (this.getView() != null) {
                Snackbar.make(this.getView(), snackMessage, Snackbar.LENGTH_SHORT).show();
            }
        }
        else {
            if (selectedDay == null) {
                selectedDay = getActivity().getIntent().getParcelableExtra(DetailActivity.EXTRASCURRENTDAY);
            }

            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);

            Calendar tomorrow = Calendar.getInstance();
            tomorrow.add(Calendar.DAY_OF_YEAR, 1);
            tomorrow.set(Calendar.HOUR_OF_DAY, 0);
            tomorrow.set(Calendar.MINUTE, 0);
            tomorrow.set(Calendar.SECOND, 0);
            tomorrow.set(Calendar.MILLISECOND, 0);

            Calendar selectedCalDate = selectedDay.getDate();
            selectedCalDate.set(Calendar.HOUR_OF_DAY, 0);
            selectedCalDate.set(Calendar.MINUTE, 0);
            selectedCalDate.set(Calendar.SECOND, 0);
            selectedCalDate.set(Calendar.MILLISECOND, 0);

            // Day and Date
            TextView dayText = (TextView) detailFragment.findViewById(R.id.detail_day_textview);
            if (selectedCalDate.equals(today)) {
                dayText.setText(getActivity().getApplicationContext().getString(R.string.today));
            }
            else if (selectedCalDate.equals(tomorrow)) {
                dayText.setText(getActivity().getApplicationContext().getString(R.string.tomorrow));
            }
            else {
                dayText.setText(selectedDay.getDate().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));
            }
            TextView dateText = (TextView) detailFragment.findViewById(R.id.detail_date_textview);
            dateText.setText(selectedDay.getDate().getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
                    .concat(" ")
                    .concat(Integer.toString(selectedDay.getDate().get(Calendar.DAY_OF_MONTH))));

            // Handle Imperial vs Metric preference
            TextView maxTemp = (TextView) detailFragment.findViewById(R.id.detail_high_textview);
            TextView minTemp = (TextView) detailFragment.findViewById(R.id.detail_low_textview);
            SharedPreferences sharedPrefs =
                    PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
            String unitType = sharedPrefs.getString(
                    getActivity().getApplicationContext().getString(R.string.pref_units_key),
                    getActivity().getApplicationContext().getString(R.string.pref_units_metric));
            if (unitType.equals(getActivity().getApplicationContext().getString(R.string.pref_units_metric))) {
                maxTemp.setText(String.format("%.0f", Util.convertFahrenheitToCelcius(selectedDay.getTempMax())).concat("\u00B0"));
                minTemp.setText(String.format("%.0f", Util.convertFahrenheitToCelcius(selectedDay.getTempMin())).concat("\u00B0"));
            }
            else {
                maxTemp.setText(String.format("%.0f", selectedDay.getTempMax()).concat("\u00B0"));
                minTemp.setText(String.format("%.0f", selectedDay.getTempMin()).concat("\u00B0"));
            }

            // Weather image
            ImageView image = (ImageView) detailFragment.findViewById(R.id.detail_icon);
            image.setImageResource(Util.getFeaturedWeatherIcon(selectedDay.getIconCode()));
            TextView weatherDescr = (TextView) detailFragment.findViewById(R.id.detail_forecast_textview);
            weatherDescr.setText(selectedDay.getWeatherDescription());

            // Humidity
            TextView humidityText = (TextView) detailFragment.findViewById(R.id.detail_humidity_textview);
            humidityText.setText(getString(R.string.detail_humidity).concat(": ").concat(String.format("%.0f", selectedDay.getHumidity())).concat(" %"));


            // Pressure
            TextView pressureText = (TextView) detailFragment.findViewById(R.id.detail_pressure_textview);
            pressureText.setText(getString(R.string.detail_pressure).concat(": ").concat(String.format("%.0f", selectedDay.getPressure())).concat(" hPa"));

            // Wind
            TextView windText = (TextView) detailFragment.findViewById(R.id.detail_wind_textview);
            windText.setText(getString(R.string.detail_wind).concat(": ").concat(String.format("%.0f", selectedDay.getWind())).concat(" km/h "));
        }

        return detailFragment;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (getActivity().getIntent() != null &&
                getActivity().getIntent().getParcelableExtra(DetailActivity.EXTRASCURRENTDAY) != null) {
            savedInstanceState.putParcelable(SAVED_DAY,
                    getActivity().getIntent().getParcelableExtra(DetailActivity.EXTRASCURRENTDAY));
        }
        super.onSaveInstanceState(savedInstanceState);
    }
}