package com.example.mac.airnow;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Collections;

public class LocationActivity extends AppCompatActivity {

    Toolbar locationToolbar;
    ListView listView;
    TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        listView = findViewById(R.id.location_list_view);

        locationToolbar = findViewById(R.id.location_toolbar);
        locationToolbar.setTitle("Select a location...");
        locationToolbar.setTitleTextColor(Color.WHITE);

        tinyDB = new TinyDB(getApplicationContext());

        final ArrayList<String> locations = new ArrayList<>();

        locationToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToMainActivity();
            }
        });
        Integer count = tinyDB.getInt("count");

        while(count!=0){
            LocationItem l = tinyDB.getObject("LocationItem " + count, LocationItem.class);
            locations.add(String.valueOf(l.getCity()));
            count--;
        }
        Collections.reverse(locations);
        CustomLocationAdapter customAdapter = new CustomLocationAdapter(LocationActivity.this, locations);

        listView.setAdapter(customAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("selectedLocation", position+1);
                setResult(1,intent);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        sendToMainActivity();
    }

    private void sendToMainActivity() {
        Intent intent = new Intent(LocationActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

