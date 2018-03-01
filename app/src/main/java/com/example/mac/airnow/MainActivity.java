package com.example.mac.airnow;

import android.content.Intent;
import android.location.Location;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

public class MainActivity extends AppCompatActivity {

    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MaterialSearchView searchView;
    private DashboardFragment dashboardFragment;
    private HelpFragment helpFragment;
    private BookmarksFragment bookmarksFragment;
    private TinyDB tinydb;


    @Override
    protected void onStart() {
        super.onStart();
//        LocationProvider locationProvider = new LocationProvider(getApplicationContext(), MainActivity.this);
//        locationProvider.checkPermission();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appBarLayout = findViewById(R.id.appbar);
        toolbar = findViewById(R.id.maintoolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("AirNow");

        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tablayout);

        dashboardFragment = new DashboardFragment();
        helpFragment = new HelpFragment();
        bookmarksFragment = new BookmarksFragment();

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(helpFragment, "Help");
        viewPagerAdapter.addFragment(dashboardFragment, "Dashboard");
        viewPagerAdapter.addFragment(bookmarksFragment, "Bookmarks");

        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(1);
        tabLayout.setupWithViewPager(viewPager);

        searchBarCode();
    }

    private void searchBarCode() {

        searchView = findViewById(R.id.searchview);
        searchView.setHint("Enter Location...");
        searchView.setEllipsize(true);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String queryLocation) {
                dashboardFragment.getQueryLocation(queryLocation, MainActivity.this);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2) {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.main_layout), "GPS Enabled", 2000);

            dashboardFragment.locationProvider.requestLocation();
            snackbar.show();
        }
        if(requestCode == 3) {
            Log.e("E", "Data enabled");
            if(!dashboardFragment.connectivityCheck.isConnected()){
                Log.e("E", "User didn't enabled data.");
                dashboardFragment.connectivityCheck.showDialog();
                Toast.makeText(getApplicationContext(), "Check your network connection.", Toast.LENGTH_LONG).show();
            }else {
                Log.e("E", "User did enabled data.");
                dashboardFragment.setUpDashboard();
            }
        }
    }
}
