package com.example.dhirenchandnani.fuelo;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.MarkerView;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.annotations.Polyline;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.MyBearingTracking;
import com.mapbox.mapboxsdk.constants.MyLocationTracking;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationListener;
import com.mapbox.mapboxsdk.location.LocationServices;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.services.Constants;
import com.mapbox.services.commons.ServicesException;
import com.mapbox.services.commons.geojson.LineString;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.commons.turf.TurfException;
import com.mapbox.services.commons.turf.TurfMeasurement;
import com.mapbox.services.directions.v5.DirectionsCriteria;
import com.mapbox.services.directions.v5.MapboxDirections;
import com.mapbox.services.directions.v5.models.DirectionsResponse;
import com.mapbox.services.directions.v5.models.DirectionsRoute;
import com.mapbox.services.navigation.v5.RouteUtils;
//import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class NavigationModeActivity extends AppCompatActivity {

    private static final String TAG = "OffRouteDetection";

    // Map variables
    private MapView mapView;
    private MapboxMap map;
    private MarkerView car;
    private Marker destinationMarker;
    private Polyline routePolyline;
    private Position destination;
    private LocationServices locationServices;
    private static final int PERMISSIONS_LOCATION = 0;


    public String MAPBOX_ACCESS_TOKEN = "pk.eyJ1Ijoia3VuYWw0IiwiYSI6ImNpd3J4anp4dzEyNGwyb3BpeWFzcGxpNzcifQ.G6XpIffi1IVmdQgQRPxp_w";

    // Direction variables
    private DirectionsRoute currentRoute;
    private List<LatLng> routePoints;
    private int count = 0;
    private long distance;
    private Handler handler;
    private Runnable runnable;
    private boolean routeFinished = false;
    private boolean reRoute = false;
    private RouteUtils routeUtils;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        MapboxAccountManager.start(this, MAPBOX_ACCESS_TOKEN);

        // This contains the MapView in XML and needs to be called after the account manager
        setContentView(R.layout.activity_nav_mode);

        locationServices = LocationServices.getLocationServices(NavigationModeActivity.this);

        final Intent mapsIntent = getIntent();

        mapView = (MapView) findViewById(R.id.navMapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                map = mapboxMap;

                if (!locationServices.areLocationPermissionsGranted()) {
                    ActivityCompat.requestPermissions(NavigationModeActivity.this, new String[]{
                            android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_LOCATION);
                } else {
                    enableLocationTracking();
                   // enableLocation(true);
                }

                //Toast.makeText(NavigationModeActivity.this, "Press map to add destination", Toast.LENGTH_LONG).show();

                // origin used for starting point of car.
                Position origin = Position.fromCoordinates(mapsIntent.getDoubleExtra("Olong",0.0),mapsIntent.getDoubleExtra("Olat",0.0));
                Position destination = Position.fromCoordinates(mapsIntent.getDoubleExtra("Deslong",0.0),mapsIntent.getDoubleExtra("Deslat",0.0));

                CameraPosition position = new CameraPosition.Builder()
                        .target(new LatLng(origin.getLatitude(),origin.getLongitude())) // Sets the new camera position
                        .zoom(14) // Sets the zoom
                        .bearing(180) // Rotate the camera
                        .tilt(30) // Set the camera tilt
                        .build(); // Creates a CameraPosition from the builder

                mapboxMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(position), 5000);


                // Use the default 100 meter tolerance for off route.
                routeUtils = new RouteUtils();

                addCar(new LatLng(origin.getLatitude(), origin.getLongitude()));

                //map.setOnMapClickListener(new MapboxMap.OnMapClickListener() {
                  //  @Override
                    //public void onMapClick(@NonNull LatLng point) {

                      //  if (destinationMarker != null) {
                        //    map.removeMarker(destinationMarker);
                            reRoute = true;
                        //}

                map.addMarker(new MarkerOptions().position(new LatLng(destination.getLatitude(), destination.getLongitude())));


                        try {
                            getRoute(
                                    Position.fromCoordinates(car.getPosition().getLongitude(), car.getPosition().getLatitude()),
                                    Position.fromCoordinates(destination.getLongitude(), destination.getLatitude())
                            );
                        } catch (ServicesException servicesException) {
                            servicesException.printStackTrace();
                            Log.e(TAG, "onMapReady: " + servicesException.getMessage());
                        }

                    }
                });

            } // End onMapReady

     // End onCreate

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        if (handler != null && runnable != null) {
            handler.post(runnable);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        //stopSimulation();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    private void enableLocationTracking() {

        // Disable tracking dismiss on map gesture
        map.getTrackingSettings().setDismissAllTrackingOnGesture(false);

        // Enable location and bearing tracking
        map.getTrackingSettings().setMyLocationTrackingMode(MyLocationTracking.TRACKING_FOLLOW);
        map.getTrackingSettings().setMyBearingTrackingMode(MyBearingTracking.COMPASS);

        map.setOnMyLocationChangeListener(new MapboxMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(@Nullable Location location) {


                if (location != null) {
                    // Move the map camera to where the user location is and then remove the
                    // listener so the camera isn't constantly updating when the user location
                    // changes. When the user disables and then enables the location again, this
                    // listener is registered again and will adjust the camera once again.
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location), 14));

                }
                try{
                    checkIfOffRoute();
                }
                catch (TurfException T){

                }
                catch (ServicesException serviceexception){

                }

            }
        });
        map.setMyLocationEnabled(true);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableLocationTracking();
                //enableLocation(true);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // getMenuInflater().inflate(R.menu.menu_off_route, menu);
        return super.onCreateOptionsMenu(menu);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
