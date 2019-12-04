package com.example.raul.verflix;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import static android.support.v4.app.ActivityCompat.requestPermissions;
import static android.support.v4.content.PermissionChecker.checkSelfPermission;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Busqueda.OnFragmentInteractionListener, Wikiseries.OnFragmentInteractionListener, Animeflv.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            verifyPermission();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Fragment fragment=new Busqueda();

        getSupportFragmentManager().beginTransaction().replace(R.id.content_home,fragment).commit();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment=new Busqueda();
        Intent intent=null;

        if (id == R.id.nav_camera) {
            fragment=new Busqueda();
        } else if (id == R.id.nav_gallery) {
            intent=new Intent(getApplicationContext(),Sugerencias.class);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
            fragment=new Wikiseries();

        }/* else if (id == R.id.nav_manage) {
            fragment=new Animeflv();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        getSupportFragmentManager().beginTransaction().replace(R.id.content_home,fragment).commit();
        item.setChecked(true);
        getSupportActionBar().setTitle(item.getTitle());



        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:

                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    Boolean verifyPermission() {
        int permsRequestCode = 100;
        String[] perms = {/*Manifest.permission.ACCESS_FINE_LOCATION,*/Manifest.permission.WRITE_EXTERNAL_STORAGE,/* Manifest.permission.ACCESS_COARSE_LOCATION,*/ Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE};
        int accessFinePermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        int accessCoarseLocation = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
        int writeExternalStorage = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readExternalStorage = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        int readPhoneState = checkSelfPermission(Manifest.permission.READ_PHONE_STATE);

        if (readPhoneState == PackageManager.PERMISSION_GRANTED/* && accessFinePermission == PackageManager.PERMISSION_GRANTED && accessCoarseLocation == PackageManager.PERMISSION_GRANTED && readExternalStorage == PackageManager.PERMISSION_GRANTED*/ && writeExternalStorage == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermissions(perms, permsRequestCode);
            return false;
        }
    }

}
