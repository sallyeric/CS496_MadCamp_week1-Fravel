package com.example.firstproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SimpleTextAdapter extends BaseAdapter {
    LayoutInflater inflater;
    private ArrayList<Item> items;

    public SimpleTextAdapter (Context context, ArrayList<Item> list) {
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items = list;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Item getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if ( view == null ) {
            view = inflater.inflate(R.layout.recyclerview_item, viewGroup, false);
        }

        Item item = items.get(i);

        TextView tv1 = (TextView)view.findViewById(R.id.name_tv);
        TextView tv2 = (TextView)view.findViewById(R.id.number_tv);

        tv1.setText(item.getName());
        tv2.setText(item.getNumber());

        return view;
    }
}
