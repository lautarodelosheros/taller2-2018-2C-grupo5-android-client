package com.comprame;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.comprame.login.Session;
import com.comprame.mypurchases.MyPurchasesFragment;
import com.comprame.profile.ProfileFragment;
import com.comprame.search.SearchFragment;
import com.comprame.sell.SellFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    public Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        configureDrawerActionBar();
        configureNavigationView();

        session = new Session();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null)
            session.setSession((String) bundle.get("session"));

        searchFragment();
    }

    public final static int PLACE_PICKER_REQUEST = 999;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode){
                case PLACE_PICKER_REQUEST:
                    Fragment sellFragment = getSupportFragmentManager().findFragmentByTag("SellFragment");
                    sellFragment.onActivityResult(requestCode, resultCode, data);
            }
        }
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
            case R.id.nav_search:
                searchFragment();
                return true;
            case R.id.nav_purchases:
                myPurchases();
                return true;
        }
        throw new IllegalArgumentException("Unhandled Menu searchItem");
    }

    private void myPurchases() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, new MyPurchasesFragment())
                .commit();
    }

    private void profileFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, new ProfileFragment())
                .commit();
    }

    public void sellFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, new SellFragment(), "SellFragment")
                .commit();
    }

    public void searchFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, new SearchFragment())
                .commit();
    }


}
