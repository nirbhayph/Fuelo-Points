package com.example.dhirenchandnani.fuelo;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ListCategoriesActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{

    public String[] category_list;
    private SliderLayout mDemoSlider;
    String checked_values = "";
    ListView listView;
    public static final String CV = "FILTER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_categories);
        Log.d("Entered LCA","");
        getJSON();

        /* For Slider */
        mDemoSlider = (SliderLayout)findViewById(R.id.slider);


        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);

        DefaultSliderView dSliderView1 = new DefaultSliderView(this);
        dSliderView1
                .image("http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");
        mDemoSlider.addSlider(dSliderView1);

        DefaultSliderView dSliderView2 = new DefaultSliderView(this);
        dSliderView2
                .image("http://83e2u32cf1b4dlzbl29etyxt.wpengine.netdna-cdn.com/wp-content/uploads/2014/06/banner.png");
        mDemoSlider.addSlider(dSliderView2);

        DefaultSliderView dSliderView3 = new DefaultSliderView(this);
        dSliderView3
                .image("http://www.f-covers.com/cover/friends-4-facebook-cover-timeline-banner-for-fb.jpg");
        mDemoSlider.addSlider(dSliderView3);

        DefaultSliderView dSliderView4 = new DefaultSliderView(this);
        dSliderView4
                .image("http://orig06.deviantart.net/b675/f/2013/110/e/6/arrow_facebook_banner_by_cre5po-d62fdtu.jpg");
        mDemoSlider.addSlider(dSliderView4);

        DefaultSliderView dSliderView5 = new DefaultSliderView(this);
        dSliderView5
                .image("http://img11.deviantart.net/46ad/i/2011/022/8/4/sherlock_banner_by_danglingdingle-d37r6ov.jpg");
        mDemoSlider.addSlider(dSliderView5);


        mDemoSlider.setPresetTransformer(5);
        mDemoSlider.setDuration(20);

        /* Slider Creation Ends*/

    }


    private void getJSON() {

        class GetJson extends AsyncTask<Void,Void,String>{

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                Log.d("DATA----------->",s);
                parseJSON(s);
                showData();
            }

            @Override
            protected String doInBackground(Void... params) {
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL("http://109.73.164.163/FueloPoints/server_files/get_unique_category_list.php");
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json+"\n");
                    }

                    Log.d("JSON---->",sb.toString().trim());
                    return sb.toString().trim();


                }catch(Exception e){
                    return null;
                }

            }

        }
        GetJson gJ = new GetJson();
        gJ.execute();

}

    public void showData(){


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, category_list);
        listView = (ListView) findViewById(R.id.list_category);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.d("IN check","Listener");




            }
        });

    }

    public void filteredResults(View view){

        SparseBooleanArray sparseBooleanArray = listView.getCheckedItemPositions();

        int i = 0;
        while (i<sparseBooleanArray.size()){
            if(sparseBooleanArray.valueAt(i)){
                checked_values = checked_values + category_list[sparseBooleanArray.keyAt(i)]+"////";
            }
            i++;
        }


        String check_val = checked_values;

        check_val = check_val.replaceAll("////",",  ");
        check_val = check_val.replaceAll(",  $", "");


        Toast.makeText(this,check_val,Toast.LENGTH_SHORT).show();

        Intent filteredIntent = new Intent(this,OfferActivity.class);
        filteredIntent.putExtra(CV,checked_values);
        filteredIntent.putExtra("CVLength",i);
        startActivity(filteredIntent);

    }

    private void parseJSON(String json){
        try {


            JSONArray jsonArray = new JSONArray(json);
            category_list = new String[jsonArray.length()];
            for(int i=0; i<jsonArray.length(); i++){
                JSONObject k = jsonArray.getJSONObject(i);


                category_list[i] = getCategory(k);


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private String getCategory(JSONObject j){
        String category = null;
        try {
            category = j.getString(Config.TAG_CATEGORY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return category;
    }

    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this,slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
    }





    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {}



}
