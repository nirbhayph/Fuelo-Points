package com.example.dhirenchandnani.fuelo;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GoogleMap.OnInfoWindowClickListener {
    public final static String MARKER_TITLE = "TITLE";
    public final static String MARKER_POSITION = "POSITION";
    public final static String CURRENT_LOCATION = "LOCATION";
    public final static String Dist_Bet = "DB";

    private GoogleMap mMap;
    double latitude;
    double longitude;
    static double lati = 0.0;
    static double longi = 0.0;
    String type = "gas_station";
    private int PROXIMITY_RADIUS = 4000;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    public Marker mar;
    static String userid = LoginActivity.userid;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);




        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        //Check if Google Play Services Available or not
        if (!CheckGooglePlayServices()) {
            Log.d("onCreate", "Finishing test case since Google Play Services are not available");
            finish();
        }
        else {
            Log.d("onCreate","Google Play Services available.");
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    private boolean CheckGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,0).show();
            }
            return false;
        }
        return true;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.style_json));


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                final Button b = (Button) findViewById(R.id.btnLG);
                b.setEnabled(false);
                final Button b1 = (Button) findViewById(R.id.btnUploadBill);
                b1.setEnabled(false);
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                final float[] results = new float[1];
                Location.distanceBetween(
                        (marker.getPosition().latitude),
                        (marker.getPosition().longitude),
                        MapsActivity.lati,
                        MapsActivity.longi,
                        results);
                final float f = results[0]/1000;
                DecimalFormat df = new DecimalFormat("#.00");
                marker.setSnippet(df.format(f) +"km");
                marker.showInfoWindow();
                Toast.makeText(MapsActivity.this,"MARKER CLICKED",Toast.LENGTH_SHORT).show();

                final Button b = (Button) findViewById(R.id.btnLG);
                b.setEnabled(true);
                final Button b1 = (Button) findViewById(R.id.btnUploadBill);
                b1.setEnabled(true);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("INTENTORIGIN",marker.getPosition().latitude+"");

