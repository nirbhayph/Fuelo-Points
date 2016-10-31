package com.example.dhirenchandnani.fuelo;

/**
 * Created by Dhiren Chandnani on 03-10-2016.
 */
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetNearbyPlacesData extends  AsyncTask<Object, String, String> {

    String googlePlacesData;
    GoogleMap mMap;
    String url;
    float[] results = new float[1];

    @Override
    protected String doInBackground(Object... params) {
        try {
            Log.d("GetNearbyPlacesData", "doInBackground entered");
            mMap = (GoogleMap) params[0];
            url = (String) params[1];
            DownloadUrl downloadUrl = new DownloadUrl();    //retrieve data from URL using HTTPUrl Connection
            googlePlacesData = downloadUrl.readUrl(url);
            Log.d("GooglePlacesReadTask", "doInBackground Exit");
        } catch (Exception e) {
            Log.d("GooglePlacesReadTask", e.toString());
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("GooglePlacesReadTask", "onPostExecute Entered");
        List<HashMap<String, String>> nearbyPlacesList = null;
        DataParser dataParser = new DataParser();
        nearbyPlacesList =  dataParser.parse(result);   //nearbyplaceslist will have info about nearby places
        ShowNearbyPlaces(nearbyPlacesList);
        Log.d("GooglePlacesReadTask", "onPostExecute Exit");
    }

    private void ShowNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) {
        mMap.clear();
        //Used to add markers
        for (int i = 0; i < nearbyPlacesList.size(); i++) {
            Log.d("onPostExecute","Entered into showing locations");
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));
            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_local_gas_station_white_24dp));

            mMap.addMarker(markerOptions);


            




        }


        Location.distanceBetween(
                Double.parseDouble(nearbyPlacesList.get(2).get("lat")),
                Double.parseDouble(nearbyPlacesList.get(2).get("lng")),
                MapsActivity.lati,
                MapsActivity.longi,
                results);
        Log.d("DISTANCE",results[0]+"");
        LatLng orig = new LatLng(MapsActivity.lati,MapsActivity.longi);
        MarkerOptions CurrmarkerOptions = new MarkerOptions();
        CurrmarkerOptions.position(orig);
        CurrmarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_person_pin_circle_white_24dp));
        mMap.addMarker(CurrmarkerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(orig));
        int zoomDist = (int)(results[0]);
        if(zoomDist<1128)
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        else if((zoomDist>1128)&&(zoomDist<2256))
            mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
        else if((zoomDist>2256)&&(zoomDist<4513))
            mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
        else if((zoomDist>4513)&&(zoomDist<9027))
            mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
        else if((zoomDist>9027)&&(zoomDist<18055))
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        else
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10));





    }







//    private String getdis(double latitude, double longitude, double dest_lat, double dest_lon) {
//
//        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=40.6655101,-73.89188969999998&destinations=40.6905615%2C-73.9976592%7C40.6905615%2C-73.9976592%7C40.6905615%2C-73.9976592%7C40.6905615%2C-73.9976592%7C40.6905615%2C-73.9976592%7C40.6905615%2C-73.9976592%7C40.659569%2C-73.933783%7C40.729029%2C-73.851524%7C40.6860072%2C-73.6334271%7C40.598566%2C-73.7527626%7C40.659569%2C-73.933783%7C40.729029%2C-73.851524%7C40.6860072%2C-73.6334271%7C40.598566%2C-73.7527626&key=YOUR_API_KEY");
//        googlePlacesUrl.append("location=" + latitude + "," + longitude);
//        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
//        googlePlacesUrl.append("&type=" + type);
//        googlePlacesUrl.append("&name=" + nearbyPlace);
//        googlePlacesUrl.append("&sensor=true");
//        googlePlacesUrl.append("&key=" + "AIzaSyAL2hnUzJjnYPMf4_yrJCoKpYD_MvHdxlI");
//        Log.d("getUrl", googlePlacesUrl.toString());
//        return (googlePlacesUrl.toString());
//    }
}
