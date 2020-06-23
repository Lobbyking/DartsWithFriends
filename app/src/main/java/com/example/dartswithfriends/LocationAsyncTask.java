package com.example.dartswithfriends;


import android.location.Location;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import android.location.Location;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LocationAsyncTask extends AsyncTask<String, Integer, Standort>
{
    private static final String API_KEY = "caa7aebafc4ecb";
    private String lat;
    private String lon;

    public LocationAsyncTask(String lat, String lon)
    {
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    protected Standort doInBackground(String... strings)
    {
        try
        {
            HttpURLConnection connection = (HttpURLConnection) new URL("https://eu1.locationiq.com/v1/search.php?key="+API_KEY+"&q="+lat+","+lon+"&format=json").openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");


            int responseCode = connection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK)
            {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = br.readLine();

                JSONArray bruhArray = new JSONArray(line);
                JSONObject bruhObject = new JSONObject(bruhArray.getString(0));
                Standort standort = new Standort(bruhObject.getDouble("lat"), bruhObject.getDouble("lon"), bruhObject.getString("display_name"));
                return standort;

            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;

    }
}
