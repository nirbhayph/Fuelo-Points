package com.example.dhirenchandnani.fuelo;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

public class MarkerInfoActivity extends AppCompatActivity  {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_info);

        Intent intent = getIntent();

        String marker_title = intent.getStringExtra(MapsActivity.MARKER_TITLE);
        //String marker_position = intent.getStringExtra(MapsActivity.MARKER_POSITION);
        final LatLng marker_position = intent.getExtras().getParcelable(MapsActivity.MARKER_POSITION);
        //String current_location = intent.getStringExtra(MapsActivity.CURRENT_LOCATION);
        Location current_location = intent.getExtras().getParcelable(MapsActivity.CURRENT_LOCATION);

        TextView textView = new TextView(this);
        textView.setTextSize(20);
        textView.setText("Destination: "+marker_title);

        ViewGroup layout = (ViewGroup) findViewById(R.id.marker_info);
        layout.addView(textView);

        Button btnLetsGo = (Button) findViewById(R.id.btnLG);
        btnLetsGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+marker_position.latitude+","+marker_position.longitude);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

//        final Intent navintent = new Intent(Intent.ACTION_VIEW,
//                Uri.parse("+http://maps.google.com/maps?"+"saddr="
//                        + current_location.getLatitude() + "," + current_location.getLongitude() + "&daddr="
//                        + marker_position.latitude + "," + marker_position.longitude));
//        intent.setClassName("com.google.android.apps.maps",
//                "com.google.android.maps.MapsActivity");
//        startActivity(intent);

    }


}
