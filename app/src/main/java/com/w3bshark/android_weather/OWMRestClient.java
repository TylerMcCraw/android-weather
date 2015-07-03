package com.w3bshark.android_weather;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by w3bshark on 7/3/2015.
 */
public class OWMRestClient {

    private final static String OWMBASEURL = "http://api.openweathermap.org/data/";
    private final static String OWMVERSION = "2.5";
    private final static String OWMAPIKEY = "APPID=022d10b654648a3097c21e889f15539a";
    private HttpURLConnection urlConnection = null;
    private BufferedReader reader = null;

    public OWMRestClient() {

    }
}
