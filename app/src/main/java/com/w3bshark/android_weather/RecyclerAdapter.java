package com.w3bshark.android_weather;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

    Context context;
    List<Day> days;
    View.OnClickListener clickListener;

    RecyclerAdapter(Context context, List<Day> days, View.OnClickListener clickListener) {
        this.context = context;
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
        if (i == 0) {
            //TODO: change the first day to a static view, cardview isn't going to work.
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int height = size.y;
            if (height == 0) {
                height = 300;
            }
            else {
                height = height * 2/5;
            }
            dayViewHolder.cv.setMinimumHeight(height);
            dayViewHolder.cv.setCardBackgroundColor(context.getResources().getColor(R.color.background));
            TextView dayOfWeek = (TextView)dayViewHolder.cv.findViewById(R.id.dayOfWeek);
            TextView weatherType = (TextView)dayViewHolder.cv.findViewById(R.id.weatherType);
            TextView tempMax = (TextView)dayViewHolder.cv.findViewById(R.id.tempMax);
            TextView tempMin = (TextView)dayViewHolder.cv.findViewById(R.id.tempMin);
            dayOfWeek.setTextColor(context.getResources().getColor(R.color.white));
            weatherType.setTextColor(context.getResources().getColor(R.color.white));
            tempMax.setTextColor(context.getResources().getColor(R.color.white));
            tempMin.setTextColor(context.getResources().getColor(R.color.white));
        }
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
