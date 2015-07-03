package com.w3bshark.android_weather;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

//    public final static String POSTALCODEKEY = "POSTALCODE";
    public String postalCode = "";
    private final static long LOC_REFRESH_TIME = 0;
    private final static float LOC_REFRESH_DISTANCE = 100;
    private LocationManager mLocationManager;
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            postalCode = "";
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            } catch (IOException e)
            {
                // TODO: handle exception in finding addresses
            }
            if (addresses != null && !addresses.isEmpty()) {
                postalCode = addresses.get(0).getPostalCode();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOC_REFRESH_TIME,
                LOC_REFRESH_DISTANCE, mLocationListener);

        if (savedInstanceState == null) {
//            Bundle bundle = new Bundle();
//            bundle.putString(POSTALCODEKEY, postalCode);
//            TenDayForecastFragment tenDayForecastFragment = new TenDayForecastFragment();
//            tenDayForecast.setArguments(bundle);

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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String getPostalCode() {
        return  postalCode;
    }
}
