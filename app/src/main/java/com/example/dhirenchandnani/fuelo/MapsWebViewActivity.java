package com.example.dhirenchandnani.fuelo;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.MyBearingTracking;
import com.mapbox.mapboxsdk.constants.MyLocationTracking;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.location.LocationServices;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.services.Constants;
import com.mapbox.services.commons.ServicesException;
import com.mapbox.services.commons.geojson.LineString;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.directions.v5.DirectionsCriteria;
import com.mapbox.services.directions.v5.MapboxDirections;
import com.mapbox.services.directions.v5.models.DirectionsResponse;
import com.mapbox.services.directions.v5.models.DirectionsRoute;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsWebViewActivity extends AppCompatActivity {


    private static final String TAG = "DirectionsActivity";

    private MapView mapView;
    private MapboxMap map;
    private DirectionsRoute currentRoute;
    private MapboxDirections client;
    public String MAPBOX_ACCESS_TOKEN = "pk.eyJ1Ijoia3VuYWw0IiwiYSI6ImNpd3J4anp4dzEyNGwyb3BpeWFzcGxpNzcifQ.G6XpIffi1IVmdQgQRPxp_w";
    String DistBet="", Marker_title = "";
    int k=0;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent mapsIntent = getIntent();


        DistBet = mapsIntent.getStringExtra(MapsActivity.Dist_Bet);
        Marker_title = mapsIntent.getStringExtra(MapsActivity.MARKER_TITLE);


        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        MapboxAccountManager.start(this, MAPBOX_ACCESS_TOKEN);

        // This contains the MapView in XML and needs to be called after the account manager
        setContentView(R.layout.activity_maps_web_view);

//        Handler handler = new Handler();
//
//        handler.postDelayed(new Runnable() {
//            public void run() {
//                finish();
//            }
//        }, 100);

            // Alhambra landmark in Granada, Spain.
        final Position origin = Position.fromCoordinates(mapsIntent.getDoubleExtra("Slong",0.0),mapsIntent.getDoubleExtra("Slat",0.0));

        // Plaza del Triunfo in Granada, Spain.
        final Position destination = Position.fromCoordinates(mapsIntent.getDoubleExtra("Dlong",0.0),mapsIntent.getDoubleExtra("Dlat",0.0));


        // Setup the MapView
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                map = mapboxMap;


                int zoomDist = (int)Float.parseFloat(DistBet);
                // Add origin and destination to the map
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(origin.getLatitude(), origin.getLongitude()))
                        .title("Origin")
                        .snippet("You"));
                Log.d("ORIGINLAT",origin.getLatitude()+"");
                Log.d("ORIGINLONG",origin.getLongitude()+"");
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(destination.getLatitude(), destination.getLongitude()))
                        .title("Destination")
                        .snippet(Marker_title));
                if(zoomDist<1128)
                    k=14;
                else if((zoomDist>1128)&&(zoomDist<2256))
                    k=13;
                else if((zoomDist>2256)&&(zoomDist<4513))
                    k=12;
                else if((zoomDist>4513)&&(zoomDist<9027))
                    k=11;
                else if((zoomDist>9027)&&(zoomDist<18055))
                    k=10;

                Log.d("ZoomDist:",zoomDist+"");
                Log.d("K=",k+"");

//                mapboxMap.setOnMapClickListener(new MapboxMap.OnMapClickListener() {
//                    @Override
//                    public void onMapClick(@NonNull LatLng point) {
//                        LatLngBounds latLngBounds = new LatLngBounds.Builder()
//                                .include(new LatLng(origin.getLatitude(), origin.getLongitude())) // Northeast
//                                .include(new LatLng(destination.getLatitude(), destination.getLongitude())) // Southwest
//                                .build();
//
//                        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 50), 5000);

//                    }
//                });



                CameraPosition position = new CameraPosition.Builder()
                        .target(new LatLng(origin.getLatitude(),origin.getLongitude())) // Sets the new camera position
                        .zoom(k) // Sets the zoom
                        .bearing(180) // Rotate the camera
                        .tilt(30) // Set the camera tilt
                        .build(); // Creates a CameraPosition from the builder

                mapboxMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(position), 5000);

                btn = (Button)findViewById(R.id.btnNav);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent2 = new Intent(MapsWebViewActivity.this, NavigationModeActivity.class);
                        intent2.putExtra("Olat",origin.getLatitude());
                        intent2.putExtra("Olong",origin.getLongitude());
                        intent2.putExtra("Deslat",destination.getLatitude());
                        intent2.putExtra("Deslong",destination.getLongitude());
                        intent2.putExtra("TITLE",Marker_title);
                        startActivity(intent2);
                    }
                });

                // Get route from API
                try {
                    getRoute(origin, destination);

                } catch (ServicesException servicesException) {
                    servicesException.printStackTrace();
                }


            }
        });
    }

    private void getRoute(Position origin, Position destination) throws ServicesException {

        client = new MapboxDirections.Builder()
                .setOrigin(origin)
                .setDestination(destination)
                .setProfile(DirectionsCriteria.PROFILE_DRIVING)
                .setAccessToken(MapboxAccountManager.getInstance().getAccessToken())
                .build();

        client.enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                // You can get the generic HTTP info about the response
                Log.d(TAG, "Response code: " + response.code());
                if (response.body() == null) {
                    Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                    return;
                } else if (response.body().getRoutes().size() < 1) {
                    Log.e(TAG, "No routes found");
                    return;
                }

                // Print some info about the route
                currentRoute = response.body().getRoutes().get(0);
                Log.d(TAG, "Distance: " + currentRoute.getDistance());
                Toast.makeText(
                        MapsWebViewActivity.this,
                        "Route is " + currentRoute.getDistance() + " meters long.",
                        Toast.LENGTH_SHORT).show();

                // Draw the route on the map
                drawRoute(currentRoute);
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                Log.e(TAG, "Error: " + throwable.getMessage());
                Toast.makeText(MapsWebViewActivity.this, "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void drawRoute(DirectionsRoute route) {
        // Convert LineString coordinates into LatLng[]
        LineString lineString = LineString.fromPolyline(route.getGeometry(), Constants.OSRM_PRECISION_V5);
        List<Position> coordinates = lineString.getCoordinates();
        LatLng[] points = new LatLng[coordinates.size()];
        for (int i = 0; i < coordinates.size(); i++) {
            points[i] = new LatLng(
                    coordinates.get(i).getLatitude(),
                    coordinates.get(i).getLongitude());
        }

        // Draw Points on MapView
        map.addPolyline(new PolylineOptions()
                .add(points)
                .color(Color.parseColor("#009688"))
                .width(5));
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Cancel the directions API request
        if (client != null) {
            client.cancelCall();
        }
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

}

