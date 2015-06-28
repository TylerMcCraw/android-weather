package com.w3bshark.weather;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
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
        TextView tempFahrenheit;
        TextView tempCelcius;

        DayViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            appPhoto = (ImageView)itemView.findViewById(R.id.app_photo);
            dayOfWeek = (TextView)itemView.findViewById(R.id.dayOfWeek);
            weatherType = (TextView)itemView.findViewById(R.id.weatherType);
            tempFahrenheit = (TextView)itemView.findViewById(R.id.tempFahrenheit);
            tempCelcius = (TextView)itemView.findViewById(R.id.tempCelcius);
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
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);

        dayViewHolder.cv.setTag(days.get(i).date);
        dayViewHolder.appPhoto.setImageResource(days.get(i).photoId);
        dayViewHolder.appPhoto.setContentDescription(days.get(i).weatherType.toString());
        dayViewHolder.dayOfWeek.setText(dayFormat.format(days.get(i).date.getTime()));
        dayViewHolder.weatherType.setText(days.get(i).weatherType.toString());
        dayViewHolder.tempFahrenheit.setText(String.format("%.1f",days.get(i).tempFahrenheit).concat("\u00B0 F"));
        dayViewHolder.tempCelcius.setText(String.format( "%.1f",days.get(i).tempCelcius).concat("\u00B0 C"));
        dayViewHolder.cv.setOnClickListener(clickListener);
    }

    @Override
    public int getItemCount() {
        return days.size();
    }
}
