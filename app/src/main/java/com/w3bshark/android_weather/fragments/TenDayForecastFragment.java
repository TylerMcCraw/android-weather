/*
 * Copyright (c) 2015. Tyler McCraw
 */

package com.w3bshark.android_weather.fragments;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.w3bshark.android_weather.R;
import com.w3bshark.android_weather.activities.DetailActivity;
import com.w3bshark.android_weather.activities.MainActivity;
import com.w3bshark.android_weather.model.Day;
import com.w3bshark.android_weather.rest.TenDayForecastHandler;
import com.w3bshark.android_weather.widget.RecyclerAdapter;

import java.util.ArrayList;

public class TenDayForecastFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerAdapter mRecyclerAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Day> days;
    // Movie parcelable key for saving instance state
    private static final String SAVED_DAYS = "SAVED_DAYS";
    private View mCoordinatorLayoutView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        //Set up the xml layout
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv);
        mCoordinatorLayoutView = rootView.findViewById(R.id.tendayforecast_coordinator_layout);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        // Set up initial adapter (until we retrieve our data) so there is no skipping the layout
        mRecyclerView.setAdapter(new RecyclerAdapter(getActivity(), new ArrayList<Day>(), null));


        // Attempt to restore weather data from savedInstanceState
        if (savedInstanceState != null) {
            days = savedInstanceState.getParcelableArrayList(SAVED_DAYS);
            if (mRecyclerAdapter == null) {
                initializeAdapter();
            } else {
                mRecyclerAdapter.notifyDataSetChanged();
            }
        }
        // If we couldn't retrieve days from a saved instance state
        if (days == null || days.size() == 0) {
            initializeData();
        }

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.tendayforecast_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initializeData();
                initializeAdapter();
                mRecyclerView.refreshDrawableState();
            }
        });
        mSwipeRefreshLayout.setColorSchemeResources(R.color.swiperefresh);

        return rootView;
    }

    private void initializeData(){
        if (days == null) {
            days = new ArrayList<>();
        }

        MainActivity mainActivity = (MainActivity) getActivity();
        Location location = mainActivity.getLocation();
        TenDayForecastHandler tenDayForecastHandler = new TenDayForecastHandler(getActivity().getApplicationContext())
        {
            @Override
            protected void onPostExecute(ArrayList<Day> result) {
                if (result != null && !result.isEmpty()) {
                    days.clear();
                    // It is required to call addAll because this causes the
                    // recycleradapter to realize that there is new data and to refresh the view
                    days.addAll(result);
                }
                if (mRecyclerAdapter == null) {
                    initializeAdapter();
                }
                else {
                    mRecyclerAdapter.notifyDataSetChanged();
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        };
        tenDayForecastHandler.execute(location);
    }

    private void initializeAdapter(){
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPosition = mRecyclerView.getChildLayoutPosition(v);
                Intent detailIntent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(DetailActivity.EXTRASCURRENTDAY, days.get(itemPosition));
                startActivity(detailIntent);

//            if (v instanceof CardView) {
//                CardView cv = (CardView) v;
//                int cardViewID;
//                try {
//                    cardViewID = Integer.parseInt(cv.getTag().toString());
//                } catch (NumberFormatException nfe) {
//                    cardViewID = 0;
//                }
//            }
            }
        };

        mRecyclerAdapter = new RecyclerAdapter(getActivity(), days, clickListener);
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (days != null) {
            savedInstanceState.putParcelableArrayList(SAVED_DAYS, days);
        }
        super.onSaveInstanceState(savedInstanceState);
    }
}