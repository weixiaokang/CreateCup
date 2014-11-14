package com.weixiaokang.locationrecord.adapter;


import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.weixiaokang.locationrecord.R;

public class WeatherAdapter extends BaseAdapter {

    private Context context;
    private String[] date, weather;
    private int[] image;
    public WeatherAdapter(Context context, String[] date, int[] image, String[] weather) {
        this.context = context;
        this.date = date;
        this.image = image;
        this.weather = weather;
    }

    @Override
    public int getCount() {
        return date.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.adapter_weather, null);
            TextView dateTv = (TextView) view.findViewById(R.id.date_tv);
            dateTv.setText(date[position]);
            ImageView imageView = (ImageView) view.findViewById(R.id.image_view);
            imageView.setImageResource(image[position]);
            TextView weatherTv = (TextView) view.findViewById(R.id.weather_tv);
            weatherTv.setText(weather[position]);
        } else {
            view = convertView;
        }
        return view;
    }
}