////        switch (item.getItemId()) {
////            case R.id.action_1:
////                routeUtils = new RouteUtils(0.05);
////                break;
////            case R.id.action_2:
////                routeUtils = new RouteUtils(0.5);
////                break;
////            case R.id.action_3:
////                routeUtils = new RouteUtils(1);
////                break;
////            default:
////                routeUtils = new RouteUtils(0.1);
////                break;
//        }






    private void addCar(LatLng position) {
        // Using a custom car icon for marker.
        IconFactory iconFactory = IconFactory.getInstance(NavigationModeActivity.this);
        Drawable iconDrawable = ContextCompat.getDrawable(NavigationModeActivity.this, R.drawable.ic_car_top);
        Icon icon = iconFactory.fromDrawable(iconDrawable);

        // Add the car marker to the map.
        car = map.addMarker(new MarkerViewOptions()
                .position(position)
                .anchor(0.5f, 0.5f)
                .flat(true)
                .icon(icon)
        );
    }

    private void getRoute(Position origin, Position destination) throws ServicesException {
        ArrayList<Position> positions = new ArrayList<>();
        positions.add(origin);
        positions.add(destination);

        MapboxDirections client = new MapboxDirections.Builder()
                .setAccessToken(MapboxAccountManager.getInstance().getAccessToken())
                .setCoordinates(positions)
                .setProfile(DirectionsCriteria.PROFILE_DRIVING)
                .setSteps(true)
                .setOverview(DirectionsCriteria.OVERVIEW_FULL)
                .build();

        client.enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                // You can get generic HTTP info about the response
                Log.d(TAG, "Response code: " + response.code());
                if (response.body() == null) {
                    Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                    return;
                }

                // Print some info about the route
                currentRoute = response.body().getRoutes().get(0);
                Log.d(TAG, "Distance: " + currentRoute.getDistance());

                // Draw the route on the map
                drawRoute(currentRoute);
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                Log.e(TAG, "Error: " + throwable.getMessage());
            }
        });
    }


    private void drawRoute(DirectionsRoute route) {

        // Convert the route to latlng values and add to list.
        LineString lineString = LineString.fromPolyline(route.getGeometry(), Constants.OSRM_PRECISION_V5);
        List<Position> coordinates = lineString.getCoordinates();
        List<LatLng> newRoutePoints = new ArrayList<>();
        for (int j = 0; j < coordinates.size(); j++) {
            newRoutePoints.add(new LatLng(
                    coordinates.get(j).getLatitude(),
                    coordinates.get(j).getLongitude()));
        }

        // Remove the route line if it exist on map.
        if (routePolyline != null) {
            map.removePolyline(routePolyline);
        }

        // Draw Points on map
        routePolyline = map.addPolyline(new PolylineOptions()
                .addAll(newRoutePoints)
                .color(Color.parseColor("#56b881"))
                .width(5));

        // If car's already at the routes end, we need to start our runnable back up.
        if (routeFinished) {
            routeFinished = false;
            routePoints = newRoutePoints;
            count = 0;
            handler.post(runnable);
        }

        if (!reRoute) {
            routePoints = newRoutePoints;
            //startSimulation();
        }
    }

    private void checkIfOffRoute() throws ServicesException, TurfException {

        Position carCurrentPosition = Position.fromCoordinates(
                car.getPosition().getLongitude(),
                car.getPosition().getLatitude()
        );

        // TODO currently making the assumption that only 1 leg in route exist.
        if (routeUtils.isOffRoute(carCurrentPosition, currentRoute.getLegs().get(0))) {

            // Display message to user and stop simulation.
            Toast.makeText(NavigationModeActivity.this, "Off route", Toast.LENGTH_LONG).show();
            //stopSimulation();

            // Reset our variables
            reRoute = false;
            count = 0;

            // Get the route from car position to destination and begin simulating.
            getRoute(carCurrentPosition, destination);

        }
    } // End checkIfOffRoute



