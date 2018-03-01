package com.example.mac.airnow;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Created by mac on 19/02/18.
 */

public class CustomInfoAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<String> params = new ArrayList<>();
    private ArrayList<String> values = new ArrayList<>();
    private String[] defaultKeys = new String[] {"co", "d", "h", "no2", "o3", "p", "pm10", "pm25", "so2", "t", "w", "wd"};
    private HashMap<String, String> defaultKeysFull = new HashMap<>();

    public CustomInfoAdapter(Context context, ArrayList<String> params, ArrayList<String> values){
        this.context = context;
        this.params = params;
        this.values = values;
    }

    @Override
    public int getCount() {
        return params.size();
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
        View v = View.inflate(context, R.layout.main_aqi_item, null);

        TextView paramName = v.findViewById(R.id.info_item_parameter);
        TextView value = v.findViewById(R.id.info_item_value);
        TextView unit = v.findViewById(R.id.info_item_unit);

        defaultKeysFull.put("co", "Carbon Monoxide");
        defaultKeysFull.put("d","Dew Point");
        defaultKeysFull.put("h","Humidity");
        defaultKeysFull.put("no2","Nitrogen Dioxide");
        defaultKeysFull.put("o3","Ground Level Ozone");
        defaultKeysFull.put("p","Pressure");
        defaultKeysFull.put("pm10","Particulate Matter");
        defaultKeysFull.put("pm25","Fine Particulate Matter");
        defaultKeysFull.put("so2","Sulphur Dioxide");
        defaultKeysFull.put("t", "Temperature");
        defaultKeysFull.put("w","Wind");
        defaultKeysFull.put("wd", "WD");

        paramName.setText(defaultKeysFull.get(params.get(position)));
        value.setText(String.valueOf(Math.round(Float.parseFloat(values.get(position)))));

        switch (String.valueOf(params.get(position))){
            case "p":
                unit.setText(" mb");
                break;
            case "h":
                unit.setText("%");
                break;
            case "t":
                unit.setText("°C");
                break;
            case "d":
                unit.setText("°C");
                break;
            default:
                unit.setText(" AQI");
                break;
        }

        return v;
    }
}
