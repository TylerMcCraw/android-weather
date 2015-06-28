package com.w3bshark.weather;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private RecyclerView mRecyclerView;
        private RecyclerView.LayoutManager mLayoutManager;
        private List<Day> days;
        private View mCoordinatorLayoutView;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv);
            mCoordinatorLayoutView = rootView.findViewById(R.id.snackbarPosition);

            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            mRecyclerView.setHasFixedSize(true);

            mLayoutManager = new LinearLayoutManager(this.getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);

            initializeData();
            initializeAdapter();

            return rootView;
        }

        private void initializeData(){
            days = new ArrayList<>();
            days.add(new Day(Calendar.getInstance(), 90.4, Util.WeatherTypes.SUNNY, R.drawable.ic_launcher));
        }

        private void initializeAdapter(){
            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String snackMessage = "";
                    if (v instanceof CardView) {
                        CardView cv = (CardView) v;
                        int cardViewID;
                        try {
                            cardViewID = Integer.parseInt(cv.getTag().toString());
                        } catch (NumberFormatException nfe) {
                            cardViewID = 0;
                        }
//                    switch(cardViewID)
//                    {
//                        case 0:
//                            snackMessage = getString(R.string.app_spotify_streamer);
//                            break;
//                        case 1:
//                            snackMessage = getString(R.string.app_scores);
//                            break;
//                        case 2:
//                            snackMessage = getString(R.string.app_library);
//                            break;
//                        case 3:
//                            snackMessage = getString(R.string.app_build_it_bigger);
//                            break;
//                        case 4:
//                            snackMessage = getString(R.string.app_xyz_reader);
//                            break;
//                        case 5:
//                            snackMessage = getString(R.string.app_capstone);
//                            break;
//                        default:
//                            snackMessage = getString(R.string.error);
//                    }
//                }
                        Snackbar.make(mCoordinatorLayoutView, snackMessage, Snackbar.LENGTH_SHORT)
                                .show();
                    }
                }
            };

            RecyclerAdapter adapter = new RecyclerAdapter(days, clickListener);
            mRecyclerView.setAdapter(adapter);
        }
    }
}
