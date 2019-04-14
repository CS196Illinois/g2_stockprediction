package com.example.g2_stockprediction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.g2_stockprediction.R;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater = null;
    ArrayList<Stock> stockList;

    public MyAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setStockList(ArrayList<Stock> stockList) {
        this.stockList = stockList;
    }

    @Override
    public int getCount() {
        return stockList.size();
    }

    @Override
    public Object getItem(int position) {
        return stockList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return stockList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.listrow,parent,false);

        ((TextView)convertView.findViewById(R.id.Symbol)).setText(stockList.get(position).getSymbol());
        ((TextView)convertView.findViewById(R.id.price)).setText(String.valueOf(stockList.get(position).getPrice()));
        ((TextView)convertView.findViewById(R.id.pricelow)).setText(String.valueOf(stockList.get(position).getLow()));

        return convertView;
    }

}
