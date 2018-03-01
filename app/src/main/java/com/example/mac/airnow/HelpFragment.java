package com.example.mac.airnow;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.idunnololz.widgets.AnimatedExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HelpFragment extends Fragment {


    ExpandableListAdapter listAdapter;
    AnimatedExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, String> listDataChild;

    public HelpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View helpView = inflater.inflate(R.layout.fragment_help, container, false);
        expListView = helpView.findViewById(R.id.expandableListView);

        prepareListData();

        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (expListView.isGroupExpanded(groupPosition)) {
                    expListView.collapseGroupWithAnimation(groupPosition);
                }else {
                    expListView.expandGroupWithAnimation(groupPosition);
                }
                return true;
            }
        });

        return helpView;
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        listDataHeader.add("What is Particulate Matter?");
        listDataHeader.add("What is Fine Particulate Matter?");
        listDataHeader.add("What is AQI?");

        listDataChild.put(listDataHeader.get(0), "" +
                "Particulate matter is the sum of all solid and liquid particles suspended in air, many of which are hazardous. This complex mixture contains for instance dust, pollen, soot, smoke, and liquid droplets");
        listDataChild.put(listDataHeader.get(1), "" +
                "Based on size, particulate matter is often divided into two main groups:\n" +
                "\n" +
                "1. The coarse fraction contains the larger particles with a size ranging from 2.5 to 10 µm (PM10).\n" +
                "2. The fine fraction contains the smaller ones with a size up to 2.5 µm (PM2.5).");
        listDataChild.put(listDataHeader.get(2), "" +
                "Think of the AQI as a yardstick that runs from 0 to 500. The higher the AQI value, the greater the level of air pollution and the greater the health concern.\n\n" +
                "Each category corresponds to a different level of health concern. The six levels of health concern and what they mean are:\n" +
                "\n" +
                "\"Good\" AQI is 0 to 50. Air quality is considered satisfactory, and air pollution poses little or no risk.\n" +
                "\"Moderate\" AQI is 51 to 100. Air quality is acceptable; however, for some pollutants there may be a moderate health concern for a very small number of people. For example, people who are unusually sensitive to ozone may experience respiratory symptoms.\n" +
                "\"Unhealthy for Sensitive Groups\" AQI is 101 to 150. Although general public is not likely to be affected at this AQI range, people with lung disease, older adults and children are at a greater risk from exposure to ozone, whereas persons with heart and lung disease, older adults and children are at greater risk from the presence of particles in the air.\n" +
                "\"Unhealthy\" AQI is 151 to 200. Everyone may begin to experience some adverse health effects, and members of the sensitive groups may experience more serious effects.\n" +
                "\"Very Unhealthy\" AQI is 201 to 300. This would trigger a health alert signifying that everyone may experience more serious health effects.\n" +
                "\"Hazardous\" AQI greater than 300. This would trigger a health warnings of emergency conditions. The entire population is more likely to be affected.");

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.action_search).setVisible(false);
    }
}
