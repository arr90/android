package com.example.and_notas.activities;

import android.app.Dialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.and_notas.R;
import com.example.and_notas.dao.LocationDao;
import com.example.and_notas.util.MyLocation;
import com.example.and_notas.vo.Location;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, LocationListener, GoogleApiClient.OnConnectionFailedListener {

    private static final boolean LOCATION_IN_REAL_TIME   = false;
    private static final String  LOG_MAPS_ACTIVITY       = MapsActivity.class.getSimpleName();

    private GoogleMap mMap;
    private Marker marker;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_MAPS_ACTIVITY, "LOG ["+Thread.currentThread().getStackTrace()[2].getMethodName()+"] {**********}");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        initMap();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i(LOG_MAPS_ACTIVITY, "LOG ["+Thread.currentThread().getStackTrace()[2].getMethodName()+"] {**********}");

        mMap = googleMap;
        mMap = loadLocations();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleApiClient.connect();

        changeStyleMap();

//        goToMylocation();

//        android.location.Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

//        goToLocationZoom(new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude()), 15);
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));

        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<android.location.Location>() {
            @Override
            public void onSuccess(android.location.Location location) {
                if (location != null){
                    goToLocationZoom(new LatLng(location.getLatitude(),location.getLongitude()), 15);
                }
            }
        });

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                Log.i(LOG_MAPS_ACTIVITY, "LOG ["+Thread.currentThread().getStackTrace()[2].getMethodName()+"] {**********}");

            }

            @Override
            public void onMarkerDrag(Marker marker) {
                Log.i(LOG_MAPS_ACTIVITY, "LOG ["+Thread.currentThread().getStackTrace()[2].getMethodName()+"] {**********}");

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Log.i(LOG_MAPS_ACTIVITY, "LOG ["+Thread.currentThread().getStackTrace()[2].getMethodName()+"] {**********}");

                try {
                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    LatLng latLng = marker.getPosition();
                    List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

                    Address address = addressList.get(0);
                    marker.setTitle(address.getLocality());
                    marker.showInfoWindow();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                Log.i(LOG_MAPS_ACTIVITY, "LOG ["+Thread.currentThread().getStackTrace()[2].getMethodName()+"] {**********}");

                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                Log.i(LOG_MAPS_ACTIVITY, "LOG ["+Thread.currentThread().getStackTrace()[2].getMethodName()+"] {**********}");

                View view = getLayoutInflater().inflate(R.layout.info_window, null);

                TextView tvLocality = (TextView) view.findViewById(R.id.tv_locality);
                TextView tvLat      = (TextView) view.findViewById(R.id.tv_lat);
                TextView tvLng      = (TextView) view.findViewById(R.id.tv_lng);
                TextView tvSnippet  = (TextView) view.findViewById(R.id.tv_snippet);

                LatLng latLng = marker.getPosition();

                tvLocality.setText("Title: " + marker.getTitle() + " / ID: " + marker.getId());
                tvLat.setText("Latitude:  "  + latLng.latitude);
                tvLng.setText("Longitude: "  + latLng.longitude);
                tvSnippet.setText(marker.getSnippet());

                tvSnippet.setText(marker.getId());
                return view;
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.i(LOG_MAPS_ACTIVITY, "LOG ["+Thread.currentThread().getStackTrace()[2].getMethodName()+"] {**********}");

                if (marker != null){
                    marker.remove();
                }
                Toast.makeText(MapsActivity.this, latLng.latitude + ", " + latLng.longitude, Toast.LENGTH_SHORT).show();
            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener(){
            @Override
            public void onMapLongClick(LatLng latLng) {
                Log.i(LOG_MAPS_ACTIVITY, "LOG ["+Thread.currentThread().getStackTrace()[2].getMethodName()+"] {**********}");

                setMarker("", latLng);
//                Toast.makeText(MapsActivity.this, latLng.latitude + ", " + latLng.longitude, Toast.LENGTH_SHORT).show();
            }
        });

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                Log.i(LOG_MAPS_ACTIVITY, "LOG ["+Thread.currentThread().getStackTrace()[2].getMethodName()+"] {**********}");

                return false;
            }
        });

        mMap.setMyLocationEnabled(true);
    }

    private void changeStyleMap() {
        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(this, R.raw.style_map_dark);
        mMap.setMapStyle(style);
    }

    private void goToMylocation() {
        Log.i(LOG_MAPS_ACTIVITY, "LOG ["+Thread.currentThread().getStackTrace()[2].getMethodName()+"] {**********}");

        MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
            @Override
            public void gotLocation(android.location.Location location) {
                Log.i(LOG_MAPS_ACTIVITY, "LOG ["+Thread.currentThread().getStackTrace()[2].getMethodName()+"] {**********}");
            }
        };

        MyLocation myLocation = new MyLocation();
        myLocation.getLocation(this, locationResult);
    }

    public void onMapSearch(View view) throws IOException {
        Log.i(LOG_MAPS_ACTIVITY, "LOG ["+Thread.currentThread().getStackTrace()[2].getMethodName()+"] {**********}");

        EditText locationSearch = (EditText) findViewById(R.id.editText);
        String location = locationSearch.getText().toString();

        Geocoder geocoder = new Geocoder(this);
        List<Address> addressList = geocoder.getFromLocationName(location,1);
        Address address = addressList.get(0);
        String locality = address.getLocality();

        Toast.makeText(this, locality, Toast.LENGTH_LONG).show();

        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

        goToLocationZoom(latLng, 15);

        setMarker(locality, latLng);

//        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    private void goToLocation(LatLng latLng) {
        Log.i(LOG_MAPS_ACTIVITY, "LOG ["+Thread.currentThread().getStackTrace()[2].getMethodName()+"] {**********}");

        CameraUpdate cameraUpdate =  CameraUpdateFactory.newLatLng(latLng);
        mMap.moveCamera(cameraUpdate);
    }

    private void goToLocationZoom(LatLng latLng, float zoom) {
        Log.i(LOG_MAPS_ACTIVITY, "LOG ["+Thread.currentThread().getStackTrace()[2].getMethodName()+"] {**********}");

        CameraUpdate cameraUpdate =  CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        mMap.moveCamera(cameraUpdate);
    }

    private void setMarker(String locality, LatLng latLng) {
        Log.i(LOG_MAPS_ACTIVITY, "LOG ["+Thread.currentThread().getStackTrace()[2].getMethodName()+"] {**********}");

        if (marker != null){
            marker.remove();
        }
        MarkerOptions markerOptions = new MarkerOptions()
                .title(locality)
                .draggable(true)
//                .icon(BitmapDescriptorFactory.fromResource(R.id.action_add_note))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                .position(latLng)
                .snippet("I am Here");

        marker = mMap.addMarker(markerOptions);
    }

    private GoogleMap loadLocations(){
        Log.i(LOG_MAPS_ACTIVITY, "LOG ["+Thread.currentThread().getStackTrace()[2].getMethodName()+"] {**********}");

        LocationDao locationDao = null;

        try {
            locationDao = new LocationDao(this);
            locationDao.open();
            List<Location> locations = locationDao.getAllTest();

            for (Location location : locations) {
                mMap.addMarker(
                        new MarkerOptions()
                                .position(location.getLatLng())
                                .title(location.getTitle())
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                                .snippet(String.valueOf(location.getId()))
                );
            }

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (locationDao != null)
                locationDao.close();
        }
        return mMap;
    }

    private void initMap() {
        Log.i(LOG_MAPS_ACTIVITY, "LOG ["+Thread.currentThread().getStackTrace()[2].getMethodName()+"] {**********}");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public boolean googleServiceAvailable(){
        Log.i(LOG_MAPS_ACTIVITY, "LOG ["+Thread.currentThread().getStackTrace()[2].getMethodName()+"] {**********}");

        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);

        if (isAvailable == ConnectionResult.SUCCESS){
            return true;
        }else if(api.isUserResolvableError(isAvailable)){
            Dialog dialog = api.getErrorDialog(this,isAvailable,0);
            dialog.show();
        }else{
            Toast.makeText(this,"Cant connect to play services",Toast.LENGTH_LONG).show();
        }
        return false;
    }

    public void onNormalMap(View view) {
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    public void onSatelliteMap(View view) {
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }

    public void onTerrainMap(View view) {
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
    }

    public void onHybridMap(View view) {
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(LOG_MAPS_ACTIVITY, "LOG ["+Thread.currentThread().getStackTrace()[2].getMethodName()+"] {**********}");

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(LOG_MAPS_ACTIVITY, "LOG ["+Thread.currentThread().getStackTrace()[2].getMethodName()+"] {**********}");


        android.location.Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        goToLocationZoom(new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude()), 15);

        if (LOCATION_IN_REAL_TIME){
            mLocationRequest = LocationRequest.create();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setInterval(2000);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(LOG_MAPS_ACTIVITY, "LOG ["+Thread.currentThread().getStackTrace()[2].getMethodName()+"] {**********}");

        if (mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(LOG_MAPS_ACTIVITY, "LOG ["+Thread.currentThread().getStackTrace()[2].getMethodName()+"] {**********}");
    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        Log.i(LOG_MAPS_ACTIVITY, "LOG ["+Thread.currentThread().getStackTrace()[2].getMethodName()+"] {**********}");

        if(LOCATION_IN_REAL_TIME){
            if (location == null){
                Toast.makeText(this, "Cant get current location", Toast.LENGTH_LONG).show();
            } else{
                LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                mMap.animateCamera(cameraUpdate);
            }
        }
    }

}