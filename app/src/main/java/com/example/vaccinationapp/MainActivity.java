package com.example.vaccinationapp;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.example.vaccinationapp.fragments.CovidFragment;
import com.example.vaccinationapp.fragments.HomeFragment;
import com.example.vaccinationapp.fragments.ProfileFragment;
import com.example.vaccinationapp.fragments.SettingFragment;
import com.example.vaccinationapp.helpers.Constants;
import com.example.vaccinationapp.helpers.PrefManager;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    Activity mActivity;
    ProgressDialog progressDialog;
    String name;
    PrefManager manager;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = MainActivity.this;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        manager = new PrefManager(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView mHeaderName = headerView.findViewById(R.id.header_name);

        try {
            // Name, email address, and profile photo Url
            name = manager.getData(Constants.USER_NAME);
            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.

            if (!name.equalsIgnoreCase("")) {
                mHeaderName.setText("Welcome, " + name);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        //add this line to display menu1 when the activity is loaded
        displaySelectedScreen(R.id.nav_home);
        toolbar.setTitle("Home");
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.parent_home_menu, menu);

        if (menu instanceof MenuBuilder) {
            MenuBuilder m = (MenuBuilder) menu;
            //noinspection RestrictedApi
            m.setOptionalIconsVisible(true);
        }

        if (manager.getData(Constants.USER_TYPE).equals("Admin")) {
            MenuItem item = menu.findItem(R.id.add_child);
            item.setVisible(false);
        } else if (manager.getData(Constants.USER_TYPE).equals("Doctor")) {
            menu.clear();
        } else {
            Log.e("USER_TYPE", manager.getData(Constants.USER_TYPE));
        }

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.vaccination_info:
                Intent intent = new Intent(MainActivity.this, VaccinationInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.add_child:
                Intent intent1 = new Intent(MainActivity.this, AddChildActivity.class);
                startActivity(intent1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_home:
                fragment = new HomeFragment();
                toolbar.setTitle("Home");
                break;
            case R.id.nav_profile:
                fragment = new ProfileFragment();
                toolbar.setTitle("Profile");
                break;
            case R.id.nav_settings:
                fragment = new SettingFragment();
                toolbar.setTitle("Settings");
                break;
            case R.id.nav_covid:
                fragment = new CovidFragment();
                toolbar.setTitle("Covid Vaccines");
                break;
            case R.id.nav_logout:
                manager.saveData(Constants.EMAIL_ID, "");
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finishAffinity();
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.nav_host_fragment, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        //calling the method displayselectedscreen and passing the id of selected menu
        displaySelectedScreen(item.getItemId());
        //make this method blank
        return true;
    }
}