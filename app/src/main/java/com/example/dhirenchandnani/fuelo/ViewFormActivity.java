package com.example.dhirenchandnani.fuelo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Dhiren Chandnani on 21-10-2016.
 */
public class ViewFormActivity extends AppCompatActivity {


    static final int REQUEST_IMAGE_CAPTURE = 1;
    FloatingActionButton iv_photo;

    String billNo = "", petrolAmnt = "", date = "", time = "", nol="",carV="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_form_window);

        Intent intent3 = getIntent();

        billNo = intent3.getStringExtra(FormActivity.BN);
        petrolAmnt = intent3.getStringExtra(FormActivity.PA);
        date = intent3.getStringExtra(FormActivity.Date);
        time = intent3.getStringExtra(FormActivity.Time);
        nol = intent3.getStringExtra(FormActivity.Litres);
        carV = intent3.getStringExtra(FormActivity.CAR);


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);


        iv_photo = (FloatingActionButton) findViewById(R.id.thumbImage);

        TextView myTextView2=(TextView)findViewById(R.id.label_upload);
        Typeface typeFace=Typeface.createFromAsset(getAssets(),"fonts/Pacifico.ttf");
        myTextView2.setTypeface(typeFace);






        Log.d("Details:-----", billNo + "--" + petrolAmnt + "--" + date + "--" + time);
        TextView pa = (TextView) findViewById(R.id.paView);
        TextView bn = (TextView) findViewById(R.id.bnView);
        TextView v_date = (TextView) findViewById(R.id.dateView);
        TextView v_time = (TextView) findViewById(R.id.timeView);
        TextView v_nol = (TextView) findViewById(R.id.v_nol);
        TextView carView = (TextView) findViewById(R.id.carView);


        bn.setText(" " + billNo);
        v_date.setText(" " + date);
        v_time.setText(" " + time);
        pa.setText(" " + petrolAmnt);
        v_nol.setText(" " + nol);
        carView.setText(""+carV);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            Log.d("ITS IN", "0");
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap) extras.get("data");
            iv_photo.setImageBitmap(photo);



        }
    }

    private boolean hasCamera() {

        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }


    public void uploadDetails(View view){
        new SendDataActivity(this).execute(billNo,petrolAmnt,date,time,nol,MapsActivity.userid,carV,"http://google.com");

    }
}
