package com.w3bshark.android_weather;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by w3bshark on 6/21/2015.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.DayViewHolder> {

    public static class DayViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        ImageView appPhoto;
        TextView dayOfWeek;
        TextView weatherType;
        TextView tempMax;
        TextView tempMin;

        DayViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            appPhoto = (ImageView)itemView.findViewById(R.id.app_photo);
            dayOfWeek = (TextView)itemView.findViewById(R.id.dayOfWeek);
            weatherType = (TextView)itemView.findViewById(R.id.weatherType);
            tempMax = (TextView)itemView.findViewById(R.id.tempMax);
            tempMin = (TextView)itemView.findViewById(R.id.tempMin);
        }
    }

    List<Day> days;
    View.OnClickListener clickListener;

    RecyclerAdapter(List<Day> days, View.OnClickListener clickListener) {
        this.days = days;
        this.clickListener = clickListener;
    }

    @Override
    public DayViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_item, viewGroup, false);
        return new DayViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DayViewHolder dayViewHolder, int i) {
        dayViewHolder.cv.setTag(days.get(i).date);
        dayViewHolder.appPhoto.setImageResource(days.get(i).photoId);
        dayViewHolder.appPhoto.setContentDescription(days.get(i).weatherDescription);
        dayViewHolder.dayOfWeek.setText(days.get(i).date.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));
        dayViewHolder.weatherType.setText(days.get(i).weatherDescription);
        dayViewHolder.tempMax.setText(String.format("%.1f",days.get(i).tempMax).concat("\u00B0"));
        dayViewHolder.tempMin.setText(String.format( "%.1f",days.get(i).tempMin).concat("\u00B0"));
        dayViewHolder.cv.setOnClickListener(clickListener);
    }

    @Override
    public int getItemCount() {
        return days.size();
    }
}
