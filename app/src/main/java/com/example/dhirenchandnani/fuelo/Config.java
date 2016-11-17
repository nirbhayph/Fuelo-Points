package com.example.dhirenchandnani.fuelo;

import android.graphics.Bitmap;

public class Config {

    public static String[] names;
    public static String[] titles;
    public static String[] ids;
    public static String[] descrips;
    public static String[] categorys;
    public static String[] codes;
    public static String[] turls;
    public static String[] category_list;
    public static String[] points;


    public static final String GET_URL = "http://109.73.164.163/FueloPoints/server_files/recieveCoupons.php";
    public static final String TAG_ID = "Campaign_ID";
    public static final String TAG_NAME = "Campaign_Name";
    public static final String TAG_TITLE = "Title";
    public static final String TAG_DESCRIPTION = "Description";
    public static final String TAG_CATEGORY = "Category";
    public static final String TAG_CODE = "Type_Value";
    public static final String TAG_TURL = "Url";
    public static final String TAG_POINTS = "Points";




    public Config(int i){
        names = new String[i];
        titles = new String[i];
        ids = new String[i];
        categorys = new String[i];
        descrips = new String[i];
        codes = new String[i];
        turls = new String[i];
        category_list = new String[i];
        points = new String[i];
    }
}