package com.example.mac.airnow;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mac on 19/02/18.
 */
class CustomLocationAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> list;

    public CustomLocationAdapter(Context context, ArrayList<String> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.location_item_layout, null);
        ImageView img = v.findViewById(R.id.list_location_icon);
        TextView text = v.findViewById(R.id.list_location_name);

        text.setText(list.get(position));
        img.setImageResource(R.drawable.list_item_location);
        return v;
    }
}
