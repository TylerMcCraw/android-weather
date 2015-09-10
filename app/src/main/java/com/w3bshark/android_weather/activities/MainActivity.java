/*
 * Copyright (c) 2015. Tyler McCraw
 */

package com.w3bshark.android_weather.activities;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.w3bshark.android_weather.R;
import com.w3bshark.android_weather.fragments.TenDayForecastFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Log tag to keep track of logs created against this class
    private final static String LOG_TAG = MainActivity.class.getSimpleName();
    // The default search radius when searching for places nearby.
    public static int DEFAULT_RADIUS = 150;
    // The maximum distance the user should travel between location updates.
    public static int MAX_DISTANCE = DEFAULT_RADIUS/2;
    // The maximum time that should pass before the user gets a location update.
    public static long MAX_TIME = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
    // Intent action for udpating location quickly once
    public static String SINGLE_LOCATION_UPDATE_ACTION = "WEATHER_SINGLE_LOC_UPDATE";
    // Notification ID for updating the notif later on
    private static int mId = 1;

    private LocationManager mLocationManager;
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) { }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) { }

        @Override
        public void onProviderEnabled(String provider) { }

        @Override
        public void onProviderDisabled(String provider) { }
    };

    private Intent updateIntent = new Intent(SINGLE_LOCATION_UPDATE_ACTION);
    private PendingIntent singleUpatePI;

    protected BroadcastReceiver singleUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            context.unregisterReceiver(singleUpdateReceiver);

            String key = LocationManager.KEY_LOCATION_CHANGED;
            Location location = (Location)intent.getExtras().get(key);

            if (mLocationListener != null && location != null)
                mLocationListener.onLocationChanged(location);

            singleUpatePI = PendingIntent.getBroadcast(getApplication(), 0, updateIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            mLocationManager.removeUpdates(singleUpatePI);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayShowHomeEnabled(false);
            View mActionBarView = getLayoutInflater().inflate(R.layout.main_action_bar, null);
            actionBar.setCustomView(mActionBarView);
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        }

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MAX_TIME,
//                MAX_DISTANCE, mLocationListener);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new TenDayForecastFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        else if (id == R.id.action_map) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Location currLoc = getLocation();
            if (currLoc != null) {
                Uri mapURI = Uri.parse("geo:".concat(Double.toString(currLoc.getLatitude()).concat(",").concat(Double.toString(currLoc.getLongitude()))));
                intent.setData(mapURI);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public Location getLocation() {
        Location bestResult = null;
        long minTime = System.currentTimeMillis();
        long maxTime = minTime - 86400000; // minimum time to check for refresh should be exactly one day ago
        float bestAccuracy = Float.MAX_VALUE;
        long bestTime = 0;

        try {
            List<String> matchingProviders = mLocationManager.getAllProviders();
            for (String provider : matchingProviders) {
                Location location = mLocationManager.getLastKnownLocation(provider);
                if (location != null) {
                    float accuracy = location.getAccuracy();
                    long time = location.getTime();

                    if ((time > minTime && accuracy < bestAccuracy)) {
                        bestResult = location;
                        bestAccuracy = accuracy;
                        bestTime = time;
                    } else if (time < minTime &&
                            bestAccuracy == Float.MAX_VALUE && time > bestTime) {
                        bestResult = location;
                        bestTime = time;
                    }
                }
            }

            if (mLocationListener != null &&
                    (bestTime < maxTime || bestAccuracy > MAX_DISTANCE)) {
                IntentFilter locIntentFilter = new IntentFilter(SINGLE_LOCATION_UPDATE_ACTION);
                getApplicationContext().registerReceiver(singleUpdateReceiver, locIntentFilter);
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_LOW);
                singleUpatePI = PendingIntent.getBroadcast(getApplication(), 0, updateIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                mLocationManager.requestSingleUpdate(criteria, singleUpatePI);
            }
        }
        catch (SecurityException se) {
            Log.e(LOG_TAG, "User has not given sufficient permissions for geolocation");
        }

        return bestResult;
    }
}
