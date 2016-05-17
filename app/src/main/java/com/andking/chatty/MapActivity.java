package com.andking.chatty;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.andking.chatty.utils.MyItemizedOverlay;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Runnable {
    private LocationManager locationManager;
    private ParseUser user;
    private MapView mapView;
    private MyItemizedOverlay myItemizedOverlay;
    private ProgressDialog dialog;
    private ArrayList<OverlayItem> items;
    private ArrayList<ParseUser> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        user = ParseUser.getCurrentUser();

        View header = LayoutInflater.from(this).inflate(
                R.layout.nav_header_map, null);
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFile(user.getParseFile("userphoto").getFile().getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bitmap == null) {
            ((ImageView) header.findViewById(
                    R.id.imageView)).setImageResource(R.drawable.user);
        } else {
            ((ImageView) header.findViewById(R.id.imageView)).setImageBitmap(bitmap);
        }
        navigationView.addHeaderView(header);
        ((TextView) header.findViewById(R.id.name)).setText(user.getUsername());
        ((TextView) header.findViewById(R.id.email)).setText(user.getEmail());
        navigationView.removeHeaderView(navigationView.getHeaderView(0));

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        mapView.getController().setZoom(13);
        items = new ArrayList<>();
        users = new ArrayList<>();
        Thread myThread = new Thread(this);
        myThread.start();
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
        getMenuInflater().inflate(R.menu.map, menu);
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

        if (id == R.id.nav_camera) {
            startActivity(new Intent(this, UserListActivity.class));
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            ParseUser.logOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                0, 10, locationListener);
    }




    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                user.put("location", new ParseGeoPoint(location.getLatitude(),
                        location.getLongitude()));
                user.saveInBackground();
            }
        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

    };

    public void updateMap () {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                items.clear();
                mapView.getOverlays().clear();
                mapView.getController().setZoom(12);
                GeoPoint thisUserPoint = null;
                GeoPoint userPoint = null;
                for (int i = 0; i < users.size(); i++) {
                    if (!ParseUser.getCurrentUser().getObjectId().equals(
                            users.get(i).getObjectId()
                    )) {
                         userPoint = new GeoPoint(users.get(i).getParseGeoPoint("location").getLatitude(),
                                 users.get(i).getParseGeoPoint("location").getLongitude());

                        OverlayItem userItem = new OverlayItem(
                               users.get(i).getUsername(),
                                "", userPoint);
                        items.add(userItem);
                    } else {
                        thisUserPoint =
                                new GeoPoint(users.get(i).getParseGeoPoint("location").getLatitude(),
                                        users.get(i).getParseGeoPoint("location").getLongitude());
                        OverlayItem userItem = new OverlayItem(
                                "YOU", "", thisUserPoint);
                        items.add(userItem);
                    }
                }
                myItemizedOverlay = new MyItemizedOverlay(getApplicationContext(), items);
                mapView.getController().setCenter(thisUserPoint);
                mapView.getOverlays().add(myItemizedOverlay);
                mapView.invalidate();
                }
        });
    }


    @Override
    public void run() {
        while (true) {
            SystemClock.sleep(1000);
            ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
            userQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if (e == null) {
                        for (ParseUser user : objects) {
                            users.add(user);
                        }
                        updateMap();
                    }
                }
            });
            SystemClock.sleep(60000);
        }
    }
}
