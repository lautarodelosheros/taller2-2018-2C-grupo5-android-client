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
import android.view.View;
import android.widget.TextView;

import com.comprame.categories.CategoriesFragment;
import com.comprame.login.Session;
import com.comprame.mypublications.MyPublicationsFragment;
import com.comprame.mypurchases.MyPurchasesFragment;
import com.comprame.mysellings.MySellingsFragment;
import com.comprame.notifications.FirebaseMessagingManager;
import com.comprame.profile.ProfileFragment;
import com.comprame.qrcode.BuyWithQRFragment;
import com.comprame.search.SearchFragment;
import com.comprame.sell.SellFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    public Session session;
    private FirebaseMessagingManager firebaseMessagingManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        configureDrawerActionBar();
        configureNavigationView();

        session = Session.getInstance();

        Bundle bundle = getIntent().getExtras();
        String name = "";
        if(bundle != null)
            name = bundle.getString("name");

        setHeaderData(name);

        firebaseMessagingManager = new FirebaseMessagingManager(this);

        searchFragment();
    }

    private void setHeaderData(String name) {
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.header_username);
        navUsername.setText(name);
    }

    public final static int PLACE_PICKER_REQUEST = 999;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode){
                case PLACE_PICKER_REQUEST:
                    Fragment sellFragment = getSupportFragmentManager().findFragmentByTag("SellFragment");
                    if (sellFragment != null)
                        sellFragment.onActivityResult(requestCode, resultCode, data);
                    else {
                        Fragment searchFragment = getSupportFragmentManager().findFragmentByTag("SearchFragment");
                        if (searchFragment != null)
                            searchFragment.onActivityResult(requestCode, resultCode, data);

                    }
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
            case R.id.nav_publications:
                myPublications();
                return true;
            case R.id.nav_purchases:
                myPurchases();
                return true;
            case R.id.nav_sellings:
                mySellings();
                return true;
            case R.id.nav_qr_code:
                buyWithQRFragment();
                return true;
        }
        return false;
    }

    private void myPublications() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, new MyPublicationsFragment())
                .addToBackStack(null)
                .commit();
    }

    private void myPurchases() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, new MyPurchasesFragment())
                .addToBackStack(null)
                .commit();
    }

    private void mySellings() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, new MySellingsFragment())
                .addToBackStack(null)
                .commit();
    }

    private void profileFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, new ProfileFragment())
                .addToBackStack(null)
                .commit();
    }

    public void sellFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, new SellFragment(), "SellFragment")
                .addToBackStack(null)
                .commit();
    }

    public void searchFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, new CategoriesFragment(), "CategoriesFragment")
                .addToBackStack(null)
                .commit();
    }

    public void buyWithQRFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, new BuyWithQRFragment(), "BuyWithQRFragment")
                .addToBackStack(null)
                .commit();
    }
}