//                        Intent mapintent = new Intent(MapsActivity.this, MarkerInfoActivity.class);
//                        mapintent.putExtra(CURRENT_LOCATION,mLastLocation);
//                        mapintent.putExtra(MARKER_TITLE,marker.getTitle());
//                        mapintent.putExtra(MARKER_POSITION,marker.getPosition());
//                        mapintent.putExtra(Dist_Bet,results[0]+"");
//                        startActivity(mapintent);

                        Intent intent = new Intent(MapsActivity.this, MapsWebViewActivity.class);
                        intent.putExtra("Slat",marker.getPosition().latitude);
                        intent.putExtra("Slong",marker.getPosition().longitude);
                        intent.putExtra("Dlat",MapsActivity.lati);
                        intent.putExtra("Dlong",MapsActivity.longi);

                        startActivity(intent);
                    }
                });
                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent mapintent = new Intent(MapsActivity.this, FormActivity.class);
                        startActivity(mapintent);
                    }
                });





                return true;
            }

        });


        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
                Log.d("","Current Location is getting plotted");
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }







        //googleMap.setOnMarkerClickListener(this);

        //googleMap.setOnInfoWindowClickListener(this);


    }
    public void func_Petrol(View v) {
        FloatingActionButton button = (FloatingActionButton) this.findViewById(R.id.btnPetrol);
        button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFB900")));

        FloatingActionButton button2 = (FloatingActionButton) this.findViewById(R.id.btnCng);
        button2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#121121")));

        FloatingActionButton button3 = (FloatingActionButton) this.findViewById(R.id.btnDiesel);
        button3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#121121")));


        String Petrol = "petrol";
        Log.d("onClick", "Button is Clicked");
                /* mMap.clear(); */   //clear the map
        String url = getUrl(latitude, longitude, Petrol);   //used to get information about nearby places
        Object[] DataTransfer = new Object[2];

        DataTransfer[0] = mMap;
        DataTransfer[1] = url;
        Log.d("onClick", url);
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
        getNearbyPlacesData.execute(DataTransfer);  //add markers
        Toast.makeText(MapsActivity.this,"Nearby Petrol Pumps", Toast.LENGTH_LONG).show();
    }

    public void func_CNG(View v) {
        FloatingActionButton button = (FloatingActionButton) this.findViewById(R.id.btnCng);
        button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFB900")));

        FloatingActionButton button2 = (FloatingActionButton) this.findViewById(R.id.btnPetrol);
        button2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#121121")));

        FloatingActionButton button3 = (FloatingActionButton) this.findViewById(R.id.btnDiesel);
        button3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#121121")));


        String Cng = "cng";
        Log.d("onClick", "Button is Clicked");

        String url = getUrl(latitude, longitude, Cng);
        Object[] DataTransfer = new Object[2];
        DataTransfer[0] = mMap;
        DataTransfer[1] = url;
        Log.d("onClick", url);
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
        getNearbyPlacesData.execute(DataTransfer);
        Toast.makeText(MapsActivity.this,"Nearby Cng Pumps", Toast.LENGTH_LONG).show();
    }

    public void func_Diesel(View v) {
        FloatingActionButton button = (FloatingActionButton) this.findViewById(R.id.btnDiesel);
        button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFB900")));

        FloatingActionButton button2 = (FloatingActionButton) this.findViewById(R.id.btnPetrol);
        button2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#121121")));

        FloatingActionButton button3 = (FloatingActionButton) this.findViewById(R.id.btnCng);
        button3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#121121")));

        String Diesel = "diesel";
        Log.d("onClick", "Button is Clicked");

        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        String url = getUrl(latitude, longitude, Diesel);
        Object[] DataTransfer = new Object[2];
        DataTransfer[0] = mMap;
        DataTransfer[1] = url;
        Log.d("onClick", url);
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
        getNearbyPlacesData.execute(DataTransfer);
        Toast.makeText(MapsActivity.this,"Nearby Diesel Pumps", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onInfoWindowClick(Marker marker){
        Intent mapintent = new Intent(this, MarkerInfoActivity.class);
        mapintent.putExtra(CURRENT_LOCATION,mLastLocation);
        mapintent.putExtra(MARKER_TITLE,marker.getTitle());
        mapintent.putExtra(MARKER_POSITION,marker.getPosition());
        startActivity(mapintent);
    }



//    @Override
//    public boolean onMarkerClick(final Marker marker) {
//        Intent mapintent = new Intent(this, MarkerInfoActivity.class);
//        mapintent.putExtra("Title",marker.getTitle());
//        startActivity(mapintent);
//
//        return true;
//    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)    //used to configure client.
                .addConnectionCallbacks(this)   //provides callbacks that are called when client connected or disconnected.
                .addOnConnectionFailedListener(this)    //scenarios of failed attempt of connect client to service.
                .addApi(LocationServices.API)   //adds the LocationServices API endpoint from Google Play Services.
                .build();
        mGoogleApiClient.connect();


            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            //**************************
            builder.setAlwaysShow(true); //this is the key ingredient
            //**************************

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    final LocationSettingsStates state = result.getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            // All location settings are satisfied. The client can initialize location
                            // requests here.
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the user
                            // a dialog.
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(
                                        MapsActivity.this, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.
                            break;
                    }
                }
            });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
// Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        break;
                    case Activity.RESULT_CANCELED:
                        {
                        Intent backIntent = new Intent(this,NavDrawerActivity.class);
                        startActivity(backIntent);
                        break;
                        }
                }
                break;
        }
    }



        //A client must be connected before excecuting any operation.


    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();   //get qos of location updates
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    private String getUrl(double latitude, double longitude, String nearbyPlace) {

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
       // googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&type=" + type);
        googlePlacesUrl.append("&name=" + nearbyPlace);
        googlePlacesUrl.append("&rankby=distance");
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyAL2hnUzJjnYPMf4_yrJCoKpYD_MvHdxlI");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("onLocationChanged", "entered");

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        lati = latitude;
        longi = longitude;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_person_pin_circle_white_24dp));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        Toast.makeText(MapsActivity.this,"Your Current Location", Toast.LENGTH_LONG).show();

        Log.d("onLocationChanged", String.format("latitude:%.3f longitude:%.3f",latitude,longitude));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            Log.d("onLocationChanged", "Removing Location Updates");
        }
        Log.d("onLocationChanged", "Exit");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // add here other case statements according to your requirement.
        }
    }


}