package com.example.dhirenchandnani.fuelo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class OfferActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Config config;

    Iterator keys;
    String[] keys_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

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
                parseJSON(s);
                showData();
            }

            @Override
            protected String doInBackground(Void... params) {
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(Config.GET_URL);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
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
        mAdapter = new MyAdapter(Config.names,Config.payouts, Config.ids);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void parseJSON(String json){
        try {


            JSONObject jsonObject = new JSONObject(json);
            JSONObject obj1 = (JSONObject)jsonObject.get("response");
            JSONObject obj2 = (JSONObject)obj1.get("data");
            JSONObject obj3 = (JSONObject) obj2.get("data");
            keys = obj3.keys();
            int y = 0;
            keys_list = new String[obj3.length()];
            while(keys.hasNext()){
                keys_list[y] = keys.next().toString();
                Log.d("List------>",y+"++++"+keys_list[y]);
                y++;
            }



            config = new Config(obj3.length());

            for(int i=0; i<obj3.length(); i++){
                JSONObject j = (JSONObject)obj3.get(keys_list[i]);
                JSONObject k = j.getJSONObject("Offer");

                Config.names[i] = getName(k);
                Config.payouts[i] = getPayouts(k);
                Config.ids[i] = getIds(k);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private String getName(JSONObject j){
        String name = null;
        try {
            name = j.getString(Config.TAG_NAME);
            Log.d("NAME---->",name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }

    private String getPayouts(JSONObject j){
        String payout = null;
        try {
            payout = j.getString(Config.TAG_PAYOUT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return payout;
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
















//        // specify an adapter (see also next example)
//        mAdapter = new MyAdapter(myDataset);
//        mRecyclerView.setAdapter(mAdapter);


    }
