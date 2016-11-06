package com.example.dhirenchandnani.fuelo;

import android.graphics.Bitmap;

/**
 * Created by Belal on 10/29/2015.
 */
public class Config {

    public static String[] names;
    public static String[] payouts;
    public static String[] ids ;

    public static final String GET_URL = "https://api.hasoffers.com/Apiv3/json?NetworkId=payoom&Target=Affiliate_Offer&Method=findAll&api_key=69d747268ac9ca8972af13400b18a205b710d50678b7c1d175605714a64d5372&limit=5";
    public static final String TAG_NAME = "name";
    public static final String TAG_PAYOUT = "default_payout";
    public static final String TAG_ID="id";

    public Config(int i){
        names = new String[i];
        payouts = new String[i];
        ids = new String[i];
    }
}