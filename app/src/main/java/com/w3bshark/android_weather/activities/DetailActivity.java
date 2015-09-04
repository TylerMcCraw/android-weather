/*
 * Copyright (c) 2015. Tyler McCraw
 */

package com.w3bshark.android_weather.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.w3bshark.android_weather.R;
import com.w3bshark.android_weather.utils.Util;
import com.w3bshark.android_weather.fragments.DetailActivityFragment;
import com.w3bshark.android_weather.model.Day;

import java.util.Calendar;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRASCURRENTDAY = "EXTRAS_CURRENT_DAY";
    private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();

    //Used for social sharing
    private ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailActivityFragment())
                    .commit();
        }
    }

    // Call to update the share intent for custom use
    public void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);
        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(getDefaultShareIntent());
        }
        else {
            Log.d(LOG_TAG, "ShareActionProvider is null");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Returns the default share intent
    private Intent getDefaultShareIntent(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        // FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET was deprecated as of API 21
        // This intent flag is important so that the activity is cleared from recent tasks
        //  whenever the activity is finished/closed
        if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        }
        else {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        }
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.detail_todays_forecast));

        // Retrieve maximum temp and minimum temp for shared text
        Day selectedDay = getIntent().getParcelableExtra(DetailActivity.EXTRASCURRENTDAY);
        String maxTemp = "";
        String minTemp = "";
        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String unitType = sharedPrefs.getString(
                getApplicationContext().getString(R.string.pref_units_key),
                getApplicationContext().getString(R.string.pref_units_metric));
        if (unitType.equals(getApplicationContext().getString(R.string.pref_units_imperial))) {
            maxTemp = String.format("%.0f", Util.convertFahrenheitToCelcius(selectedDay.getTempMax())).concat("\u00B0");
            minTemp = String.format("%.0f", Util.convertFahrenheitToCelcius(selectedDay.getTempMin())).concat("\u00B0");
        }
        else {
            maxTemp = String.format("%.0f", selectedDay.getTempMax()).concat("\u00B0");
            minTemp = String.format("%.0f", selectedDay.getTempMin()).concat("\u00B0");
        }

        // Build text for share provider
        String sharedText = selectedDay.getDate().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault())
                .concat(" ")
                .concat(selectedDay.getDate().getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()))
                .concat(" ")
                .concat(Integer.toString(selectedDay.getDate().get(Calendar.DAY_OF_MONTH)))
                .concat(" - ")
                .concat(selectedDay.getWeatherDescription())
                .concat(" - ")
                .concat(maxTemp)
                .concat(" / ")
                .concat(minTemp)
                .concat(" ")
                .concat(getString(R.string.detail_share_hashtag));

        intent.putExtra(Intent.EXTRA_TEXT, sharedText);
        return intent;
    }
}