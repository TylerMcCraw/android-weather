package com.w3bshark.android_weather;

import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by w3bshark on 6/28/2015.
 */
public class TenDayForecastFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerAdapter mRecyclerAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Day> days;
    private View mCoordinatorLayoutView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.tendayforecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                initializeData();
                initializeAdapter();
                mRecyclerView.refreshDrawableState();
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

        return rootView;
    }

    private void initializeData(){
        days = new ArrayList<>();

        MainActivity mainActivity = (MainActivity) getActivity();
        Location location = mainActivity.getLocation();
        TenDayForecastHandler tenDayForecastHandler = new TenDayForecastHandler(getActivity().getApplicationContext())
        {
            @Override
            protected void onPostExecute(ArrayList<Day> result) {
                days.clear();
                days.addAll(result);
                if (mRecyclerAdapter == null) {
                    initializeAdapter();
                }
                else {
                    mRecyclerAdapter.notifyDataSetChanged();
                }
            }
        };
        tenDayForecastHandler.execute(location);
    }

    private void initializeAdapter(){
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String snackMessage = "";
                int itemPosition = mRecyclerView.getChildPosition(v);
                snackMessage = days.get(itemPosition).date.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
//                if (v instanceof CardView) {
//                    CardView cv = (CardView) v;
//                    int cardViewID;
//                    try {
//                        cardViewID = Integer.parseInt(cv.getTag().toString());
//                    } catch (NumberFormatException nfe) {
//                        cardViewID = 0;
//                    }
//                }
                Snackbar.make(mCoordinatorLayoutView, snackMessage, Snackbar.LENGTH_SHORT)
                        .show();
            }
        };

        mRecyclerAdapter = new RecyclerAdapter(days, clickListener);
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }
}
