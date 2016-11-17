package com.example.dhirenchandnani.fuelo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import org.apache.http.annotation.Obsolete;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import static com.example.dhirenchandnani.fuelo.R.styleable.View;

public class OfferActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Config config;
    String checked_values = "";
    int cv_length = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mRecyclerView = (RecyclerView)findViewById(R.id.my_recycler_view);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

//       here

        Intent filteredIntent = getIntent();
        if(filteredIntent.hasExtra("FILTER")){
            checked_values = filteredIntent.getStringExtra("FILTER");
            cv_length = filteredIntent.getIntExtra("CVLength",0);
        }


//      to here

        getData();
    }

    private void getData() {
        class GetData extends AsyncTask<Void,Void,String> {

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
                    URL url = new URL(Config.GET_URL);

                    // here

                    String data  = URLEncoder.encode("checked_values", "UTF-8") + "=" + URLEncoder.encode(checked_values, "UTF-8");
                    data += "&" + URLEncoder.encode("cv_length", "UTF-8") + "=" + URLEncoder.encode(cv_length+"", "UTF-8");

                    // to here
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    //here
                    con.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());

                    wr.write( data );
                    wr.flush();

                    //to here


                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json+"\n");
                    }

                    return sb.toString().trim();


                }catch(Exception e){
                    return null;
                }
            }
        }
        GetData gd = new GetData();
        gd.execute();

    }

    public void showData(){
        mAdapter = new MyAdapter(OfferActivity.this, Config.names,Config.titles, Config.ids, Config.points,Config.turls);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void parseJSON(String json){
        try {


            JSONArray jsonArray = new JSONArray(json);

            config = new Config(jsonArray.length());

            for(int i=0; i<jsonArray.length(); i++){
                JSONObject k = jsonArray.getJSONObject(i);

                Config.titles[i] = getTitles(k);

                Config.ids[i] = getIds(k);
                Config.descrips[i] = getDescription(k);
                Config.categorys[i] = getCategory(k);
                Config.codes[i] = getCode(k);
                Config.turls[i] = getTUrl(k);
                Config.names[i] = getName(k);

                Config.points[i] = getPoints(k);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private String getName(JSONObject j){
        String name = null;
        try {
            name = j.getString(Config.TAG_NAME);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }

    private String getTitles(JSONObject j){
        String title = null;
        try {
            title = j.getString(Config.TAG_TITLE);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return title;
    }

    private String getPoints(JSONObject j){
        String point = null;
        try {
            point = j.getString(Config.TAG_POINTS);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return point;
    }

    private String getIds(JSONObject j){
        String ids = null;
        try {
            ids = j.getString(Config.TAG_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ids;
    }


    private String getDescription(JSONObject j){
        String description = null;
        try {
            description = j.getString(Config.TAG_DESCRIPTION);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return description;
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

    private String getCode(JSONObject j){
        String code = null;
        try {
            code = j.getString(Config.TAG_CODE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return code;
    }

    private String getTUrl(JSONObject j){
        String turl = null;
        try {
            turl = j.getString(Config.TAG_TURL);

            } catch (JSONException e) {
            e.printStackTrace();
        }
        return turl;
    }

    public void goToCategoryList(View view){
        Intent intent = new Intent(this,ListCategoriesActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(getApplicationContext(),NavDrawerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        finish();
    }



}
