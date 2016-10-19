package com.example.dhirenchandnani.fuelo;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class CameraActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView iv_photo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.cam_window);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);


        Button b = (Button)findViewById(R.id.fab);
        iv_photo = (ImageView)findViewById(R.id.imageView2);



//        if(!hasCamera()) {
//            b.setEnabled(false);
//        }
        }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK) {

            Log.d("ITS IN","0");
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap) extras.get("data");
            iv_photo.setImageBitmap(photo);
        }
        else
            Log.d("ITS OUT","1");
    }

//    private boolean hasCamera() {
//
//        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
//    }








}
