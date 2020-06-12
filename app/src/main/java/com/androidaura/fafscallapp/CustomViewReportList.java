package com.androidaura.fafscallapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomViewReportList extends BaseAdapter {

    private final Activity context;
    ArrayList<HashMap<String, String>> data;

    public CustomViewReportList(Activity context,
                                ArrayList<HashMap<String, String>> arraylist) {
        this.context = context;
        data = arraylist;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = context.getLayoutInflater();

        if (view == null) {
            view = inflater.inflate(R.layout.layout_customviewreportlist, null);
            holder = new ViewHolder();
            holder.clv_number = (TextView) view.findViewById(R.id.clv_number);
            holder.clv_call_status = (TextView) view.findViewById(R.id.clv_call_status);

            view.setTag(holder);

        } else {

            holder = (ViewHolder) view.getTag();
        }

        HashMap<String, String> obj = data.get(position);

        holder.clv_call_status.setText(obj.get("clv_call_status"));
        holder.clv_number.setText(obj.get("clv_number"));

        if (obj.get("clv_call_status").equals("Interested")) {
            holder.clv_call_status.setTextColor(Color.GREEN);

        } else {
            holder.clv_call_status.setTextColor(Color.GRAY);
        }

        return view;
    }

    static class ViewHolder {
        TextView clv_number, clv_call_status;
    }
}