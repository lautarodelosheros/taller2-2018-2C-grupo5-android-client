package com.comprame;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.comprame.search.SearchFragment;
import com.comprame.sell.SellFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        configureDrawerActionBar();
        configureNavigationView();
        searchFragment();
    }

    private void configureNavigationView() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void configureDrawerActionBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this
                , drawerLayout
                , toolbar
                , R.string.navigation_drawer_open
                , R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        switch (item.getItemId()) {
            case R.id.nav_sell:
                sellFragment();
                return true;
            case R.id.nav_profile:
                profileFragment();
                return true;
            case R.id.nav_purchase:
                buyFragment();
                return true;
            case R.id.nav_search:
                searchFragment();
                return true;
            case R.id.nav_help:
                helpFragment();
                return true;
        }
        throw new IllegalArgumentException("Unhandled Menu item");
    }

    private void helpFragment() {
        throw new IllegalArgumentException("Unhandled Menu item");
    }

    private void profileFragment() {
        throw new IllegalArgumentException("Unhandled Menu item");
    }

    public void buyFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, new SellFragment())
                .commit();
    }

    public void sellFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, new SellFragment())
                .commit();
    }

    public void searchFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, new SearchFragment())
                .commit();
    }

}

