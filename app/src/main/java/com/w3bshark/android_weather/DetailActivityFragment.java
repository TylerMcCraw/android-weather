package com.w3bshark.android_weather;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private Day selectedDay;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View detailFragment = inflater.inflate(R.layout.fragment_detail, container, false);
        if (getActivity().getIntent() == null || !getActivity().getIntent().hasExtra(DetailActivity.EXTRASCURRENTDAY)) {
            String snackMessage;
            snackMessage = getActivity().getApplicationContext().getString(R.string.error_unexpected);
            Snackbar.make(this.getView(), snackMessage, Snackbar.LENGTH_SHORT).show();
        }
        else {
            selectedDay = getActivity().getIntent().getParcelableExtra(DetailActivity.EXTRASCURRENTDAY);

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

            TextView dayText = (TextView) detailFragment.findViewById(R.id.detail_day_textview);
            if (selectedCalDate.equals(today)) {
                dayText.setText(getActivity().getApplicationContext().getString(R.string.today));
            }
            else if (selectedCalDate.equals(tomorrow)) {
                dayText.setText(getActivity().getApplicationContext().getString(R.string.today));
            }
            else {
                dayText.setText(selectedDay.getDate().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));
            }
            TextView dateText = (TextView) detailFragment.findViewById(R.id.detail_date_textview);
            dateText.setText(selectedDay.getDate().getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
                    .concat(" ")
                    .concat(Integer.toString(selectedDay.getDate().get(Calendar.DAY_OF_MONTH))));

            TextView maxTemp = (TextView) detailFragment.findViewById(R.id.detail_high_textview);
            maxTemp.setText(String.format("%.0f",selectedDay.getTempMax()).concat("\u00B0"));
            TextView minTemp = (TextView) detailFragment.findViewById(R.id.detail_low_textview);
            minTemp.setText(String.format("%.0f",selectedDay.getTempMin()).concat("\u00B0"));

            ImageView image = (ImageView) detailFragment.findViewById(R.id.detail_icon);
            image.setImageResource(Util.getFeaturedWeatherIcon(selectedDay.getIconCode()));
            TextView weatherDescr = (TextView) detailFragment.findViewById(R.id.detail_forecast_textview);
            weatherDescr.setText(selectedDay.getWeatherDescription());
        }

        return detailFragment;
    }
}
