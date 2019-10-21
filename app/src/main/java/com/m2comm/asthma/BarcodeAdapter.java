package com.m2comm.asthma;

import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BarcodeAdapter extends ArrayAdapter {

    ArrayList<BarcodeClass> BarcodeList;
    Context context;
    ListActivity activity;
    String address;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    public BarcodeAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context;
        BarcodeList = new ArrayList<BarcodeClass>();
    }

    public void add(BarcodeClass object) {
        BarcodeList.add(object);
        super.add(object);
    }

    public void reset() {
        BarcodeList.clear();
    }

    @Override
    public int getCount() {
        return BarcodeList.size();
    }

    @Override
    public BarcodeClass getItem(int index) {
        return BarcodeList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row = inflater.inflate(R.layout.barcode_list, parent, false);

        BarcodeClass mBarcode = (BarcodeClass) BarcodeList.get(position);

        TextView day = (TextView) row.findViewById(R.id.day);
        TextView in_time = (TextView) row.findViewById(R.id.in_time);
        TextView out_time = (TextView) row.findViewById(R.id.out_time);
        TextView residense_time = (TextView) row.findViewById(R.id.residense_time);
        day.setText(mBarcode.day);
        in_time.setText(mBarcode.in_time);
        out_time.setText(mBarcode.out_time);
        residense_time.setText(mBarcode.residense_time);

        return row;

    }

}