//    private void enableLocation(boolean enabled) {
//        if (enabled) {
//            // If we have the last location of the user, we can move the camera to that position.
//            Location lastLocation = map.getMyLocation();
//            if (lastLocation != null) {
//                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastLocation), 14));
//            }
//
//            locationListener = new LocationListener() {
//                @Override
//                public void onLocationChanged(Location location) {
//                    if (location != null) {
//                        // Move the map camera to where the user location is and then remove the
//                        // listener so the camera isn't constantly updating when the user location
//                        // changes. When the user disables and then enables the location again, this
//                        // listener is registered again and will adjust the camera once again.
//                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location), 14));
//                        locationServices.removeLocationListener(this);
//
//                    }
//                    try{
//                        checkIfOffRoute();
//                    }
//                    catch (TurfException T){
//
//                    }
//                    catch (ServicesException serviceexception){
//
//                    }
//                }
//            };
//            locationServices.addLocationListener(locationListener);
//
//
////            floatingActionButton.setImageResource(R.drawable.ic_location_disabled_24dp);
////        } else {
////            floatingActionButton.setImageResource(R.drawable.ic_my_location_24dp);
////        }
//            // Enable or disable the location layer on the map
//            map.setMyLocationEnabled(enabled);
//        }
//    }

//    private void startSimulation() {
//        // Typically you wouldn't need this method but since we want to simulate movement along a
//        // route, we use a handler to animate the car (similar behaviour to driving).
//        handler = new Handler();
//        runnable = new Runnable() {
//            @Override
//            public void run() {
//
//                // Check if we are at the end of the routePoints list, if so we want to stop using
//                // the handler.
//                if ((routePoints.size() - 1) > count) {
//
//                    // Calculating the distance is done between the current point and next.
//                    // This gives us the duration we will need to execute the ValueAnimator.
//                    // Multiplying by ten is done to slow down the marker speed. Adjusting
//                    // this value will result in the marker traversing faster or slower along
//                    // the line
//                    distance = (long) car.getPosition().distanceTo(routePoints.get(count)) * 10;
//
//                    // animate the marker from it's current position to the next point in the
//                    // points list.
//                    ValueAnimator markerAnimator = ObjectAnimator.ofObject(car, "position",
//                            new LatLngEvaluator(), car.getPosition(), routePoints.get(count));
//                    markerAnimator.setDuration(distance);
//                    markerAnimator.setInterpolator(new LinearInterpolator());
//                    markerAnimator.start();
//
//                    // This line will make sure the marker appears when it is being animated
//                    // and starts outside the current user view. Without this, the user must
//                    // intentionally execute a gesture before the view marker reappears on
//                    // the map.
//                    map.getMarkerViewManager().scheduleViewMarkerInvalidation();
//
//                    // Rotate the car (marker) to the correct orientation.
//                    car.setRotation((float) computeHeading(car.getPosition(), routePoints.get(count)));
//
//                    // Check that the vehicles off route or not. If you aren't simulating the car,
//                    // and want to use this example in the real world, the checkingIfOffRoute method
//                    // should go in a locationListener.
//                    try {
//                        checkIfOffRoute();
//                    } catch (ServicesException | TurfException turfException) {
//                        turfException.printStackTrace();
//                        Log.e(TAG, "check if off route error: " + turfException.getMessage());
//                    }
//
//                    // Keeping the current point count we are on.
//                    count++;
//
//                    // Once we finish we need to repeat the entire process by executing the
//                    // handler again once the ValueAnimator is finished.
//                    handler.postDelayed(this, distance);
//                } else {
//                    // Car's at the end of route so notify that we are finished.
//                    routeFinished = true;
//                }
//            }
//        };
//        handler.post(runnable);
//    } // End startSimulation
//
//    private void stopSimulation() {
//        if (handler != null) {
//            if (runnable != null) {
//                handler.removeCallbacks(runnable);
//            }
//        }
//    }

//    public static double computeHeading(LatLng from, LatLng to) {
//        // Compute bearing/heading using Turf and return the value.
//        return TurfMeasurement.bearing(
//                Position.fromCoordinates(from.getLongitude(), from.getLatitude()),
//                Position.fromCoordinates(to.getLongitude(), to.getLatitude())
//        );
//    }

//    private static class LatLngEvaluator implements TypeEvaluator<LatLng> {
//        // Method is used to interpolate the marker animation.
//        private LatLng latLng = new LatLng();
//
//        @Override
//        public LatLng evaluate(float fraction, LatLng startValue, LatLng endValue) {
//            latLng.setLatitude(startValue.getLatitude()
//                    + ((endValue.getLatitude() - startValue.getLatitude()) * fraction));
//            latLng.setLongitude(startValue.getLongitude()
//                    + ((endValue.getLongitude() - startValue.getLongitude()) * fraction));
//            return latLng;
//        }
//    }
}