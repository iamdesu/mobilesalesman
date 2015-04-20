package com.bali.nusadua.productmonitor.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bali.nusadua.productmonitor.model.Outlet;
import com.bali.nusadua.productmonitor.model.Team;

import java.util.List;

/**
 * Created by desu sudarsana on 4/21/2015.
 */
public class SpinnerOutletAdapter extends ArrayAdapter<Outlet> {
    private Context mContext;
    private List<Outlet> mValues;

    public SpinnerOutletAdapter(Context context, int textViewResourceId, List<Outlet> objects){
        super(context, textViewResourceId, objects);
        this.mContext = context;
        this.mValues = objects;
    }

    @Override
    public int getCount() {
        return mValues.size();
    }

    @Override
    public Outlet getItem(int position){
        return mValues.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //This is for the first item before dropdown or default state
        TextView label = new TextView(mContext);
        label.setTextColor(Color.BLACK);
        label.setTextSize(18);
        label.setText(" " + mValues.get(position).getName());
        label.setHeight(50);
        label.setGravity(Gravity.LEFT | Gravity.CENTER );
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        //This is when user click the spinner and list of item display
        // beneath it
        TextView label = new TextView(mContext);
        label.setTextColor(Color.BLACK);
        label.setTextSize(18);
        label.setText(" " + mValues.get(position).getName());
        label.setHeight(70);
        label.setGravity(Gravity.LEFT | Gravity.CENTER );

        return label;
    }
}
